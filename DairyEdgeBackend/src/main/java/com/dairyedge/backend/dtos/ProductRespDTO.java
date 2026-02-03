package com.dairyedge.backend.dtos;

import com.dairyedge.backend.entities.ProductCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRespDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private ProductCategory category;
    private Long supplierId;
    private String supplierName;
    private String imageUrl;
}
