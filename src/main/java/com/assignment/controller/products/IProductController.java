package com.assignment.controller.products;

import com.assignment.dto.products.ProductCategoryDto;
import com.assignment.dto.products.ProductDto;
import com.assignment.helpers.ActionResponse;
import com.assignment.helpers.RecordHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IProductController {
    /**
     * Creates a new category
     * @param productCategoryDto
     * @return
     */
    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    ActionResponse createProductCategory(
            @RequestBody ProductCategoryDto productCategoryDto
    );

    /**
     * Updates details of existing category
     * @param productCategoryDto
     * @param id
     * @return
     */
    @PutMapping("/category/{id}")
    @ResponseStatus(HttpStatus.OK)
    ActionResponse editProductCategory(
            @RequestBody ProductCategoryDto productCategoryDto,
            @PathVariable Integer id
    );

    /**
     * Deletes an existing category
     * @param id
     * @return
     */
    @DeleteMapping("/category/{id}")
    @ResponseStatus(HttpStatus.OK)
    ActionResponse deleteProductCategory(
            @PathVariable Integer id
    );

    /**
     * Retrieves all categories, also filters based on query params
     * @return
     */
    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    RecordHolder<List<ProductCategoryDto>> getProductCategories();


    /**
     * Creates a new product
     * @param productDto
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ActionResponse createProduct(
            @RequestBody ProductDto productDto
    );

    /**
     * Updates details of existing product
     * @param productDto
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ActionResponse editProduct(
            @RequestBody ProductDto productDto,
            @PathVariable Integer id
    );

    /**
     * Deletes an existing product
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ActionResponse deleteProduct(
            @PathVariable Integer id
    );

    /**
     * Retrieves all products, also filters based on query params
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    RecordHolder<List<ProductDto>> getProducts();
}
