package com.github.ggeorgovassilis.webshop;

import javax.validation.ValidationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

import com.github.ggeorgovassilis.webshop.model.Commodity;
import com.github.ggeorgovassilis.webshop.model.Supplier;
import com.github.ggeorgovassilis.webshop.service.SupplierService;

public class SupplierServiceValidationsTest extends BaseTest {


	@Test(expected = ValidationException.class)
	public void testValidationForCommodities_missingSupplier() {
		Commodity c1 = makeCommodity("commodity 1",
				"description of commodity 1", "kgr", 1, null);
		supplierService.persist(c1);
	}

	@Test(expected = ValidationException.class)
	public void testValidationForCommodities_missingName() {
		Commodity c1 = makeCommodity(null, "description of commodity 1", "kgr",
				1, null);
		supplierService.persist(c1);
	}

	@Test(expected = ValidationException.class)
	public void testValidationForCommodities_negativeUnitPrice() {
		Commodity c1 = makeCommodity("commodity 1", "description of commodity 1", "kgr",
				-1, null);
		supplierService.persist(c1);
	}

	@Test(expected = ValidationException.class)
	public void testValidationForCommodities_nameTooLong() {
		Commodity c1 = makeCommodity("a commodity with a rediculously long name that noone can remember", "description of commodity 1", "kgr",
				1, null);
		supplierService.persist(c1);
	}

	@Test(expected = ValidationException.class)
	public void testValidationForSupplier_missingDescription() {
		Supplier supplier = makeSupplier("Supplier 1", null);
		supplierService.persist(supplier);
	}

	// ...
	// we could check the validation for more properties here, for this exercise
	// the current validations shall suffice

}
