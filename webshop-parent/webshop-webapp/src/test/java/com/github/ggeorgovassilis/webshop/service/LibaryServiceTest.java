package com.github.ggeorgovassilis.webshop.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.DispatcherServlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ggeorgovassilis.webshop.model.Author;
import com.github.ggeorgovassilis.webshop.model.Book;
import com.github.ggeorgovassilis.webshop.model.Publisher;
import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO;

import static junit.framework.Assert.*;

public class LibaryServiceTest {

	private ObjectMapper mapper = new ObjectMapper();
	private DispatcherServlet dispatcherServlet;

	protected byte[] toJson(Object object) throws IOException {
		return mapper.writeValueAsBytes(object);
	}

	protected <T> T fromJson(String json, Class<T> c) throws Exception {
		return mapper.readValue(json, c);
	}

	protected <T> T fromJson(String json, TypeReference<T> c) throws Exception {
		return mapper.readValue(json, c);
	}

	protected Author newAuthor(String name) throws Exception {
		Author a = new Author();
		a.setName(name);
		MockHttpServletResponse response = post("/api/authors", toJson(a));
		return fromJson(response.getContentAsString(), Author.class);
	}

	protected Publisher newPublisher(String name) throws Exception {
		Publisher p = new Publisher();
		p.setName(name);
		MockHttpServletResponse response = post("/api/publishers", toJson(p));
		return fromJson(response.getContentAsString(), Publisher.class);
	}

	@Before
	public void setup() throws Exception {
		MockServletConfig config = new MockServletConfig("spring");
		config.addInitParameter(
				"contextConfigLocation",
				"classpath:webshop/environment-context.xml, classpath:webshop/application-context.xml");
		dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.init(config);

		get("/api/reset");
	}

	@After
	public void tearDown() throws Exception {
		dispatcherServlet.destroy();
	}

	protected boolean hasExactlyTheseAuthors(Collection<Author> authors, String...names){
		if (authors.size()!=names.length)
			return false;
		Set<String> tmpNames = new HashSet<>(Arrays.asList(names));
		for (Author a:authors)
			tmpNames.remove(a.getName());
		return tmpNames.isEmpty();
	}
	
	protected void invoke(MockHttpServletRequest request,
			MockHttpServletResponse response) throws Exception {
		dispatcherServlet.service(request, response);
	}

	protected MockHttpServletResponse post(String url, byte[] content)
			throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest(
				dispatcherServlet.getServletContext(), "POST", url);
		request.setContent(content);
		request.setContentType("application/json");
		MockHttpServletResponse response = new MockHttpServletResponse();
		invoke(request, response);
		return response;
	}

	protected MockHttpServletResponse get(String url) throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest(
				dispatcherServlet.getServletContext(), "GET", url);
		MockHttpServletResponse response = new MockHttpServletResponse();
		invoke(request, response);
		return response;
	}

	@Test
	public void testCreateUpdateAndGetAuthor() throws Exception {
		Author a = new Author();
		a.setName("Author 1");
		MockHttpServletResponse response = post("/api/authors", toJson(a));
		assertEquals(200, response.getStatus());

		a = fromJson(response.getContentAsString(), Author.class);
		assertNotNull(a.getId());
		assertEquals("Author 1", a.getName());

		a.setName("Author 1 updated");
		response = post("/api/authors", toJson(a));
		fromJson(response.getContentAsString(), Author.class);
		assertNotNull(a.getId());
		assertEquals("Author 1 updated", a.getName());
	}

	@Test
	public void testCreateAuthorWithFailingValidation() throws Exception {
		Author a = new Author();
		a.setName(null);
		MockHttpServletResponse response = post("/api/authors", toJson(a));
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

		ValidationErrorsDTO errors = fromJson(response.getContentAsString(),
				ValidationErrorsDTO.class);
		assertEquals(1, errors.getFieldErrors().size());
		assertEquals("may not be empty", errors.getFieldErrors().get("name"));
	}

	@Test
	public void testCreateUpdateAndGetPublisher() throws Exception {
		Publisher p = newPublisher("Publisher 1");
		MockHttpServletResponse response = post("/api/publishers", toJson(p));
		assertEquals(200, response.getStatus());

		p = fromJson(response.getContentAsString(), Publisher.class);
		assertNotNull(p.getId());
		assertEquals("Publisher 1", p.getName());

		p.setName("Publisher 1 updated");
		response = post("/api/publishers", toJson(p));
		fromJson(response.getContentAsString(), Publisher.class);
		assertNotNull(p.getId());
		assertEquals("Publisher 1 updated", p.getName());
	}

	@Test
	public void testCreatePublisherWithFailingValidation() throws Exception {
		Publisher p = new Publisher();
		p.setName(null);
		MockHttpServletResponse response = post("/api/publishers", toJson(p));
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

		ValidationErrorsDTO errors = fromJson(response.getContentAsString(),
				ValidationErrorsDTO.class);
		assertEquals(1, errors.getFieldErrors().size());
		assertEquals("may not be empty", errors.getFieldErrors().get("name"));
	}

	@Test
	public void testCreateUpdateAndGetBook() throws Exception {
		Author a = newAuthor("Author 1");
		Publisher p = newPublisher("Publisher 1");

		Book book = new Book();
		book.getAuthors().add(a);
		book.setPublisher(p);
		book.setTitle("Book 1");
		book.setIsbn("123456789X");
		book.setPublicationYear(1988);

		book = fromJson(post("/api/books", toJson(book)).getContentAsString(),
				Book.class);

		assertNotNull(book.getId());
		long id = book.getId();
		book = fromJson(get("/api/books/" + book.getId()).getContentAsString(),
				Book.class);
		assertEquals(id, book.getId().longValue());
		assertEquals("Book 1", book.getTitle());
		assertEquals(a.getId(), book.getAuthors().iterator().next().getId());
		assertEquals(p.getId(), book.getPublisher().getId());
		assertEquals(1988, book.getPublicationYear());

		Author coAuthor = newAuthor("Coauthor");
		book.getAuthors().add(coAuthor);
		book = fromJson(post("/api/books", toJson(book)).getContentAsString(),
				Book.class);

		assertEquals(id, book.getId().longValue());
		assertEquals("Book 1", book.getTitle());
		assertTrue(hasExactlyTheseAuthors(book.getAuthors(), "Author 1","Coauthor"));

		assertEquals(p.getId(), book.getPublisher().getId());
		assertEquals(1988, book.getPublicationYear());

	}

	@Test
	public void testCreateBookFailingValidation() throws Exception {
		Author a = newAuthor("Author 1");
		Publisher p = newPublisher("Publisher 1");

		Book book = new Book();
		book.getAuthors().add(a);
		book.setPublisher(p);
		book.setTitle(null);
		book.setIsbn("not an isbn");
		book.setPublicationYear(1988);

		ValidationErrorsDTO errors = fromJson(post("/api/books", toJson(book))
				.getContentAsString(), ValidationErrorsDTO.class);

		assertEquals(2, errors.getFieldErrors().size());
		assertEquals("may not be empty", errors.getFieldErrors().get("title"));
		assertEquals("not a valid ISBN", errors.getFieldErrors().get("isbn"));
	}

	@Test
	public void testListBooks() throws Exception {
		Author a = newAuthor("Author 1");
		Author coAuthor = newAuthor("Coauthor");
		Publisher p = newPublisher("Publisher 1");

		Book book = new Book();
		book.getAuthors().add(a);
		book.setPublisher(p);
		book.setTitle("Book 1");
		book.setIsbn("123456789X");
		book.setPublicationYear(1988);

		post("/api/books", toJson(book));

		List<Book> books = fromJson(get("/api/books").getContentAsString(),
				new TypeReference<List<Book>>() {
				});
		assertEquals(1, books.size());
		assertEquals("Book 1", books.get(0).getTitle());

		book = new Book();
		book.getAuthors().add(a);
		book.getAuthors().add(coAuthor);
		book.setPublisher(p);
		book.setTitle("Book 2");
		book.setIsbn("123456999X");
		book.setPublicationYear(2014);

		post("/api/books", toJson(book));

		books = fromJson(get("/api/books").getContentAsString(),
				new TypeReference<List<Book>>() {
				});
		assertEquals(2, books.size());
		assertEquals("Book 1", books.get(0).getTitle());
		assertEquals("Book 2", books.get(1).getTitle());
		assertTrue(hasExactlyTheseAuthors(books.get(0).getAuthors(), "Author 1"));
		assertTrue(hasExactlyTheseAuthors(books.get(1).getAuthors(), "Author 1", "Coauthor"));
	}
}
