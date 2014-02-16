package com.github.ggeorgovassilis.webshop.api;

import java.io.Serializable;

/**
 * Models a commodity which can be sold over the webshop
 * @author George Georgovassilis
 *
 */
public class CommodityDTO implements Serializable {

	protected String name;
	protected String description;
	protected String unitName;
	protected int pricePerUnit;
	protected String supplierName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(int pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

}
