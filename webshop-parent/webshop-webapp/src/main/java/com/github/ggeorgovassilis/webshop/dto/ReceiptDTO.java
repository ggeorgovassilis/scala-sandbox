package com.github.ggeorgovassilis.webshop.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;

/**
 * Models the response to an order
 * @author george georgovassilis
 *
 */
public class ReceiptDTO implements Serializable {

	protected double milk;
	protected int skins;
	protected int day;
	protected String id;
	protected String customerName;
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getMilk() {
		return milk;
	}

	public void setMilk(double milk) {
		this.milk = milk;
	}

	public int getSkins() {
		return skins;
	}

	public void setSkins(int skins) {
		this.skins = skins;
	}
}
