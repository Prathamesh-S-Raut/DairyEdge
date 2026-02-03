package com.dairyedge.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairyedge.backend.entities.Supplier;


public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}
