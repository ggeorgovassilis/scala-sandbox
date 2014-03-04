package com.github.ggeorgovassilis.webshop.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ggeorgovassilis.webshop.model.LoanedBook;

@Repository
public interface LoanDao extends JpaRepository<LoanedBook, Long>{
}
