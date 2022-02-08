package com.assignment.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {
    private String employeeNumber;
    private String lineManager;
    private String employmentDate;
    private String contractExpiryDate;
}
