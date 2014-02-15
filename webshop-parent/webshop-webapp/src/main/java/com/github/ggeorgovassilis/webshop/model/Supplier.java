package com.github.ggeorgovassilis.webshop.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Supplier {

	@Id
	@Column(nullable=false)
	@NotNull
	@Size(min=1, max=30)
	protected String name;

	@Column(nullable=false)
	@NotNull
	@Size(min=1, max=255)
	protected String description;

	@OneToMany(cascade=CascadeType.ALL)
	protected List<Commodity> commodities = new ArrayList<Commodity>();

	@Version
	protected Long version;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Commodity> getCommodities() {
		return commodities;
	}

	public void setCommodities(List<Commodity> commodities) {
		this.commodities = commodities;
	}
}
