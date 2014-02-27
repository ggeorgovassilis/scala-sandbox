package com.github.ggeorgovassilis.webshop.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.ggeorgovassilis.webshop.model.Book;

public interface BookDao extends JpaRepository<Book, Long>{

	@Query("select b from Book b order by b.title")
	List<Book> findAllOrdered();
}
