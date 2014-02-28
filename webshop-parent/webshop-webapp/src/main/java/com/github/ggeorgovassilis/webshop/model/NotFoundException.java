package com.github.ggeorgovassilis.webshop.model;

public class NotFoundException extends RuntimeException{

	public NotFoundException(String message){
		super(message);
	}
}
