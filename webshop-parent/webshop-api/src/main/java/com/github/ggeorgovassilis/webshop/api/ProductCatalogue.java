package com.github.ggeorgovassilis.webshop.api;

import java.util.List;

/**
 * Suppliers must implement this interface which provides access to their supply store.
 * @author George Georgovassilis
 *
 */
public interface ProductCatalogue {

	/**
	 * Get available commodities
	 * @return
	 */
	List<CommodityDTO> getCommodities();
	
}
