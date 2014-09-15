package com.github.ggeorgovassilis.webshop.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Author extends BaseEntity {

	@Column
	@NotBlank
	@Size(min=1, max=100)
	protected String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
