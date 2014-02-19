package com.github.ggeorgovassilis.webshop.service.impl;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.ggeorgovassilis.webshop.dao.AnimalDao;
import com.github.ggeorgovassilis.webshop.dao.HerdDao;
import com.github.ggeorgovassilis.webshop.dao.OrderDao;
import com.github.ggeorgovassilis.webshop.dto.AnimalDTO;
import com.github.ggeorgovassilis.webshop.dto.HerdDTO;
import com.github.ggeorgovassilis.webshop.dto.OrderDTO;
import com.github.ggeorgovassilis.webshop.dto.ReceiptDTO;
import com.github.ggeorgovassilis.webshop.dto.StockDTO;
import com.github.ggeorgovassilis.webshop.dto.ValidationErrorsDTO;
import com.github.ggeorgovassilis.webshop.model.Animal;
import com.github.ggeorgovassilis.webshop.model.Herd;
import com.github.ggeorgovassilis.webshop.model.Order;
import com.github.ggeorgovassilis.webshop.model.ProductionLogic;
import com.github.ggeorgovassilis.webshop.service.SupplierService;

/**
 * Supplier web service. It can query stock, the herd status, update the herd
 * and place orders
 * 
 * @author George Georgovassilis
 * 
 */
@Controller("SupplierServiceImpl")
@Transactional
@RequestMapping("/api")
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	protected ProductionLogic production;

	@Autowired
	protected HerdDao herdDao;

	@Autowired
	protected Validator validator;

	@Autowired
	protected AnimalDao animalDao;

	@Autowired
	protected OrderDao orderDao;

	protected void validate(Object o) {
		Set<ConstraintViolation<Object>> result = validator.validate(o);
		if (!result.isEmpty()) {
			throw new ConstraintViolationException(result);
		}
	}

	@Override
	@RequestMapping(value = "/stock/{daysFromNow}", method = RequestMethod.GET)
	public @ResponseBody
	StockDTO getStock(@PathVariable int daysFromNow) {
		Herd herd = new Herd();
		herd.setAnimals(animalDao.findAll());
		StockDTO stock = new StockDTO();
		stock.setMilk(production.getMilkOutputAtDate(herd, daysFromNow));
		stock.setSkins(production.getWoolOutputAtDate(herd, daysFromNow));
		return stock;
	}

	protected ReceiptDTO toReceipt(Order order) {
		ReceiptDTO receipt = new ReceiptDTO();
		if (order != null) {
			receipt.setDay(order.getDate());
			receipt.setId(order.getId());
			receipt.setMilk(order.getMilk());
			receipt.setSkins(order.getWool());
			receipt.setCustomerName(order.getCustomerName());
		}
		return receipt;
	}

	protected AnimalDTO toDto(Animal animal) {
		AnimalDTO dto = new AnimalDTO();
		dto.setAge(production.years(animal.getAge()));
		dto.setAgeLastShaved(production.years(animal.getAgeLastShaved()));
		dto.setName(animal.getName());
		return dto;
	}

	@Override
	@RequestMapping(value = "/herd/{daysFromNow}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<HerdDTO> getHerd(@PathVariable int daysFromNow) {
		HerdDTO herdDTO = null;
		HttpStatus status = HttpStatus.NOT_FOUND;
		if (daysFromNow >= 0) {
			status = HttpStatus.OK;
			herdDTO = new HerdDTO();
			for (Animal animal : animalDao.findAll()) {
				AnimalDTO animalDTO = toDto(animal);
				animalDTO.setAge(production.getAnimalAgeInYearsOnDay(animal,
						daysFromNow));
				herdDTO.getAnimals().add(animalDTO);
			}
		}
		return new ResponseEntity<HerdDTO>(herdDTO, status);
	}

	/**
	 * This should be a POST, but we're leaving it a GET to make it easier
	 * accessible with the browser
	 */
	@Override
	@RequestMapping(value = "/herd/add", method = RequestMethod.GET)
	public @ResponseBody
	AnimalDTO updateAnimal(@RequestParam("name") String name,
			@RequestParam("age") double age,
			@RequestParam("ageLastShorn") double ageLastShorn) {
		Animal animal = animalDao.findOne(name);
		if (animal == null) {
			animal = new Animal();
			animal.setName(name);
		}
		animal.setAge(production.days(age));
		animal.setAgeLastShaved(production.days(ageLastShorn));
		validate(animal);
		animal = animalDao.save(animal);
		return toDto(animal);
	}

	@Override
	@RequestMapping(value = "/order/{daysFromNow}", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<ReceiptDTO> placeOrder(@RequestBody @Valid OrderDTO order,
			@PathVariable int daysFromNow) {
		HttpStatus statusCode = HttpStatus.CREATED;
		Order persistedOrder = null;
		ReceiptDTO receipt = null;
		StockDTO stock = null;
		order.setDay(daysFromNow);
		validate(order);
		stock = getStock(daysFromNow);

		stock.setSkins(stock.getSkins() >= order.getOrder().getSkins() ? order
				.getOrder().getSkins() : 0);
		stock.setMilk(stock.getMilk() >= order.getOrder().getMilk() ? order
				.getOrder().getMilk() : 0);
		if (stock.getSkins() != order.getOrder().getSkins()
				|| stock.getMilk() != order.getOrder().getMilk())
			statusCode = HttpStatus.PARTIAL_CONTENT;
		if (stock.getSkins() == 0 && stock.getMilk() == 0)
			statusCode = HttpStatus.NOT_FOUND;
		else {
			persistedOrder = new Order();
			persistedOrder.setDate(daysFromNow);
			persistedOrder.setMilk(stock.getMilk());
			persistedOrder.setWool(stock.getSkins());
			persistedOrder.setCustomerName(order.getCustomer());
			validate(persistedOrder);
			persistedOrder = orderDao.saveAndFlush(persistedOrder);
		}
		receipt = toReceipt(persistedOrder);
		return new ResponseEntity<>(receipt, statusCode);
	}

	@PostConstruct
	public void initialized() {
		Herd herd = herdDao.find("classpath:customization/herd.xml");
		animalDao.save(herd.getAnimals());
	}

	@Override
	@RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<ReceiptDTO> findOrder(@PathVariable String id) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ReceiptDTO receipt = new ReceiptDTO();

		Order order = orderDao.findOne(id);
		if (order != null) {
			status = HttpStatus.OK;
			receipt = toReceipt(order);
		}
		return new ResponseEntity<ReceiptDTO>(receipt, status);
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationErrorsDTO processValidationError(
			ConstraintViolationException ex) {
		ValidationErrorsDTO errors = new ValidationErrorsDTO();
		for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
			errors.getFieldErrors().put(v.getPropertyPath().toString(),
					v.getMessage());
		}
		return errors;
	}

}
