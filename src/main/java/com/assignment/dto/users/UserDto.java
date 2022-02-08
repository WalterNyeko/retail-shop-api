package com.assignment.dto.users;

import com.assignment.entity.users.User;
import com.assignment.helpers.ErrorConstants;
import com.assignment.helpers.SystemUtil;
import com.assignment.helpers.Validator;
import com.assignment.helpers.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String idType;
    private String idNumber;
    private String address;
    private String email;
    private String phoneNumber;
    private String password;
    private AffiliateDto affiliate;
    private EmployeeDto employee;
    private String token;

    @SneakyThrows
    public void isValid() {
        Validator.validEmail(email, ErrorConstants.INVALID_EMAIL);
        Validator.validPassword(password, ErrorConstants.INVALID_PASSWORD);
        Validator.notNull(dateOfBirth, ErrorConstants.INVALID_DATE_OF_BIRTH);
        Validator.validEnum(IdType.class, idType, ErrorConstants.INVALID_ID_TYPE);
        Validator.notNull(idNumber, ErrorConstants.INVALID_ID_VALUE);
        Validator.validEmployeeAndAffiliateDetails(employee, affiliate,
                ErrorConstants.INVALID_EMPLOYEE_AFFILIATE_INFO);
    }
    public User toUserEntity() { return SystemUtil.copyProperties(this, new User()); }
}
