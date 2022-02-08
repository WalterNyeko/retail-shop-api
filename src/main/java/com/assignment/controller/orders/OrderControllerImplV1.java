package com.assignment.controller.orders;

import com.assignment.dto.orders.OrderDetailsDto;
import com.assignment.dto.orders.OrderDto;
import com.assignment.dto.orders.OrderResponseDto;
import com.assignment.helpers.RecordHolder;
import com.assignment.service.orders.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
public class OrderControllerImplV1 implements IOrderController {

    private final IOrderService orderService;

    @Autowired
    public OrderControllerImplV1(IOrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public Object placeOrder(OrderDto orderDto) {
        return orderService.placeOrder(orderDto);
    }

    @Override
    public RecordHolder<List<OrderResponseDto>> getOrders() {
        return orderService.getOrders();
    }

    @Override
    public OrderResponseDto getOrderById(Integer orderId) {
        return orderService.getOrderById(orderId);
    }

    @Override
    public RecordHolder<List<OrderDetailsDto>> getOrderDetailsForOrder(Integer orderId) {
        return orderService.getOrderDetailsForOrder(orderId);
    }
}
