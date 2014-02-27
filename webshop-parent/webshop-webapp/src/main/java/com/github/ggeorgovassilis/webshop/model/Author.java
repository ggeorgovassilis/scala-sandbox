package com.github.ggeorgovassilis.webshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Author implements Serializable {

	@Id
	@GeneratedValue
	protected Long id;

	@Column
	@NotBlank
	@Size(min=1, max=100)
	protected String name;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
