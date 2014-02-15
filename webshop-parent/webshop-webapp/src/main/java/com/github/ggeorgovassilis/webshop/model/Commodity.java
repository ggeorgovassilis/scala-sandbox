package com.github.ggeorgovassilis.webshop.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Models a commodity (a product for sale)
 * 
 * @author george georgovassilis
 * 
 */
@Entity
public class Commodity {

	@Id
	@Column(nullable = false)
	@NotNull
	@Size(min = 1, max = 30)
	protected String name;

	@Version
	protected Long version;

	@Column(nullable = false)
	@NotNull
	@Size(min = 1, max = 255)
	protected String description;

	@Column(nullable = false)
	@Size(min = 1, max = 30)
	protected String unitName;

	@Column(nullable = false)
	@NotNull
	@Min(value = 0)
	protected int pricePerUnit;

	@ManyToOne
	@NotNull
	protected Supplier supplier;

	@ElementCollection
	@JoinTable(name="CUSTOM_ATTRIBUTES", joinColumns=@JoinColumn(name="ID"))
	@MapKeyColumn (name="ATTRIBUTE_NAME")
	@Column(name="VALUE")
	protected Map<String, String> customAttributes = new HashMap<String, String>();
	
	public Map<String, String> getCustomAttributes() {
		return customAttributes;
	}

	public void setCustomAttributes(Map<String, String> customAttributes) {
		this.customAttributes = customAttributes;
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

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(int pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
