package com.github.ggeorgovassilis.webshop.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
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
	
	@Min(1)
	protected int day = 1;
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

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
