package com.github.ggeorgovassilis.webshop;

import java.util.List;

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.Assert.*;

import com.github.ggeorgovassilis.webshop.model.Commodity;
import com.github.ggeorgovassilis.webshop.model.Supplier;
import com.github.ggeorgovassilis.webshop.service.SupplierService;

/**
 * Performs integration tests around persistence functions of the {@link SupplierService}
 * @author george georgovassilis
 *
 */
public class SupplierServicePersistenceTest extends BaseTest {

	/**
	 * Create a supplier and a couple commodities which should pass all validations, persist them
	 * and sample if they have been saved ok
	 */
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
		
		Commodity c1prime = supplierService.findCommodity(c1.getName());
		assertEquals(c1.getName(), c1prime.getName());
		assertEquals(c1.getDescription(), c1prime.getDescription());
		assertEquals(c1.getSupplier(), c1prime.getSupplier());
	}

	/**
	 * Test persistence by not saving commodities one by one, but instead
	 * saving them in a batch together with the supplier
	 */
	@Test
	public void testCascadingPersistenceForSupplier() {

		Supplier supplier = makeSupplier("Supplier 1", "Test Supplier");

		Commodity c1 = makeCommodity("commodity 1",
				"description of commodity 1", "kgr", 1, supplier);
		Commodity c2 = makeCommodity("commodity 2",
				"description of commodity 2", "lt", 2, supplier);

		supplierService.persist(supplier);

		Commodity c1prime = supplierService.findCommodity(c1.getName());
		Commodity c2prime = supplierService.findCommodity(c2.getName());
		assertEquals(c1.getDescription(), c1prime.getDescription());
		assertEquals(c2.getPricePerUnit(), c2prime.getPricePerUnit());
		assertEquals(c1.getSupplier(), supplier);
	}

	/**
	 * Saves a commodity and then updates it
	 */
	@Test
	public void testUpdating() {
		Supplier supplier = makeSupplier("Supplier 1", "Test Supplier");
		supplier = supplierService.persist(supplier);

		Commodity c1 = makeCommodity("commodity 1",
				"description of commodity 1", "kgr", 1, supplier);
		c1 = supplierService.persist(c1);
		c1.setDescription("updated");
		c1 = supplierService.persist(c1);
		
		Commodity c1prime = supplierService.findCommodity(c1.getName());
		assertEquals("updated", c1prime.getDescription());
	}

	/**
	 * Saves a commodity and then tries to save a new instance with the same name.
	 * Validate that this is not possible
	 * original one
	 */
	@Test(expected=DataIntegrityViolationException.class)
	public void testThatAliasingFails() {
		Supplier supplier = makeSupplier("Supplier 1", "Test Supplier");
		supplier = supplierService.persist(supplier);

		Commodity c1 = makeCommodity("commodity 1",
				"description of commodity 1", "kgr", 1, supplier);
		c1 = supplierService.persist(c1);
		
		Commodity c1prime = makeCommodity(c1.getName(),
				"updated", c1.getUnitName(), c1.getPricePerUnit(), c1.getSupplier());
	
		c1prime = supplierService.persist(c1prime);
	}
	
	/**
	 * Tests that all commodities can be retrieved
	 */
	@Test
	public void testGetAvailableCommodities() {
		Supplier supplier1 = makeSupplier("Supplier 1", "Test Supplier 1");
		supplier1 = supplierService.persist(supplier1);

		Supplier supplier2 = makeSupplier("Supplier 2", "Test Supplier 2");
		supplier2 = supplierService.persist(supplier2);

		Commodity c1 = makeCommodity("commodity 1",
				"description of commodity 1", "kgr", 1, supplier1);
		Commodity c2 = makeCommodity("commodity 2",
				"description of commodity 2", "lt", 2, supplier1);
		Commodity c3 = makeCommodity("commodity 3",
				"description of commodity 3", "ccm", 3, supplier2);
		
		c1 = supplierService.persist(c1);
		c2 = supplierService.persist(c2);
		c3 = supplierService.persist(c3);
		
		List<Commodity> commodities = supplierService.getAvailableCommodities();
		assertEquals(3, commodities.size());
		assertTrue(commodities.contains(c1));
		assertTrue(commodities.contains(c2));
		assertTrue(commodities.contains(c3));
	}
}
