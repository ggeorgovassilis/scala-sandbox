package com.github.ggeorgovassilis.webshop.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.github.ggeorgovassilis.webshop.model.Author;
import com.github.ggeorgovassilis.webshop.model.Book;
import com.github.ggeorgovassilis.webshop.model.LoanedBook;
import com.github.ggeorgovassilis.webshop.model.NotFoundException;
import com.github.ggeorgovassilis.webshop.model.Publisher;
import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO;
import com.github.ggeorgovassilis.webshop.service.LibraryService;
import com.github.ggeorgovassilis.webshop.service.dao.AuthorDao;
import com.github.ggeorgovassilis.webshop.service.dao.BookDao;
import com.github.ggeorgovassilis.webshop.service.dao.LoanDao;
import com.github.ggeorgovassilis.webshop.service.dao.PublisherDao;

@Service
public class StockInitializationService{

	@Resource
	LibraryService libraryService;
	
	private String pad(int number){
		String s = ""+number;
		if (number<100)
			s = "0"+s;
		if (number<10)
			s = "0"+s;
		s+=s;
		return s;
	}
	
	@PostConstruct
	@Transactional
	public void init(){
		Author a = new Author();
		a.setName("Test Author");
		a = libraryService.save(a);
		
		Publisher p = new Publisher();
		p.setName("Test Publisher");
		p = libraryService.save(p);
		
		Author a2 = new Author();
		a2.setName("Test Author 2");
		a2 = libraryService.save(a2);

		Book b = new Book();
		b.getAuthors().add(a);
		b.setPublisher(p);
		b.setTitle("Test Title 1");
		b.setPublicationYear(2014);
		libraryService.save(b);

		b = new Book();
		b.getAuthors().add(a);
		b.getAuthors().add(a2);
		b.setPublisher(p);
		b.setTitle("Test Title 2");
		b.setPublicationYear(1988);
		libraryService.save(b);
		
		for (int i=2;i<100;i++){
			b = new Book();
			b.getAuthors().add(a);
			b.setTitle("book "+i);
			b.setPublisher(p);
			b.setPublicationYear(1900+i);
			b.setAvailability((i%4));
			b.setIsbn("123456"+pad(i)+"X");
			libraryService.save(b);
		}
	}

}
