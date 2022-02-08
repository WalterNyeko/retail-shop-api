package com.assignment.entity.users;

import com.assignment.entity.commons.Auditable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "employee")
public class Employee extends Auditable {

    private String employeeNumber;
    private String lineManager;
    private Date employmentDate;
    private Date contractExpiryDate;
    private User user;

    @Column(name = "employee_number")
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    @Column(name = "line_manager")
    public String getLineManager() {
        return lineManager;
    }

    @Column(name = "employment_date")
    public Date getEmploymentDate() {
        return employmentDate;
    }

    @Column(name = "contract_expiry_date")
    public Date getContractExpiryDate() {
        return contractExpiryDate;
    }

    @OneToOne(mappedBy = "employee")
    public User getUser() {
        return user;
    }
}
