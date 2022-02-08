package com.assignment.entity.products;

import com.assignment.entity.commons.Auditable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "product")
public class Product extends Auditable {
    private String name;
    private String description;
    private BigDecimal sellingPrice;
    private ProductCategory productCategory;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "selling_price")
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id", nullable=false)
    public ProductCategory getProductCategory() {
        return productCategory;
    }
}
