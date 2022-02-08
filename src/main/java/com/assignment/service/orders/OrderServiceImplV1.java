package com.assignment.service.orders;

import com.assignment.configurations.DiscountConfigs;
import com.assignment.dto.orders.CartItemDto;
import com.assignment.dto.orders.OrderDetailsDto;
import com.assignment.dto.orders.OrderDto;
import com.assignment.dto.orders.OrderResponseDto;
import com.assignment.dto.products.ProductDto;
import com.assignment.entity.orders.Order;
import com.assignment.entity.orders.OrderDetails;
import com.assignment.entity.products.Product;
import com.assignment.exceptions.BadRequestException;
import com.assignment.exceptions.EntityNotFoundException;
import com.assignment.helpers.ErrorConstants;
import com.assignment.helpers.RecordHolder;
import com.assignment.helpers.UserContext;
import com.assignment.helpers.enums.OrderStatus;
import com.assignment.helpers.enums.RecordStatus;
import com.assignment.repositories.OrderDetailsRepository;
import com.assignment.repositories.OrderRepository;
import com.assignment.repositories.ProductRepository;
import com.assignment.service.AuditingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplV1 implements IOrderService{

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final DiscountConfigs discountConfigs;
    private final AuditingService auditingService;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImplV1(
            OrderRepository orderRepository,
            OrderDetailsRepository orderDetailsRepository,
            DiscountConfigs discountConfigs, AuditingService auditingService,
            ProductRepository productRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderRepository = orderRepository;
        this.discountConfigs = discountConfigs;
        this.auditingService = auditingService;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public Object placeOrder(OrderDto orderDto) {
        orderDto.isValid();
        Order order = orderRepository.save(buildOrder(orderDto));
        List<OrderDetails> orderDetails = orderDto.getCartItems().stream()
                .map(this::toOrderDetails)
                .map(oDetails -> {
                    oDetails.setOrder(order);
                    auditingService.stamp(oDetails);
                    return oDetails;
                })
                .collect(Collectors.toList());
        orderDetailsRepository.saveAll(orderDetails);
        return toOrderResponseDto(order);
    }

    @Override
    public RecordHolder<List<OrderResponseDto>> getOrders() {
        List<OrderResponseDto> orders = orderRepository.findAll().stream()
                .map(this::toOrderResponseDto)
                .collect(Collectors.toList());
        return new RecordHolder<>(orders.size(), orders);
    }

    @Override
    public OrderResponseDto getOrderById(Integer orderId) {
        return toOrderResponseDto(getOrder(orderId));
    }

    private Order getOrder(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException(
                String.format(ErrorConstants.ENTITY_DOES_NOT_EXISTS, Order.class.getSimpleName(), "id")));
    }

    @Override
    public RecordHolder<List<OrderDetailsDto>> getOrderDetailsForOrder(Integer orderId) {
        List<OrderDetailsDto> orderDetails = orderDetailsRepository.findOrderDetailsByOrder(getOrder(orderId)).stream()
                .map(this::toOrderDetailsDto)
                .map(oDetails -> {
                    oDetails.setOrderId(orderId);
                    return oDetails;
                })
                .collect(Collectors.toList());
        return new RecordHolder<>(orderDetails.size(), orderDetails);
    }

    private OrderDetails toOrderDetails(CartItemDto cartItemDto) {
        Product product = productRepository.findById(cartItemDto.getProduct().getId())
                .orElseThrow(() -> new BadRequestException(String.format(ErrorConstants.ENTITY_DOES_NOT_EXISTS,
                        Product.class.getSimpleName(), "id")));
        return OrderDetails.builder()
                .orderQuantity(cartItemDto.getQuantity())
                .product(product)
                .user(UserContext.getLoggedInUser())
                .build();
    }

    private OrderDetailsDto toOrderDetailsDto(OrderDetails orderDetails) {
        return OrderDetailsDto.builder()
                .orderDate(orderDetails.getCreatedDate().toString())
                .product(toProductDto(orderDetails.getProduct()))
                .quantity(orderDetails.getOrderQuantity())
                .orderedBy(orderDetails.getUser().getId())
                .build();
    }

    private ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .sellingPrice(product.getSellingPrice())
                .id(product.getId())
                .productCategory(product.getProductCategory().toProductCategoryDto())
                .name(product.getName())
                .description(product.getDescription())
                .build();
    }

    private OrderResponseDto toOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .orderedBy(order.getUser().getId())
                .status(order.getStatus().toString())
                .totalCost(order.getTotalCost())
                .discountedCost(order.getDiscountedCost())
                .build();
    }

    private Order buildOrder(OrderDto orderDto) {
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal discountValue = BigDecimal.ZERO;
        for (OrderDetails orderDetailsValue : orderDto.getCartItems().stream()
                .map(this::toOrderDetails).collect(Collectors.toList())) {

            totalCost = totalCost.add(orderDetailsValue.getProduct().getSellingPrice()
                    .multiply(new BigDecimal(orderDetailsValue.getOrderQuantity())));

            BigDecimal productCost = orderDetailsValue.getProduct().getSellingPrice()
                    .multiply(new BigDecimal(orderDetailsValue.getOrderQuantity()));

            if (!orderDetailsValue.getProduct().getProductCategory()
                    .getName().toLowerCase().contains("groceries")) {

                //Is Employee --> Give Discount
                if (UserContext.getLoggedInUser().getEmployee() != null) {
                    discountValue = discountValue.add(productCost
                            .multiply(new BigDecimal((discountConfigs.getEmployeeDiscount() / 100.00)))
                            .setScale(2, RoundingMode.HALF_DOWN));
                }

                //Is Affiliate --> Give Discount
                if (UserContext.getLoggedInUser().getAffiliate() != null) {
                    discountValue = discountValue.add(productCost
                            .multiply(new BigDecimal((discountConfigs.getAffiliateDiscount() / 100.00)))
                            .setScale(2, RoundingMode.HALF_DOWN));
                }

                //Is Loyal Customer(Neither Employee nor Affiliate) --> Give Discount
                if (UserContext.getLoggedInUser().getEmployee() == null
                        && UserContext.getLoggedInUser().getAffiliate() == null
                        && getDurationInYears(UserContext.getLoggedInUser().getCreatedDate(),
                        new Date()) >= discountConfigs.getLoyalCustomerDuration()) {
                    discountValue = discountValue.add(productCost
                            .multiply(new BigDecimal((discountConfigs.getLoyalCustomerDiscount() / 100.00))));
                }

            }
        }

        //Non-percentage-based discount
        discountValue = discountValue.add(totalCost
                .divide(discountConfigs.getPerUnitSalesPrice())
                .setScale(2, RoundingMode.HALF_DOWN)
                .multiply((new BigDecimal(discountConfigs.getPerUnitSalesDiscount()))));

        Order order = Order.builder()
                .totalCost(totalCost)
                .discountedCost(totalCost.subtract(discountValue).setScale(2, RoundingMode.HALF_DOWN))
                .user(UserContext.getLoggedInUser())
                .orderDate(new Date())
                .status(OrderStatus.PENDING)
                .recordStatus(RecordStatus.ACTIVE)
                .build();
        auditingService.stamp(order);
        return order;
    }

    public static int getDurationInYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
}
