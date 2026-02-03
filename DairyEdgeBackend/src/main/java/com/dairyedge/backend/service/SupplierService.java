package com.dairyedge.backend.service;

import java.util.List;

import com.dairyedge.backend.dtos.SupplierDTO;
import com.dairyedge.backend.dtos.SupplierRespDTO;

public interface SupplierService {

	SupplierRespDTO addSupplier(SupplierDTO supplierDTO);

    List<SupplierRespDTO> getAllSuppliers();

    SupplierRespDTO getSupplierById(Long id);

    SupplierRespDTO updateSupplier(Long id, SupplierDTO supplierDTO);

    String deleteSupplier(Long id);
}
