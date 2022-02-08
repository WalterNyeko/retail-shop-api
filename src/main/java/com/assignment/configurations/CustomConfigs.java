package com.assignment.configurations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.auth")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomConfigs {
    private Integer jwtExpiryTime;
}