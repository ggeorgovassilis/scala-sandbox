package com.github.ggeorgovassilis.webshop.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Models validation errors
 * 
 * @author george georgovassilis
 * 
 */
public class ValidationErrorsDTO implements Serializable {

	private Map<String, String> fieldErrors = new HashMap<>();

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}
