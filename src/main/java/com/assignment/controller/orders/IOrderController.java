package com.assignment.controller.orders;

import com.assignment.dto.orders.OrderDetailsDto;
import com.assignment.dto.orders.OrderDto;
import com.assignment.dto.orders.OrderResponseDto;
import com.assignment.helpers.RecordHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IOrderController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Object placeOrder(@RequestBody OrderDto orderDto);

    @GetMapping
    RecordHolder<List<OrderResponseDto>> getOrders();

    @GetMapping("/{orderId}")
    OrderResponseDto getOrderById(@PathVariable Integer orderId);

    @GetMapping("/{orderId}/details")
    RecordHolder<List<OrderDetailsDto>> getOrderDetailsForOrder(@PathVariable Integer orderId);
}
