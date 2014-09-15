package com.github.ggeorgovassilis.webshop.service.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.ggeorgovassilis.webshop.model.Book;

public interface BookDao extends JpaRepository<Book, Long>{

	@Query("select b from Book b order by b.title")
	List<Book> findAllOrdered();
	
	@Query("select b from Book b where b.title like %:title% order by b.title")
	Page<Book> findBySimilarTitle(@Param("title") String title, Pageable page);
}
