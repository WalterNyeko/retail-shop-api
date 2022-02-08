package com.assignment.entity.orders;

import com.assignment.entity.commons.Auditable;
import com.assignment.entity.users.User;
import com.assignment.helpers.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order extends Auditable {

    private OrderStatus status;
    private BigDecimal totalCost;
    private BigDecimal discountedCost;
    private Date orderDate;
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public OrderStatus getStatus() {
        return status;
    }

    @Column(name = "total_cost")
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    @Column(name = "discounted_cost")
    public BigDecimal getDiscountedCost() {
        return discountedCost;
    }

    @Column(name = "order_date")
    public Date getOrderDate() {
        return orderDate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }
}
