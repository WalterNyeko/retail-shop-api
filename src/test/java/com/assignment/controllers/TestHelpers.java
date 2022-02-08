package com.assignment.controllers;

import com.assignment.dto.orders.CartItemDto;
import com.assignment.dto.orders.OrderDto;
import com.assignment.dto.products.ProductDto;
import com.assignment.dto.users.AffiliateDto;
import com.assignment.dto.users.EmployeeDto;
import com.assignment.dto.users.LoginDto;
import com.assignment.dto.users.UserDto;
import com.assignment.entity.products.Product;
import com.assignment.entity.products.ProductCategory;
import com.assignment.entity.users.Affiliate;
import com.assignment.helpers.enums.IdType;
import com.assignment.repositories.OrderDetailsRepository;
import com.assignment.repositories.OrderRepository;
import com.assignment.repositories.ProductCategoryRepository;
import com.assignment.repositories.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class TestHelpers {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TestRestTemplate restTemplate;

    public static OrderDto orderDto = new OrderDto();
    public static UserDto ordinaryUserDto = new UserDto();
    public static UserDto affiliateUserDto = new UserDto();
    public static UserDto employeeUserDto = new UserDto();

    @Before
    public void setUp() {
        //Sign Up New Customer
        ordinaryUserDto = UserDto.builder()
                .username("john")
                .address("address")
                .dateOfBirth("22/02/1990")
                .email("email@gmail.com")
                .firstName("John")
                .lastName("Smith")
                .idNumber("Test123")
                .idType(IdType.NATIONAL_ID.toString())
                .phoneNumber("0785255051")
                .password("JohnSmith@123!")
                .build();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDto> entity = new HttpEntity<>(ordinaryUserDto, headers);
        restTemplate.postForEntity("/v1/users/signup", entity, UserDto.class);

        //Sign Up Affiliate Customer
        AffiliateDto affiliate = AffiliateDto.builder()
                .affiliateCommission(new BigDecimal(0.5))
                .affiliateNumber("123GH")
                .lineManager("Onen Junior")
                .contractStartDate("22/01/2022")
                .contractEndDate("22/01/2024")
                .build();

        affiliateUserDto = UserDto.builder()
                .username("jane")
                .address("address")
                .dateOfBirth("22/02/1990")
                .email("jane@gmail.com")
                .firstName("Jane")
                .lastName("Doe")
                .idNumber("Test123")
                .idType(IdType.NATIONAL_ID.toString())
                .phoneNumber("0785255051")
                .password("JaneDoe@123!")
                .affiliate(affiliate)
                .build();

        HttpEntity<UserDto> entityJane = new HttpEntity<>(affiliateUserDto, headers);
        restTemplate.postForEntity("/v1/users/signup", entityJane, UserDto.class);

        //Sign Up Employee Customer
        EmployeeDto employee = EmployeeDto.builder()
                .employeeNumber("123GH")
                .lineManager("Onen Junior")
                .employmentDate("22/01/2022")
                .contractExpiryDate("22/01/2024")
                .build();

        employeeUserDto = UserDto.builder()
                .username("johnny")
                .address("address")
                .dateOfBirth("22/02/1990")
                .email("johnny@gmail.com")
                .firstName("Johnny")
                .lastName("Doe")
                .idNumber("Test123")
                .idType(IdType.NATIONAL_ID.toString())
                .phoneNumber("0785255051")
                .password("JohnnyDoe@123!")
                .employee(employee)
                .build();

        HttpEntity<UserDto> entityJohnny = new HttpEntity<>(employeeUserDto, headers);
        restTemplate.postForEntity("/v1/users/signup", entityJohnny, UserDto.class);

        //Login New User
        LoginDto loginDto = LoginDto.builder()
                .username("john")
                .password("JohnSmith@123!")
                .build();
        HttpEntity<LoginDto> loginEntity = new HttpEntity<>(loginDto, headers);
        ResponseEntity<UserDto> loginResponse = restTemplate.postForEntity("/v1/users/signin", loginEntity, UserDto.class);
        ordinaryUserDto.setToken(loginResponse.getBody().getToken());

        //Login Affiliate User
        LoginDto loginDtoJane = LoginDto.builder()
                .username("jane")
                .password("JaneDoe@123!")
                .build();
        HttpEntity<LoginDto> loginEntityJane = new HttpEntity<>(loginDtoJane, headers);
        ResponseEntity<UserDto> loginResponseJane = restTemplate.postForEntity("/v1/users/signin", loginEntityJane, UserDto.class);
        affiliateUserDto.setToken(loginResponseJane.getBody().getToken());

        //Login Employee User
        LoginDto loginDtoJohnny = LoginDto.builder()
                .username("johnny")
                .password("JohnnyDoe@123!")
                .build();
        HttpEntity<LoginDto> loginEntityJohnny = new HttpEntity<>(loginDtoJohnny, headers);
        ResponseEntity<UserDto> loginResponseJohnny = restTemplate.postForEntity("/v1/users/signin", loginEntityJohnny, UserDto.class);
        employeeUserDto.setToken(loginResponseJohnny.getBody().getToken());

        //Setup Product Category
        ProductCategory productCategory = ProductCategory.builder()
                .name("Electronics")
                .description("Electronics and electrical appliances")
                .build();
        productCategory = productCategoryRepository.save(productCategory);

        //Setup Product
        Product product = Product.builder()
                .productCategory(productCategory)
                .sellingPrice(new BigDecimal(2000.00))
                .name("Home theatre")
                .description("Music to the heart")
                .build();
        product = productRepository.save(product);

        //Setup Order
        List<CartItemDto> cartItems = Arrays.asList(
                CartItemDto.builder()
                        .quantity(2)
                        .product(ProductDto.builder().id(product.getId()).build())
                        .build());

        orderDto.setCartItems(cartItems);
    }

    @Test
    public void contextLoad() {
    }
}
