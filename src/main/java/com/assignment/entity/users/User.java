package com.assignment.entity.users;

import com.assignment.dto.users.UserDto;
import com.assignment.entity.commons.Auditable;
import com.assignment.entity.users.Affiliate;
import com.assignment.entity.users.Employee;
import com.assignment.helpers.SystemUtil;
import com.assignment.helpers.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Setter
@Entity(name = "user")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Auditable {

    private String username;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private IdType idType;
    private String idNumber;
    private String address;
    private String email;
    private String phoneNumber;
    private String password;
    private Affiliate affiliate;
    private Employee employee;

    public UserDto toUserDto() {
        return SystemUtil.copyProperties(this, new UserDto());
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @Column(name = "date_of_birth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Column(name = "id_type")
    @Enumerated(EnumType.STRING)
    public IdType getIdType() {
        return idType;
    }

    @Column(name = "id_number")
    public String getIdNumber() {
        return idNumber;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_affiliate",
            joinColumns =
                    { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "affiliate_id", referencedColumnName = "id") })
    public Affiliate getAffiliate() { return affiliate; }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_employee",
            joinColumns =
                    { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "employee_id", referencedColumnName = "id") })
    public Employee getEmployee() { return employee; }

}
