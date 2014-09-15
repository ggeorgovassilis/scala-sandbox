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
import org.springframework.stereotype.Controller;
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

@Controller
public class LibraryServiceImpl implements LibraryService {

	@Resource
	protected BookDao bookDao;
	@Resource
	protected AuthorDao authorDao;
	@Resource
	protected PublisherDao publisherDao;
	@Resource
	protected LoanDao loanDao;
	
	protected void notFound(String message) throws NotFoundException{
		throw new NotFoundException(message);
	}
	
	protected <T> T ifNotFound(T o, String message) throws NotFoundException{
		if (o == null)
			notFound(message);
		if ((o instanceof Collection)&&((Collection<?>)o).isEmpty())
			notFound(message);
		return o;
	}
	
	@Override
	public void reset() {
		loanDao.deleteAll();
		bookDao.deleteAll();
		authorDao.deleteAll();
		publisherDao.deleteAll();
	}

	@Override
	public Book save(Book book) {
		return bookDao.saveAndFlush(book);
	}

	@Override
	public Author save(Author author) {
		return authorDao.saveAndFlush(author);
	}

	@Override
	public Publisher save(Publisher publisher) {
		return publisherDao.saveAndFlush(publisher);
	}

	@Override
	public List<Book> findBooks() {
		return ifNotFound(bookDao.findAllOrdered(),"There are not any books registered yet");
	}

	@Override
	public Book getBook(Long id) {
		return ifNotFound(bookDao.findOne(id),"Book with id "+id+" not found");
	}

	
	@Override
	public Author getAuthor(Long id) {
		return ifNotFound(authorDao.findOne(id), "Author with id "+id+" not found");
	}

	@Override
	public Publisher getPublisher(Long id) {
		return ifNotFound(publisherDao.findOne(id), "Publisher with id "+id+" not found");
	}

	@Override
	public List<Book> findBooksByQuery(String query, Integer page) {
		if (page!=null && page.intValue()<0)
			notFound("Can't paginate below 0");
		Page<Book> result = bookDao.findBySimilarTitle(query, new PageRequest(page, 10));
		return ifNotFound(result.getContent(),"No books found that match this query");
	}
	
	
	@Override
	public LoanedBook borrowBook(Long bookId, String clientName) {
		Book book = bookDao.findOne(bookId);
		ifNotFound(book, "This book doesn't exist");
		ifNotFound(book.getAvailability()<1?null:book, "This book isn't in stock right now");
		LoanedBook loan = new LoanedBook();
		loan.setBook(book);
		loan.setDateLoaned(new Date());
		loan.setClientName(clientName);
		book.setAvailability(book.getAvailability()-1);
		bookDao.saveAndFlush(book);
		return loanDao.saveAndFlush(loan);
	}

	@Override
	public ValidationErrorsDTO handleValidationErrors(ValidationException e) {
		ValidationErrorsDTO errors = new ValidationErrorsDTO();
		errors.setMessage("There were validation errors");
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
		errors.setMessage("There were validation errors");
		for (FieldError fe : e.getBindingResult().getFieldErrors()) {
			errors.getFieldErrors().put(fe.getField(), fe.getDefaultMessage());
		}
		return errors;
	}

	@Override
	public ValidationErrorsDTO handleNotFound(NotFoundException e) {
		ValidationErrorsDTO errors = new ValidationErrorsDTO();
		errors.setMessage(e.getMessage());
		return errors;
	}

}
