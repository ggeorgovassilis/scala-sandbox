package com.github.ggeorgovassilis.webshop;

import java.util.List;

import javax.validation.ValidationException;

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.Assert.*;

import com.github.ggeorgovassilis.webshop.model.Availability;
import com.github.ggeorgovassilis.webshop.model.Commodity;
import com.github.ggeorgovassilis.webshop.model.Supplier;
import com.github.ggeorgovassilis.webshop.service.SupplierService;

/**
 * Tests functions related to availability handling of the
 * {@link SupplierService}
 * 
 * @author george georgovassilis
 * 
 */
public class SupplierServiceAvailabilityTest extends BaseTest {

	/**
	 * Create a commodity and modifies its availability
	 */
	@Test
	public void testSimpleAvailabiltyHandling() {

		Supplier supplier = makeSupplier("Supplier 1", "Test Supplier");
		supplierService.persist(supplier);

		Commodity c1 = makeCommodity("commodity 1",
				"description of commodity 1", "kgr", 1, supplier);
		c1 = supplierService.persist(c1);
		
		//important: verify that there is an initial availability of 0 (not null)
		Availability a1 = supplierService.findAvailability(c1);
		assertEquals(0, a1.getQuantity());
		assertEquals(c1, a1.getCommodity());

		
		a1 = supplierService.modifyAvailability(c1, 10);
		assertEquals(10, a1.getQuantity());

		a1 = supplierService.modifyAvailability(c1, -5);
		assertEquals(5, a1.getQuantity());
	}

	/**
	 * Test that it's not possible to reduce availability below 0
	 */
	@Test(expected=ValidationException.class)
	public void testReduceAvailabilityBelowZero() {

		Supplier supplier = makeSupplier("Supplier 1", "Test Supplier");
		supplierService.persist(supplier);

		Commodity c1 = makeCommodity("commodity 1",
				"description of commodity 1", "kgr", 1, supplier);
		c1 = supplierService.persist(c1);

		supplierService.modifyAvailability(c1, 10);
		supplierService.modifyAvailability(c1, -10);
		supplierService.modifyAvailability(c1, -10);
	}

}
