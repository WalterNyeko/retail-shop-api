package com.assignment.service.orders;

import com.assignment.dto.orders.OrderDetailsDto;
import com.assignment.dto.orders.OrderDto;
import com.assignment.dto.orders.OrderResponseDto;
import com.assignment.helpers.RecordHolder;

import java.util.List;

public interface IOrderService {
    Object placeOrder(OrderDto orderDto);
    RecordHolder<List<OrderResponseDto>> getOrders();
    OrderResponseDto getOrderById(Integer orderId);
    RecordHolder<List<OrderDetailsDto>> getOrderDetailsForOrder(Integer orderId);
}
