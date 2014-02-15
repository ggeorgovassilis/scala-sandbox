package com.github.ggeorgovassilis.webshop.supplierwebservice.model;

import java.io.Serializable;

public class Animal implements Serializable {

	public Animal() {
	}

	public Animal(String name, double age, double ageLastShaved) {
		setName(name);
		setAge(age);
		setAgeLastShaved(ageLastShaved);
	}
	/**
	 * Animal name
	 */
	protected String name;
	
	/**
	 * Age of animal in species years (not solar years)
	 */
	protected double age;
	
	/**
	 * Age at which the animal was last shaved (see {@link #age} about scales).
	 */
	protected double ageLastShaved;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public double getAgeLastShaved() {
		return ageLastShaved;
	}

	public void setAgeLastShaved(double ageLastShaved) {
		this.ageLastShaved = ageLastShaved;
	}
	
}
