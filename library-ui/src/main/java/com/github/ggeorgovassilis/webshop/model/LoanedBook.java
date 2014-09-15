package com.github.ggeorgovassilis.webshop.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class LoanedBook extends BaseEntity {

	@ManyToOne
	@NotNull
	protected Book book;
	
	@Column
	@NotNull
	protected Date dateLoaned;
	
	@Column
	@NotEmpty
	protected String clientName;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Date getDateLoaned() {
		return dateLoaned;
	}

	public void setDateLoaned(Date dateLoaned) {
		this.dateLoaned = dateLoaned;
	}
	
}
