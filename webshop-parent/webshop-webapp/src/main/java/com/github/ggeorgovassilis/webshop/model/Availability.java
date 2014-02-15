package com.github.ggeorgovassilis.webshop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Models the availability of a commodity
 * @author george georgovassilis
 *
 */
@Entity
public class Availability {

	@Id
	@GeneratedValue
	protected Long id;
	
	@ManyToOne
	@NotNull
	protected Commodity commodity;

	/**
	 * Quantity in commodity units (see {@link Commodity#unitName}
	 */
	@Column
	@Min(0)
	protected int quantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
}
