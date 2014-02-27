package com.github.ggeorgovassilis.webshop.service;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.DispatcherServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ggeorgovassilis.webshop.model.Author;
import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO;

import static junit.framework.Assert.*;

public class LibaryServiceTest {

	private ObjectMapper mapper = new ObjectMapper();
	private DispatcherServlet dispatcherServlet;
	
	
	protected byte[] toJson(Object object) throws IOException {
		return mapper.writeValueAsBytes(object);
	}
	
	protected <T> T fromJson(String json, Class<T> c) throws Exception{
		return mapper.readValue(json, c);
	}

	@Before
	public void setup() throws Exception{
		MockServletConfig config = new MockServletConfig("spring");
		config.addInitParameter("contextConfigLocation","classpath:webshop/environment-context.xml, classpath:webshop/application-context.xml");
		dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.init(config);
	}
	
	@After
	public void tearDown() throws Exception{
		dispatcherServlet.destroy();
	}
	
	protected void invoke(MockHttpServletRequest request, MockHttpServletResponse response) throws Exception{
		dispatcherServlet.service(request, response);
	}
	
	protected MockHttpServletResponse post(String url, byte[] content) throws Exception{
		MockHttpServletRequest request = new MockHttpServletRequest(dispatcherServlet.getServletContext(), "POST", url);
		request.setContent(content);
		request.setContentType("application/json");
		MockHttpServletResponse response = new MockHttpServletResponse();
		invoke(request, response);
		return response;
	}

	@Test
	public void testCreateUpdateAndGetAuthor() throws Exception {
		Author a = new Author();
		a.setName("Author 1");
		MockHttpServletResponse response = post("/api/authors", toJson(a));
		System.out.println(response.getContentAsString());
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
		
		ValidationErrorsDTO errors = fromJson(response.getContentAsString(), ValidationErrorsDTO.class);
		assertEquals(1, errors.getFieldErrors().size());
		assertEquals("may not be empty", errors.getFieldErrors().get("name"));
	}

	@Test
	public void testListBooks() throws Exception {

//		MockHttpServletRequest request = new MockHttpServletRequest(wac.getServletContext(), "GET", "/api/books");
//		MockHttpServletResponse response = new MockHttpServletResponse();
//		
//		
//		invoke(request, response);
//		
//		assertEquals(200, response.getStatus());
//		assertEquals("[]", response.getContentAsString());
	}
}
