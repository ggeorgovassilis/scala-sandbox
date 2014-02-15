package com.github.ggeorgovassilis.webshop;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.github.ggeorgovassilis.webshop.model.Commodity;
import com.github.ggeorgovassilis.webshop.model.Supplier;
import com.github.ggeorgovassilis.webshop.service.SupplierService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/application-context.xml","classpath:webshop/environment-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
abstract class BaseTest {

	@Autowired
	protected SupplierService supplierService;

	protected Commodity makeCommodity(String name, String description,
			String unitName, int pricePerUnit, Supplier supplier) {
		Commodity commodity = new Commodity();
		commodity.setName(name);
		commodity.setDescription(description);
		commodity.setPricePerUnit(pricePerUnit);
		commodity.setUnitName(unitName);
		commodity.setSupplier(supplier);
		if (supplier != null)
			supplier.getCommodities().add(commodity);
		return commodity;
	}

	protected Supplier makeSupplier(String name, String description) {
		Supplier supplier = new Supplier();
		supplier.setName(name);
		supplier.setDescription(description);
		return supplier;
	}

}
