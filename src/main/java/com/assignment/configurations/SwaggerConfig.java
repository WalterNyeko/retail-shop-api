package com.assignment.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("retail-store-api")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Retail Store API")
                .description("Take Home Technical Challenge")
                .termsOfServiceUrl("https://walternyeko.github.io")
                .contact(new Contact("Nyeko Walter", "https://walternyeko.github.io", "nyekoWalter69@gmail.com"))
                .license("Apache License")
                .licenseUrl("https://walternyeko.github.io")
                .version("1.0").build();
    }
}
