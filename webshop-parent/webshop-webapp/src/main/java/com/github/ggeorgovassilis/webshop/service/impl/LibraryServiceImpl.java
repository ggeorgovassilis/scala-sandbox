package com.github.ggeorgovassilis.webshop.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.ValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.github.ggeorgovassilis.webshop.model.Author;
import com.github.ggeorgovassilis.webshop.model.Book;
import com.github.ggeorgovassilis.webshop.model.Publisher;
import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO;
import com.github.ggeorgovassilis.webshop.service.LibraryService;
import com.github.ggeorgovassilis.webshop.service.dao.AuthorDao;
import com.github.ggeorgovassilis.webshop.service.dao.BookDao;
import com.github.ggeorgovassilis.webshop.service.dao.PublisherDao;

@Service
@Controller
@Transactional
@Validated
public class LibraryServiceImpl implements LibraryService {

	@Resource
	protected BookDao bookDao;
	@Resource
	protected AuthorDao authorDao;
	@Resource
	protected PublisherDao publisherDao;

	@Override
	public Book save(Book book) {
		return bookDao.saveAndFlush(book);
	}

	@Override
	public Author save(@Valid Author author) {
		return authorDao.saveAndFlush(author);
	}

	@Override
	public Publisher save(@Valid Publisher publisher) {
		return publisherDao.saveAndFlush(publisher);
	}

	@Override
	public List<Book> findBooks() {
		return bookDao.findAll();
	}

	@Override
	public Book getBook(Long id) {
		return bookDao.findOne(id);
	}

	@Override
	public ValidationErrorsDTO handleValidationErrors(ValidationException e) {
		ValidationErrorsDTO errors = new ValidationErrorsDTO();
		if (e instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) e;
			for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
				errors.getFieldErrors().put(cv.getPropertyPath().toString(),
						cv.getMessage());
			}
		}
		return errors;
	}

	@Override
	public ValidationErrorsDTO handleMethodValidationErrors(
			MethodArgumentNotValidException e) {
		ValidationErrorsDTO errors = new ValidationErrorsDTO();
		for (FieldError fe : e.getBindingResult().getFieldErrors()) {
			errors.getFieldErrors().put(fe.getField(), fe.getDefaultMessage());
		}
		return errors;
	}

}
