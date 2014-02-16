package com.github.ggeorgovassilis.webshop.supplierwebservice.service;


import com.github.ggeorgovassilis.webshop.supplierwebservice.dto.AnimalDTO;
import com.github.ggeorgovassilis.webshop.supplierwebservice.dto.HerdDTO;
import com.github.ggeorgovassilis.webshop.supplierwebservice.dto.StockDTO;

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
	StockDTO getStock(int daysFromNow);

	/**
	 * Return status of herd at a future point in time. Assumes no consumption or replenishment
	 * of the herd.
	 * @param daysFromNow Any positive number of days.
	 * @return
	 */
	HerdDTO getHerd(int daysFromNow);
	
	/**
	 * Update an animal.
	 * @param name Name of animal to update. If the name does not exist yet,a new animal is added to the herd
	 * @param age
	 * @param ageLastShorn
	 * @return
	 */
	AnimalDTO updateAnimal(String name, double age, double ageLastShorn);
}
