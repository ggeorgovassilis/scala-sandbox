package com.github.ggeorgovassilis.webshop.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Models an order
 * @author George Georgovassilis
 *
 */
@Entity
@Table(name="product_order")
public class Order implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	protected String id;
	
	@Column
	@NotNull
	@Min(0)
	protected double milk;

	@Column
	@NotNull
	@Min(0)
	protected int wool;
	
	@Column
	@NotNull
	protected Date date;
	
	@Column
	@NotNull
	@NotEmpty
	protected String customerName;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public int getWool() {
		return wool;
	}

	public void setWool(int wool) {
		this.wool = wool;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
