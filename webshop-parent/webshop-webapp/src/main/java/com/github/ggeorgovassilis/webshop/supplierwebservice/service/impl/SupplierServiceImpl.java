package com.github.ggeorgovassilis.webshop.supplierwebservice.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.ggeorgovassilis.webshop.supplierwebservice.application.ProductionPrediction;
import com.github.ggeorgovassilis.webshop.supplierwebservice.dao.AnimalDao;
import com.github.ggeorgovassilis.webshop.supplierwebservice.dao.HerdDao;
import com.github.ggeorgovassilis.webshop.supplierwebservice.dto.AnimalDTO;
import com.github.ggeorgovassilis.webshop.supplierwebservice.dto.HerdDTO;
import com.github.ggeorgovassilis.webshop.supplierwebservice.dto.StockDTO;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Animal;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Herd;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Production;
import com.github.ggeorgovassilis.webshop.supplierwebservice.service.SupplierService;

/**
 * Webservice that implements the webservice
 * @author George Georgovassilis
 *
 */
@Controller
@Transactional
public class SupplierServiceImpl implements SupplierService{

	@Autowired
	protected Production production;

	@Autowired
	protected HerdDao herdDao;

	@Autowired
	protected AnimalDao animalDao;
	@Override
	@RequestMapping(value = "/api/stock/{daysFromNow}", method = RequestMethod.GET)
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
		animal = animalDao.save(animal);
		return toDto(animal);
	}
	
	@PostConstruct
	public void initialized() {
		Herd herd = herdDao.find("classpath:webshop/herd.xml");
		animalDao.save(herd.getAnimals());
	}

}
