package com.assignment.entity.orders;

import com.assignment.entity.commons.Auditable;
import com.assignment.entity.products.Product;
import com.assignment.entity.users.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "order_details")
public class OrderDetails extends Auditable {

    private Product product;
    private Order order;
    private User user;
    private Integer orderQuantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    public Product getProduct() {
        return product;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    public Order getOrder() {
        return order;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    @Column(name = "order_quantity")
    public Integer getOrderQuantity() {
        return orderQuantity;
    }
}
