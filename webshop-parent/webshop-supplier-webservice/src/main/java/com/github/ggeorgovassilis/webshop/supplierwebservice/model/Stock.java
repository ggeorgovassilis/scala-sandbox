package com.github.ggeorgovassilis.webshop.supplierwebservice.model;

import java.io.Serializable;

public class Stock implements Serializable {

	protected double milk;
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
