package com.github.ggeorgovassilis.webshop.service;


import org.springframework.http.ResponseEntity;

import com.github.ggeorgovassilis.webshop.dto.AnimalDTO;
import com.github.ggeorgovassilis.webshop.dto.HerdDTO;
import com.github.ggeorgovassilis.webshop.dto.OrderDTO;
import com.github.ggeorgovassilis.webshop.dto.StockDTO;

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

	/**
	 * Places an order and returns the part of the order that could be satisfied.
	 * @param order
	 * @param daysFromNow day on which to execute the order
	 * @return
	 */
	ResponseEntity<StockDTO> placeOrder(OrderDTO order, int daysFromNow);
}
