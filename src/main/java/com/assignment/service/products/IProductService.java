package com.assignment.service.products;

import com.assignment.dto.products.ProductCategoryDto;
import com.assignment.dto.products.ProductDto;
import com.assignment.helpers.ActionResponse;
import com.assignment.helpers.RecordHolder;

import java.util.List;

public interface IProductService {

    //Product Categories

    ActionResponse createProductCategory(ProductCategoryDto productCategoryDto);
    ActionResponse editProductCategory(ProductCategoryDto productCategoryDto, Integer id);
    ActionResponse deleteProductCategory(Integer id);
    RecordHolder<List<ProductCategoryDto>> getProductCategories();

    //Products

    ActionResponse createProduct(ProductDto productDto);
    ActionResponse editProduct(ProductDto productDto, Integer id);
    ActionResponse deleteProduct(Integer id);
    RecordHolder<List<ProductDto>> getProducts();
}
