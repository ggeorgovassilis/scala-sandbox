package com.github.ggeorgovassilis.webshop.service;

import java.util.List;

import com.github.ggeorgovassilis.webshop.model.Availability;
import com.github.ggeorgovassilis.webshop.model.Commodity;
import com.github.ggeorgovassilis.webshop.model.Supplier;

/**
 * Functional interface for supplier/stock related functions.
 * @author george georgovassilis
 *
 */
public interface SupplierService {

	/**
	 * Get a list of all commodities whether in stock or not.
	 * @return List of commodities or empty list, if no commodities are registered
	 */
	List<Commodity> getAllCommodities();
	
	/**
	 * Stores a new supplier or updates an existing one. Will perform validations declared
	 * on the {@link Supplier} entity.
	 * @param supplier
	 * @return The persisted supplier
	 */
	
	Supplier persist(Supplier supplier);
	
	/**
	 * Stores a new commodity or updates an existing one. Will perform validations declared
	 * on the {@link Commodity} entity/
	 * @param commodity
	 * @return The persisted commodity
	 */
	Commodity persist(Commodity commodity);
	
	/**
	 * Finds and returns a commodity by name
	 * @param name
	 * @return The commodity or null if none is registered under the given name
	 */
	Commodity findCommodity(String name);
	
	/**
	 * Finds and returns a supplier by name
	 * @param name
	 * @return The supplier or null if none is registered under the given name
	 */
	Supplier findSupplier(String name);
	
	/**
	 * Finds the availability of a commodity. If the commodity exists, then an availability
	 * is always returned.
	 * @param commodity
	 * @return Availability or null if commodity is not registerd
	 */
	Availability findAvailability(Commodity commodity);
	
	/**
	 * Adds delta to the existing availability. The resulting quantity can never be less than 0
	 * @param commodity
	 * @param delta
	 * @return New availability
	 */
	Availability modifyAvailability(Commodity commodity, int delta);
}
