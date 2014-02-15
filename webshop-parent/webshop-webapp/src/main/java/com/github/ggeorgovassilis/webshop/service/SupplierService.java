package com.github.ggeorgovassilis.webshop.service;

import java.util.List;

import com.github.ggeorgovassilis.webshop.model.Commodity;
import com.github.ggeorgovassilis.webshop.model.Supplier;

public interface SupplierService {

	List<Commodity> getAvailableCommodities();
	Supplier persist(Supplier supplier);
	Commodity persist(Commodity commodity);
	Commodity findCommodity(String name);
	Supplier findSupplier(String name);
}
