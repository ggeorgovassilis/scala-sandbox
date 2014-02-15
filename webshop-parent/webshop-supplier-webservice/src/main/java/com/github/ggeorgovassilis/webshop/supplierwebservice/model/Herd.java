package com.github.ggeorgovassilis.webshop.supplierwebservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Herd implements Serializable {

	protected List<Animal> herd = new ArrayList<Animal>();

	public List<Animal> getHerd() {
		return herd;
	}

	public void setHerd(List<Animal> herd) {
		this.herd = herd;
	}
}
