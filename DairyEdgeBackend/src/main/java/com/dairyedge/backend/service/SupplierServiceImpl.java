package com.dairyedge.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyedge.backend.custom_exception.ResourceNotFoundException;
import com.dairyedge.backend.dtos.SupplierDTO;
import com.dairyedge.backend.dtos.SupplierRespDTO;
import com.dairyedge.backend.entities.Supplier;
import com.dairyedge.backend.repositories.SupplierRepository;

import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public SupplierRespDTO addSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);
        supplier.setIsActive(true);

        Supplier saved = supplierRepository.save(supplier);
        return modelMapper.map(saved, SupplierRespDTO.class);
    }

    @Override
    public List<SupplierRespDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(supplier -> modelMapper.map(supplier, SupplierRespDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SupplierRespDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        return modelMapper.map(supplier, SupplierRespDTO.class);
    }

    @Override
    public SupplierRespDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        modelMapper.map(supplierDTO, existing);

        Supplier updated = supplierRepository.save(existing);
        return modelMapper.map(updated, SupplierRespDTO.class);
    }

    @Override
    public String deleteSupplier(Long id) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found with id: " + id));

        supplierRepository.delete(existing);
        return "Supplier deleted successfully";
    }
}
