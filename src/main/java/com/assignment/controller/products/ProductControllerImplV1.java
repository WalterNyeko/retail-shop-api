package com.assignment.controller.products;

import com.assignment.dto.products.ProductCategoryDto;
import com.assignment.dto.products.ProductDto;
import com.assignment.helpers.ActionResponse;
import com.assignment.helpers.RecordHolder;
import com.assignment.service.products.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductControllerImplV1 implements IProductController {

    private final IProductService productService;

    @Autowired
    public ProductControllerImplV1(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public ActionResponse createProductCategory(ProductCategoryDto productCategoryDto) {
        return productService.createProductCategory(productCategoryDto);
    }

    @Override
    public ActionResponse editProductCategory(ProductCategoryDto productCategoryDto, Integer id) {
        return productService.editProductCategory(productCategoryDto, id);
    }

    @Override
    public ActionResponse deleteProductCategory(Integer id) {
        return productService.deleteProductCategory(id);
    }

    @Override
    public RecordHolder<List<ProductCategoryDto>> getProductCategories() {
        return productService.getProductCategories();
    }

    @Override
    public ActionResponse createProduct(ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @Override
    public ActionResponse editProduct(ProductDto productDto, Integer id) {
        return productService.editProduct(productDto, id);
    }

    @Override
    public ActionResponse deleteProduct(Integer id) {
        return productService.deleteProduct(id);
    }

    @Override
    public RecordHolder<List<ProductDto>> getProducts() {
        return productService.getProducts();
    }
}
