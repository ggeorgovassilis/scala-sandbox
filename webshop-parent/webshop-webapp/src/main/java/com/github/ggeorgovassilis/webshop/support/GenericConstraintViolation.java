package com.github.ggeorgovassilis.webshop.support;

import java.io.Serializable;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

/**
 * Models a generic constraint violation (i.e. general messages)
 * @author george georgovassilis
 *
 */
public class GenericConstraintViolation<T> implements ConstraintViolation<T>, Serializable{

	protected String message;
	protected String field;
	
	public GenericConstraintViolation(String message, String field){
		this.field = field;
		this.message = message;
	}

	public String getField(){
		return field;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getMessageTemplate() {
		return null;
	}

	@Override
	public T getRootBean() {
		return null;
	}

	@Override
	public Class<T> getRootBeanClass() {
		return null;
	}

	@Override
	public Object getLeafBean() {
		return null;
	}

	@Override
	public Object[] getExecutableParameters() {
		return null;
	}

	@Override
	public Object getExecutableReturnValue() {
		return null;
	}

	@Override
	public Path getPropertyPath() {
		return null;
	}

	@Override
	public Object getInvalidValue() {
		return null;
	}

	@Override
	public ConstraintDescriptor<?> getConstraintDescriptor() {
		return null;
	}

	@Override
	public <U> U unwrap(Class<U> type) {
		return null;
	}
	
}
