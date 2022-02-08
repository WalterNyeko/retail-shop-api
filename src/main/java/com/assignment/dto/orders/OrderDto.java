package com.assignment.dto.orders;

import com.assignment.helpers.ErrorConstants;
import com.assignment.helpers.Validator;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private List<CartItemDto> cartItems;

    @SneakyThrows
    public void isValid() {
        Validator.notEmpty(cartItems, ErrorConstants.INVALID_CART_ITEMS);
    }
}
