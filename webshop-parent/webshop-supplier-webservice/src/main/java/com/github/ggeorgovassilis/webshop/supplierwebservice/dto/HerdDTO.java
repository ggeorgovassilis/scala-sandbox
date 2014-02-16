package com.github.ggeorgovassilis.webshop.supplierwebservice.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Models the entire herd
 * @author george georgovassilis
 *
 */
public class HerdDTO {

	protected List<AnimalDTO> animals = new ArrayList<AnimalDTO>();

	public List<AnimalDTO> getAnimals() {
		return animals;
	}

	public void setAnimals(List<AnimalDTO> animals) {
		this.animals = animals;
	}
}
