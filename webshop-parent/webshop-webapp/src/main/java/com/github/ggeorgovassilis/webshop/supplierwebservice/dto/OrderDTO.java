package com.github.ggeorgovassilis.webshop.supplierwebservice.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Models an order
 * @author George Georgovassilis
 *
 */
public class OrderDTO implements Serializable {

	@NotNull
	protected String customer;

	@NotNull
	protected StockDTO order;

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public StockDTO getOrder() {
		return order;
	}

	public void setOrder(StockDTO order) {
		this.order = order;
	}
}
