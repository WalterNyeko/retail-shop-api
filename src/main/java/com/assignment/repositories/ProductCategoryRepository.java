package com.assignment.repositories;

import com.assignment.entity.products.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository  extends JpaRepository<ProductCategory, Integer> {
}
