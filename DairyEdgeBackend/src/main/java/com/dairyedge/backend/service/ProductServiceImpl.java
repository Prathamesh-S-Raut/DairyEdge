package com.dairyedge.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyedge.backend.custom_exception.ResourceNotFoundException;
import com.dairyedge.backend.dtos.ProductDTO;
import com.dairyedge.backend.dtos.ProductRespDTO;
import com.dairyedge.backend.entities.Product;
import com.dairyedge.backend.entities.Supplier;
import com.dairyedge.backend.repositories.ProductRepository;
import com.dairyedge.backend.repositories.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductRespDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToRespDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductRespDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));
        return mapToRespDTO(product);
    }

    @Override
    public ProductRespDTO addProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);

        // Fetch supplier entity
        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier not found with id: " + productDTO.getSupplierId()));
        product.setSupplier(supplier);
        
        product.setIsActive(true);
        
        switch(productDTO.getCategory()) {
        	case MILK :
        		product.setImageUrl("https://res.cloudinary.com/dlvyaxcdt/image/upload/v1769963451/milk_lvvwf6.png");
        		break;
        	case CURD :
        		product.setImageUrl("https://res.cloudinary.com/dlvyaxcdt/image/upload/v1769963452/curd_f0c4ua.png");
        		break;
        	case BUTTER :
        		product.setImageUrl("https://res.cloudinary.com/dlvyaxcdt/image/upload/v1769963451/butter_hplwsh.png");
        		break;
        	case GHEE :
        		product.setImageUrl("https://res.cloudinary.com/dlvyaxcdt/image/upload/v1769963453/ghee_vmrhlm.png");
        		break;
        	case YOGURT :
        		product.setImageUrl("https://res.cloudinary.com/dlvyaxcdt/image/upload/v1769963457/yogurt_mxxku3.png");
        		break;
        	case CHEESE :
        		product.setImageUrl("https://res.cloudinary.com/dlvyaxcdt/image/upload/v1769963452/cheese_zzac7k.png");
        		break;
        	default :
        		product.setImageUrl(null);;
        	
        }

        Product saved = productRepository.save(product);
        return mapToRespDTO(saved);
    }

    @Override
    public ProductRespDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        // Map updated fields from DTO
        existing.setName(productDTO.getName());
        existing.setDescription(productDTO.getDescription());
        existing.setPrice(productDTO.getPrice());
        existing.setStockQuantity(productDTO.getStockQuantity());
        existing.setCategory(productDTO.getCategory());

        // Update supplier if needed
        if (!existing.getSupplier().getId().equals(productDTO.getSupplierId())) {
            Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Supplier not found with id: " + productDTO.getSupplierId()));
            existing.setSupplier(supplier);
        }

        Product updated = productRepository.save(existing);
        return mapToRespDTO(updated);
    }

    @Override
    public String deleteProduct(Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(existing);
        return "Product deleted successfully";
    }

    // Helper method to map Product -> ProductRespDTO
    private ProductRespDTO mapToRespDTO(Product product) {
        ProductRespDTO resp = modelMapper.map(product, ProductRespDTO.class);
        if (product.getSupplier() != null) {
            resp.setSupplierId(product.getSupplier().getId());
            resp.setSupplierName(product.getSupplier().getName());
        }
        return resp;
    }
}
