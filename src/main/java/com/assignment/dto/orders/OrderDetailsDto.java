package com.assignment.dto.orders;


import com.assignment.dto.products.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsDto {
    private Integer orderId;
    private ProductDto product;
    private Integer quantity;
    private String orderDate;
    private Integer orderedBy;
}
