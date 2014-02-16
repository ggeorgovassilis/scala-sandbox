package com.github.ggeorgovassilis.webshop.supplierwebservice.dto;

import java.io.Serializable;

/**
 * Models a herd animal. Ages are in animal years.
 * @author George Georgovassilis
 *
 */
public class AnimalDTO implements Serializable {

	protected String name;
	protected double age;
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
