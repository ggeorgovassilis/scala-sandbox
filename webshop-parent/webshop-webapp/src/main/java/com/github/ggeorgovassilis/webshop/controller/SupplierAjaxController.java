package com.github.ggeorgovassilis.webshop.controller;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.ggeorgovassilis.webshop.dto.AnimalDTO;
import com.github.ggeorgovassilis.webshop.dto.HerdDTO;
import com.github.ggeorgovassilis.webshop.dto.OrderDTO;
import com.github.ggeorgovassilis.webshop.dto.ReceiptDTO;
import com.github.ggeorgovassilis.webshop.dto.StockDTO;
import com.github.ggeorgovassilis.webshop.dto.ValidationErrorsDTO;
import com.github.ggeorgovassilis.webshop.service.SupplierService;

/**
 * Supplier web service. It can query stock, the herd status, update the herd
 * and place orders
 * 
 * @author George Georgovassilis
 * 
 */
@Controller
@RequestMapping("/api")
public class SupplierAjaxController {

	@Autowired
	protected SupplierService service;

	@RequestMapping(value = "/stock/{daysFromNow}", method = RequestMethod.GET)
	public @ResponseBody
	StockDTO getStock(@PathVariable int daysFromNow) {
		return service.getStock(daysFromNow);
	}

	@RequestMapping(value = "/herd/{daysFromNow}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<HerdDTO> getHerd(@PathVariable int daysFromNow) {
		return service.getHerd(daysFromNow);
	}

	/**
	 * This should be a POST, but we're leaving it a GET to make it easier
	 * accessible with the browser
	 */
	@RequestMapping(value = "/herd/add", method = RequestMethod.GET)
	public @ResponseBody
	AnimalDTO updateAnimal(@RequestParam("name") String name,
			@RequestParam("age") double age,
			@RequestParam("ageLastShorn") double ageLastShorn) {
		return service.updateAnimal(name, age, ageLastShorn);
	}

	@RequestMapping(value = "/order/{daysFromNow}", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<ReceiptDTO> placeOrder(@RequestBody OrderDTO order,
			@PathVariable int daysFromNow) {
		return service.placeOrder(order, daysFromNow);
	}

	@RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<ReceiptDTO> findOrder(@PathVariable String id) {
		return service.findOrder(id);
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ValidationErrorsDTO processValidationError(
			ConstraintViolationException ex) {
		ValidationErrorsDTO errors = new ValidationErrorsDTO();
		for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
			errors.getFieldErrors().put(v.getPropertyPath().toString(),
					v.getMessage());
		}
		return errors;
	}

	@PostConstruct
	public void initialized() {
		service.importHerd();
	}

}
