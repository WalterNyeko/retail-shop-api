package com.assignment.repositories;

import com.assignment.entity.orders.Order;
import com.assignment.entity.orders.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findOrderDetailsByOrder(Order order);
}
