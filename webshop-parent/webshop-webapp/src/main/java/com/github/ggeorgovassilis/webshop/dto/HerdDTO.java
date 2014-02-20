package com.github.ggeorgovassilis.webshop.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Models the entire herd
 * @author george georgovassilis
 *
 */
public class HerdDTO {

	protected List<AnimalDTO> herd = new ArrayList<AnimalDTO>();

	public List<AnimalDTO> getHerd() {
		return herd;
	}

	public void setHerd(List<AnimalDTO> animals) {
		this.herd = animals;
	}
}
