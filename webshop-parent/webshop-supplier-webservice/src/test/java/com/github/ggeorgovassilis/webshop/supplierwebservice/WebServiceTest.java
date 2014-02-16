package com.github.ggeorgovassilis.webshop.supplierwebservice;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.ggeorgovassilis.webshop.supplierwebservice.dto.HerdDTO;
import com.github.ggeorgovassilis.webshop.supplierwebservice.dto.StockDTO;
import com.github.ggeorgovassilis.webshop.supplierwebservice.service.SupplierService;

import static org.junit.Assert.*;

/**
 * Tests the webservice which provides access to the herd database and production functions
 * @author George Georgovassilis
 *
 */
public class WebServiceTest extends BaseTest{

	final double error = 0.01;
	
	@Autowired
	protected SupplierService service;
	
	@Test
	public void testGetStock() {
		StockDTO stock = service.getStock(13);
		assertEquals(1104.48, stock.getMilk(),error);
		assertEquals(3, stock.getSkins());
	}

	@Test
	public void testGetHerd() {
		HerdDTO herd = service.getHerd(13);
		assertEquals(3, herd.getAnimals().size());
		assertEquals("Betty-1", herd.getAnimals().get(0).getName());
		assertEquals(4.13, herd.getAnimals().get(0).getAge(),error);
		assertEquals(0, herd.getAnimals().get(0).getAgeLastShaved(),error);
	}

	@Test
	public void testUpdateHerd() {
		service.updateAnimal("Betty-1", 4, 3.9);
		HerdDTO herd = service.getHerd(13);
		assertEquals(3, herd.getAnimals().size());
		assertEquals("Betty-1", herd.getAnimals().get(0).getName());
		assertEquals(4.13, herd.getAnimals().get(0).getAge(),error);
		assertEquals(3.9, herd.getAnimals().get(0).getAgeLastShaved(),error);
	}
}
