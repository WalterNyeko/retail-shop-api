package com.assignment.dto.products;

import com.assignment.entity.products.Product;
import com.assignment.entity.products.ProductCategory;
import com.assignment.helpers.ErrorConstants;
import com.assignment.helpers.SystemUtil;
import com.assignment.helpers.Validator;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal sellingPrice;
    private ProductCategoryDto productCategory;

    @SneakyThrows
    public void isValid() {
        Validator.validStringLength(name, 2, 100, ErrorConstants.INVALID_CATEGORY_NAME);
        Validator.validStringLength(description, 2, 255, ErrorConstants.INVALID_DESCRIPTION);
        Validator.isGreaterThanZero(sellingPrice, ErrorConstants.INVALID_SELLING_PRICE);
    }

    public Product toProductEntity() { return SystemUtil.copyProperties(this, new Product()); }
}
