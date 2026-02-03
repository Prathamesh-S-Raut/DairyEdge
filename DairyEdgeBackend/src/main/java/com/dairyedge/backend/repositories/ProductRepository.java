package com.dairyedge.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairyedge.backend.entities.Product;
import com.dairyedge.backend.entities.ProductCategory;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByCategory(ProductCategory category);
	
	List<Product> findByIsActiveTrue();
}
