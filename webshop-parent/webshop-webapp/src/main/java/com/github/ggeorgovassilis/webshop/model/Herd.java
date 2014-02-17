package com.github.ggeorgovassilis.webshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Models an herd. Since the system accommodates only a single herd, this class is neither meant to be persisted nor
 * does it have an identifier
 * @author george georgovassilis
 *
 */
public class Herd implements Serializable {

	protected List<Animal> animals = new ArrayList<Animal>();
	
	/**
	 * Create a deep copy
	 */
	public Herd clone() {
		Herd copy = new Herd();
		for (Animal animal:animals)
			copy.getAnimals().add(animal.clone());
		return copy;
	}

	public List<Animal> getAnimals() {
		return animals;
	}

	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}
}
