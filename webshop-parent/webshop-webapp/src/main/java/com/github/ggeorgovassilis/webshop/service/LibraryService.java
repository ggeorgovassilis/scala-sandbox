package com.github.ggeorgovassilis.webshop.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.ggeorgovassilis.webshop.model.Author;
import com.github.ggeorgovassilis.webshop.model.Book;
import com.github.ggeorgovassilis.webshop.model.Publisher;
import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO;

@Controller
@RequestMapping("/api")
public interface LibraryService {

	@RequestMapping(value = "/books", method = RequestMethod.POST)
	@Valid @ResponseBody Book save(@Valid @RequestBody Book book);
	
	@RequestMapping(value = "/authors", method = RequestMethod.POST)
	@Valid @ResponseBody Author save(@Valid @RequestBody Author author);
	
	@RequestMapping(value = "/publishers", method = RequestMethod.POST)
	@Valid Publisher save(@Valid @RequestBody Publisher publisher);
	
	@RequestMapping(value = "/books", method = RequestMethod.GET)
	@Valid @ResponseBody List<Book> findBooks();
	
	@RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
	@Valid @ResponseBody Book getBook(@NotNull @PathVariable("id") Long id);
	
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody ValidationErrorsDTO handleValidationErrors(ValidationException e);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody ValidationErrorsDTO handleMethodValidationErrors(MethodArgumentNotValidException e);

}