package com.assignment.controllers;

import com.assignment.dto.orders.OrderDto;
import com.assignment.dto.orders.OrderResponseDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class OrdersControllerTest extends TestHelpers{


    @Autowired
    TestRestTemplate restTemplate;


    @Test
    public void shouldPlaceOrderAndComputeDiscountCorrectlyForNewCustomer() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ordinaryUserDto.getToken());
        HttpEntity<OrderDto> entity = new HttpEntity<>(orderDto, headers);

        //When
        ResponseEntity<OrderResponseDto> responseEntity = restTemplate.postForEntity("/v1/orders", entity, OrderResponseDto.class);

        //Then
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assert.assertEquals(
                new BigDecimal(3800.00).setScale(2, RoundingMode.HALF_DOWN),
                responseEntity.getBody().getDiscountedCost()
        );
    }

    @Test
    public void shouldPlaceOrderAndComputeDiscountCorrectlyForAffiliateCustomer() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + affiliateUserDto.getToken());
        HttpEntity<OrderDto> entity = new HttpEntity<>(orderDto, headers);

        //When
        ResponseEntity<OrderResponseDto> responseEntity = restTemplate.postForEntity("/v1/orders", entity, OrderResponseDto.class);

        //Then
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assert.assertEquals(
                new BigDecimal(3400.00).setScale(2, RoundingMode.HALF_DOWN),
                responseEntity.getBody().getDiscountedCost()
        );
    }

    @Test
    public void shouldPlaceOrderAndComputeDiscountCorrectlyForEmployeeCustomer() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + employeeUserDto.getToken());
        HttpEntity<OrderDto> entity = new HttpEntity<>(orderDto, headers);

        //When
        ResponseEntity<OrderResponseDto> responseEntity = restTemplate.postForEntity("/v1/orders", entity, OrderResponseDto.class);

        //Then
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assert.assertEquals(
                new BigDecimal(2600.00).setScale(2, RoundingMode.HALF_DOWN),
                responseEntity.getBody().getDiscountedCost()
        );
    }
}
