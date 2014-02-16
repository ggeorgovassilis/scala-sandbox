package com.github.ggeorgovassilis.webshop.supplierwebservice.model;

import java.io.Serializable;

/**
 * Models an animal. All ages/times are in days.
 * @author george
 *
 */
public class Animal implements Serializable {

	public Animal() {
	}

	public Animal(String name, int age, int ageLastShaved) {
		setName(name);
		setAge(age);
		setAgeLastShaved(ageLastShaved);
	}
	
	/**
	 * Make a (deep) copy of this object
	 */
	public Animal clone() {
		return new Animal(name, age, ageLastShaved);
	}
	/**
	 * Animal name
	 */
	protected String name;
	
	/**
	 * Age of animal in days
	 */
	protected int age;
	
	/**
	 * Age at which the animal was last shaved
	 */
	protected int ageLastShaved;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int ageInDays) {
		this.age = ageInDays;
	}

	public int getAgeLastShaved() {
		return ageLastShaved;
	}

	public void setAgeLastShaved(int ageInDaysWhenLastShaved) {
		this.ageLastShaved = ageInDaysWhenLastShaved;
	}

	
}
