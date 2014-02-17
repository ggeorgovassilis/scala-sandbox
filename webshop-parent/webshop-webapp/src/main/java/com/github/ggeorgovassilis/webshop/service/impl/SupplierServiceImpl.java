package com.github.ggeorgovassilis.webshop.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.github.ggeorgovassilis.webshop.application.ProductionPrediction;
import com.github.ggeorgovassilis.webshop.dao.AnimalDao;
import com.github.ggeorgovassilis.webshop.dao.HerdDao;
import com.github.ggeorgovassilis.webshop.dto.AnimalDTO;
import com.github.ggeorgovassilis.webshop.dto.HerdDTO;
import com.github.ggeorgovassilis.webshop.dto.OrderDTO;
import com.github.ggeorgovassilis.webshop.dto.StockDTO;
import com.github.ggeorgovassilis.webshop.model.Animal;
import com.github.ggeorgovassilis.webshop.model.Herd;
import com.github.ggeorgovassilis.webshop.model.Production;
import com.github.ggeorgovassilis.webshop.service.SupplierService;

/**
 * Webservice that implements the webservice
 * @author George Georgovassilis
 *
 */
@Controller
@Transactional
@RequestMapping("/api")
public class SupplierServiceImpl implements SupplierService{

	@Autowired
	protected Production production;

	@Autowired
	protected HerdDao herdDao;
	
	@Autowired
	protected Validator validator;

	@Autowired
	protected AnimalDao animalDao;
	
	protected void validate(Object o) {
		Set<ConstraintViolation<Object>> result = validator.validate(o);
		if (!result.isEmpty()) {
			String message = "Validation failed: ";
			String prefix="";
			for (ConstraintViolation<Object> cv:result) {
				message+=prefix;
				prefix=",";
				message+=cv.getPropertyPath()+": "+cv.getMessage();
			}
			throw new ValidationException(message);
		}
	}
	
	@Override
	@RequestMapping(value = "/stock/{daysFromNow}", method = RequestMethod.GET)
	public @ResponseBody StockDTO getStock(@PathVariable int daysFromNow) {
		Herd herd = new Herd();
		herd.setAnimals(animalDao.findAll());
		StockDTO stock = new StockDTO();
		stock.setMilk(production.getMilkOutputAtDate(herd, daysFromNow));
		stock.setSkins(production.getWoolOutputAtDate(herd, daysFromNow));
		return stock;
	}

	AnimalDTO toDto(Animal animal) {
		AnimalDTO dto = new AnimalDTO();
		dto.setAge(production.years(animal.getAge()));
		dto.setAgeLastShaved(production.years(animal.getAgeLastShaved()));
		dto.setName(animal.getName());
		return dto;
	}
	
	@Override
	@RequestMapping(value = "/herd/{daysFromNow}", method = RequestMethod.GET)
	public @ResponseBody HerdDTO getHerd(@PathVariable int daysFromNow) {
		HerdDTO herdDTO = new HerdDTO();
		for (Animal animal : animalDao.findAll()) {
			AnimalDTO animalDTO = toDto(animal);
			animalDTO.setAge(production.getAnimalAgeInYearsOnDay(animal, daysFromNow));
			herdDTO.getAnimals().add(animalDTO);
		}
		return herdDTO;
	}

	@Override
	@RequestMapping(value = "/herd/add", method = RequestMethod.GET)
	public @ResponseBody AnimalDTO updateAnimal(@RequestParam("name") String name, @RequestParam("age") double age, @RequestParam("ageLastShorn") double ageLastShorn) {
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
	public @ResponseBody ResponseEntity<StockDTO> placeOrder(@RequestBody OrderDTO order, @PathVariable int daysFromNow) {
		HttpStatus statusCode = HttpStatus.CREATED;
		validate(order);
		StockDTO stock = getStock(daysFromNow);
		stock.setSkins(stock.getSkins()>=order.getOrder().getSkins()?order.getOrder().getSkins():0);
		stock.setMilk(stock.getMilk()>=order.getOrder().getMilk()?order.getOrder().getMilk():0);
		if (stock.getSkins()!=order.getOrder().getSkins() || stock.getMilk()!=order.getOrder().getMilk())
			statusCode = HttpStatus.PARTIAL_CONTENT;
		if (stock.getSkins() == 0 && stock.getMilk() == 0)
			statusCode = HttpStatus.NOT_FOUND;
		return new ResponseEntity<StockDTO>(stock, statusCode);
	}
	
	@PostConstruct
	public void initialized() {
		Herd herd = herdDao.find("classpath:webshop/herd.xml");
		animalDao.save(herd.getAnimals());
	}

}
