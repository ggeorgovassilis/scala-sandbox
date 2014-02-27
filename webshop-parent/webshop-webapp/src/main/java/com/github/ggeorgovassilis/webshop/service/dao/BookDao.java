package com.github.ggeorgovassilis.webshop.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.ggeorgovassilis.webshop.model.Book;

public interface BookDao extends JpaRepository<Book, Long>{

}
