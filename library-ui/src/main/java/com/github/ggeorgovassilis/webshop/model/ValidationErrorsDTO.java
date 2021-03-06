package com.github.ggeorgovassilis.webshop.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



public class ValidationErrorsDTO implements Serializable {

	protected Map<String, String> fieldErrors = new HashMap<>();
	protected String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

}
