package com.github.ggeorgovassilis.webshop.dto;

import java.io.Serializable;

/**
 * Models a herd animal. Ages are in animal years.
 * @author George Georgovassilis
 *
 */
public class AnimalDTO implements Serializable {

	/**
	 * The animal name is also the unique identifier
	 */
	protected String name;
	
	/**
	 * Age in animal years
	 */
	protected double age;
	
	/**
	 * Age at which animal was last shaved. In animal years.
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
