package com.github.ggeorgovassilis.webshop.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Book implements Serializable {

	@Id
	@GeneratedValue
	protected Long id;
	
	@Column
	@Pattern(message="not a valid ISBN", regexp="^(?:ISBN(?:-1[03])?:??)?(?=[-0-9?]{17}$|[-0-9X?]{13}$|[0-9X]{10}$)?(?:97[89][-?]?)?[0-9]{1,5}[-?]?(?:[0-9]+[-?]?){2}[0-9X]$")
	protected String isbn;

	@Column
	@NotBlank
	@Size(min=1, max=100)
	protected String title;
	
	@ManyToMany
	@NotNull
	@NotEmpty
	protected Set<Author> authors = new HashSet<>();
	
	@Column
	protected int publicationYear;
	
	@ManyToOne
	@NotNull
	protected Publisher publisher;

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}
	
	
}
