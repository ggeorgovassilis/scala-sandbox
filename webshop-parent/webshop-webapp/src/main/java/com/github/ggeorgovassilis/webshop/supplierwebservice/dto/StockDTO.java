package com.github.ggeorgovassilis.webshop.supplierwebservice.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Models available stock for sale
 * @author george georgovassilis
 *
 */
public class StockDTO implements Serializable {

	@Min(0)
	protected double milk;

	@Min(0)
	protected int skins;

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
