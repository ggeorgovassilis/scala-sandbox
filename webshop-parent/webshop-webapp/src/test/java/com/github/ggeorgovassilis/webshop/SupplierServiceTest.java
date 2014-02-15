package com.github.ggeorgovassilis.webshop;

import javax.validation.ValidationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

import com.github.ggeorgovassilis.webshop.model.Commodity;
import com.github.ggeorgovassilis.webshop.model.Supplier;
import com.github.ggeorgovassilis.webshop.service.SupplierService;

public class SupplierServiceTest extends BaseTest {

	@Test
	public void testBasicPersistence() {

		Supplier supplier = makeSupplier("Supplier 1", "Test Supplier");
		supplierService.persist(supplier);

		Commodity c1 = makeCommodity("commodity 1",
				"description of commodity 1", "kgr", 1, supplier);
		Commodity c2 = makeCommodity("commodity 2",
				"description of commodity 2", "lt", 2, supplier);
		c1 = supplierService.persist(c1);
		c2 = supplierService.persist(c2);

		assertEquals("commodity 1", c1.getName());
		assertEquals("description of commodity 1", c1.getDescription());
		assertEquals("kgr", c1.getUnitName());
		assertEquals(1, c1.getPricePerUnit());

		assertEquals("commodity 2", c2.getName());
		assertEquals("description of commodity 2", c2.getDescription());
		assertEquals("lt", c2.getUnitName());
		assertEquals(2, c2.getPricePerUnit());
	}


}
