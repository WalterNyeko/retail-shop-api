package com.assignment.entity.products;

import com.assignment.dto.products.ProductCategoryDto;
import com.assignment.entity.commons.Auditable;
import com.assignment.helpers.SystemUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "product_category")
public class ProductCategory extends Auditable {
    private String name;
    private String description;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public ProductCategoryDto toProductCategoryDto() {
        return SystemUtil.copyProperties(this, new ProductCategoryDto());
    }
}
