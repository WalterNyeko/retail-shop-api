package com.assignment.dto.products;

import com.assignment.entity.products.ProductCategory;
import com.assignment.helpers.ErrorConstants;
import com.assignment.helpers.SystemUtil;
import com.assignment.helpers.Validator;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCategoryDto {
    private Integer id;
    private String name;
    private String description;

    @SneakyThrows
    public void isValid() {
        Validator.validStringLength(name, 2, 100, ErrorConstants.INVALID_CATEGORY_NAME);
        Validator.validStringLength(description, 2, 255, ErrorConstants.INVALID_DESCRIPTION);
    }

    public ProductCategory toProductCategory() {
        return SystemUtil.copyProperties(this, new ProductCategory());
    }
}
