package com.github.ggeorgovassilis.webshop.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Models the response to an order
 * @author george georgovassilis
 *
 */
public class ReceiptDTO implements Serializable {

	protected double milk;
	protected int skins;
	protected Date date;
	protected String id;
	protected String customerName;
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
