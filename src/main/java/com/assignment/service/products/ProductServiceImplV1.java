package com.assignment.service.products;

import com.assignment.dto.products.ProductCategoryDto;
import com.assignment.dto.products.ProductDto;
import com.assignment.entity.products.Product;
import com.assignment.entity.products.ProductCategory;
import com.assignment.exceptions.BadRequestException;
import com.assignment.helpers.ActionResponse;
import com.assignment.helpers.ErrorConstants;
import com.assignment.helpers.RecordHolder;
import com.assignment.helpers.SystemUtil;
import com.assignment.repositories.ProductCategoryRepository;
import com.assignment.repositories.ProductRepository;
import com.assignment.service.AuditingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplV1 implements IProductService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final AuditingService auditingService;

    @Autowired
    public ProductServiceImplV1(
            ProductCategoryRepository productCategoryRepository,
            ProductRepository productRepository,
            AuditingService auditingService) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.auditingService = auditingService;
    }

    @Transactional
    @Override
    public ActionResponse createProductCategory(ProductCategoryDto productCategoryDto) {
        productCategoryDto.isValid();
        ProductCategory productCategory = productCategoryDto.toProductCategory();
        auditingService.stamp(productCategory);
        return ActionResponse.builder().resourceId(productCategoryRepository.save(productCategory).getId()).build();
    }

    @Override
    public ActionResponse editProductCategory(ProductCategoryDto productCategoryDto, Integer id) {
        ProductCategory productCategory = getProductCategory(id);
        SystemUtil.copyNonNullProperties(productCategoryDto, productCategory);
        productCategoryRepository.save(productCategory);
        return ActionResponse.builder().resourceId(id).build();
    }

    @Override
    public ActionResponse deleteProductCategory(Integer id) {
        getProductCategory(id);
        productCategoryRepository.deleteById(id);
        return ActionResponse.builder().resourceId(id).build();
    }

    @Override
    public RecordHolder<List<ProductCategoryDto>> getProductCategories() {
        List<ProductCategoryDto> productCategories = productCategoryRepository
                .findAll().stream().map(this::toProductCategoryDto)
                .collect(Collectors.toList());
        return new RecordHolder(productCategories.size(), productCategories);
    }

    @Transactional
    @Override
    public ActionResponse createProduct(ProductDto productDto) {
        productDto.isValid();
        Product product = productDto.toProductEntity();
        ProductCategory productCategory = getProductCategory(productDto.getProductCategory().getId());
        product.setProductCategory(productCategory);
        auditingService.stamp(product);
        return ActionResponse.builder().resourceId(productRepository.save(product).getId()).build();
    }

    @Override
    public ActionResponse editProduct(ProductDto productDto, Integer id) {
        Product product = getProduct(id);
        SystemUtil.copyNonNullProperties(productDto, product);
        productRepository.save(product);
        return ActionResponse.builder().resourceId(id).build();
    }

    @Override
    public ActionResponse deleteProduct(Integer id) {
        getProduct(id);
        productRepository.deleteById(id);
        return ActionResponse.builder().resourceId(id).build();
    }

    @Override
    public RecordHolder<List<ProductDto>> getProducts() {
        List<ProductDto> products = productRepository
                .findAll().stream().map(this::toProductDto)
                .collect(Collectors.toList());
        return new RecordHolder(products.size(), products);
    }

    private Product getProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException(
                String.format(ErrorConstants.ENTITY_DOES_NOT_EXISTS, Product.class.getSimpleName(), "id")));
        return product;
    }

    private ProductCategory getProductCategory(Integer id) {
        ProductCategory productCategory = productCategoryRepository.findById(id).orElseThrow(() -> new BadRequestException(
                String.format(ErrorConstants.ENTITY_DOES_NOT_EXISTS, ProductCategory.class.getSimpleName(), "id")));
        return productCategory;
    }

    private ProductCategoryDto toProductCategoryDto(ProductCategory productCategory) {
        return SystemUtil.copyProperties(productCategory, new ProductCategoryDto());
    }

    private ProductDto toProductDto(Product product) {
        ProductDto productDto = SystemUtil.copyProperties(product, new ProductDto());
        productDto.setProductCategory(ProductCategoryDto.builder()
                .name(product.getProductCategory().getName())
                .description(product.getProductCategory().getDescription())
                .build());
        return productDto;
    }
}
