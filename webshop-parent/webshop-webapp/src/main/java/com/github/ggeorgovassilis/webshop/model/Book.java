package com.github.ggeorgovassilis.webshop.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
//	@Pattern(regexp="ISBN(-1(?:(0)|3))?:?\\x20+(?(1)(?(2)(?:(?=.{13}$)\\d{1,5}([ -])\\d{1,7}\\3\\d{1,6}\\3(?:\\d|x)$)|(?:(?=.{17}$)97(?:8|9)([ -])\\d{1,5}\\4\\d{1,7}\\4\\d{1,6}\\4\\d$))|(?(.{13}$)(?:\\d{1,5}([ -])\\d{1,7}\\5\\d{1,6}\\5(?:\\d|x)$)|(?:(?=.{17}$)97(?:8|9)([ -])\\d{1,5}\\6\\d{1,7}\\6\\d{1,6}\\6\\d$)))")
	protected String isbn;

	@Column
	@NotBlank
	@Size(min=1, max=100)
	protected String title;
	
	@OneToMany
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
