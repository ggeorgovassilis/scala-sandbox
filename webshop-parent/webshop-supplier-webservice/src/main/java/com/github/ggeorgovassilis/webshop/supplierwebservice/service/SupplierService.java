package com.github.ggeorgovassilis.webshop.supplierwebservice.service;

import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Herd;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Stock;

/**
 * Functional interface for an external supplier service
 * @author george georgovassilis
 *
 */
public interface SupplierService {

	/**
	 * Predict stock at a future point in time. Assumes no consumption
	 * @param daysFromNow Any positive number of days, 0 for current stock
	 * @return
	 */
	Stock getStock(int daysFromNow);

	/**
	 * Return status of herd at a future point in time. Assumes no consumption or replenishment
	 * of the herd.
	 * @param daysFromNow Any positive number of days.
	 * @return
	 */
	Herd getHerd(int daysFromNow);
}
