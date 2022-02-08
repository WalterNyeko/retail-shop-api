package com.assignment.entity.users;

import com.assignment.entity.commons.Auditable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "affiliate")
public class Affiliate extends Auditable {

    private String affiliateNumber;
    private BigDecimal affiliateCommission;
    private String lineManager;
    private Date contractStartDate;
    private Date contractEndDate;
    private User user;

    @Column(name = "affiliate_number")
    public String getAffiliateNumber() {
        return affiliateNumber;
    }

    @Column(name = "affiliate_commission")
    public BigDecimal getAffiliateCommission() {
        return affiliateCommission;
    }

    @Column(name = "line_manager")
    public String getLineManager() {
        return lineManager;
    }

    @Column(name = "contract_start_date")
    public Date getContractStartDate() {
        return contractStartDate;
    }

    @Column(name = "contract_end_date")
    public Date getContractEndDate() {
        return contractEndDate;
    }

    @OneToOne(mappedBy = "affiliate")
    public User getUser() {
        return user;
    }
}
