package com.github.ggeorgovassilis.webshop.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.ggeorgovassilis.webshop.model.Supplier;

public interface SupplierDao extends JpaRepository<Supplier, String>{

}
