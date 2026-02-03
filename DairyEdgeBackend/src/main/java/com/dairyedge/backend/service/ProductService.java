package com.dairyedge.backend.service;

import java.util.List;

import com.dairyedge.backend.dtos.ProductDTO;
import com.dairyedge.backend.dtos.ProductRespDTO;

public interface ProductService {

    List<ProductRespDTO> getAllProducts();

    ProductRespDTO getProductById(Long id);

    ProductRespDTO addProduct(ProductDTO  product);

    ProductRespDTO updateProduct(Long id, ProductDTO  product);

    String deleteProduct(Long id);
}
