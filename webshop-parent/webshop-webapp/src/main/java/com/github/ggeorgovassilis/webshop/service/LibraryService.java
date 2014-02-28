package com.github.ggeorgovassilis.webshop.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.ggeorgovassilis.webshop.model.Author;
import com.github.ggeorgovassilis.webshop.model.Book;
import com.github.ggeorgovassilis.webshop.model.NotFoundException;
import com.github.ggeorgovassilis.webshop.model.Publisher;
import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO;

@Controller
@RequestMapping("/api")
@Transactional
public interface LibraryService {

	@RequestMapping(value = "/authors/{id}", method = RequestMethod.GET)
	@Valid @ResponseBody Author getAuthor(@NotNull @PathVariable("id") Long id);

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/authors", method = RequestMethod.POST)
	@Valid @ResponseBody Author save(@Valid @RequestBody Author author);
	
	@RequestMapping(value = "/publishers/{id}", method = RequestMethod.GET)
	@Valid @ResponseBody Publisher getPublisher(@NotNull @PathVariable("id") Long id);

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/publishers", method = RequestMethod.POST)
	@Valid @ResponseBody Publisher save(@Valid @RequestBody Publisher publisher);

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	@Valid @ResponseBody List<Book> findBooks();

	@RequestMapping(value = "/books/search/{query}", method = RequestMethod.GET)
	@Valid @ResponseBody List<Book> findBooksByQuery(@NotNull @PathVariable("query") String query, @RequestParam(value="page", required=false, defaultValue="0") Integer page);

	@RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
	@Valid @ResponseBody Book getBook(@NotNull @PathVariable("id") Long id);

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/books", method = RequestMethod.POST)
	@Valid @ResponseBody Book save(@Valid @RequestBody Book book);

	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	void reset();

	
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody ValidationErrorsDTO handleValidationErrors(ValidationException e);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody ValidationErrorsDTO handleMethodValidationErrors(MethodArgumentNotValidException e);

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody ValidationErrorsDTO handleNotFound(NotFoundException e);

}
