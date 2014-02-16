package com.github.ggeorgovassilis.webshop.supplierwebservice.dao;

import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Herd;

/**
 * Declares functions for reading and persisting a herd
 * @author george georgovassilis
 *
 */
public interface HerdDao {

	/**
	 * Loads a herd identified by 'identifier'
	 * @param identifier
	 * @return
	 */
	Herd find(String identifier);
}
