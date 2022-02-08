package com.assignment.configurations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "app.discount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountConfigs {
    private Integer employeeDiscount;
    private Integer affiliateDiscount;
    private Integer loyalCustomerDiscount;
    private Integer loyalCustomerDuration;
    private BigDecimal perUnitSalesPrice;
    private Integer perUnitSalesDiscount;
}
