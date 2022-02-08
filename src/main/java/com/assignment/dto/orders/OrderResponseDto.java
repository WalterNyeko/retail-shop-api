package com.assignment.dto.orders;

import com.assignment.dto.users.UserDto;
import com.assignment.entity.users.User;
import com.assignment.helpers.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private Integer id;
    private String status;
    private BigDecimal totalCost;
    private BigDecimal discountedCost;
    private Date orderDate;
    private Integer orderedBy;
}
