package com.assignment.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AffiliateDto {
    private String affiliateNumber;
    private BigDecimal affiliateCommission;
    private String lineManager;
    private String contractStartDate;
    private String contractEndDate;
}
