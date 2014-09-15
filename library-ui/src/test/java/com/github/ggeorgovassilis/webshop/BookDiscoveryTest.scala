package com.github.ggeorgovassilis.webshop
import org.springframework.test.context.TestContextManager
import scala.collection.JavaConversions._
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import javax.xml.bind.ValidationException
import javax.validation.ConstraintViolationException
import java.util.Collection
import com.github.ggeorgovassilis.webshop.model.Author
import com.github.ggeorgovassilis.webshop.model.ValidationErrorsDTO
import com.github.ggeorgovassilis.webshop.model.Publisher
import com.github.ggeorgovassilis.webshop.model.Book
import com.fasterxml.jackson.core.`type`.TypeReference
import org.junit.Test
import com.github.ggeorgovassilis.webshop.support.BaseScalaWebserviceTest
import com.github.ggeorgovassilis.webshop.model.LoanedBook

@Test
class BookDiscoveryTest extends BaseLibraryRestServiceTest {

  "A non-existing book" should "not be discoverable" in {
    var response = get("/api/books/123", classOf[Book])
    response.status should be(STATUS_NOT_FOUND)
    response.errors.get.getMessage() should be("Book with id 123 not found")
  }

  "Books" should "be available in a public listing" in {
    val a = newAuthor("Author 1")
    val coAuthor = newAuthor("Coauthor")
    val p = newPublisher("Publisher 1")
    var book = newBook("Book 1", "123456789X", 1988, p, 1, a)
    post("/api/books", book);

    var response = get("/api/books", new TypeReference[java.util.List[Book]] {})
    var books = response.value.get
    books.size should be(1)
    books.get(0).getTitle() should be("Book 1")

    book = newBook("Book 2", "123456999X", 2014, p, 1, a, coAuthor)

    response = get("/api/books", new TypeReference[java.util.List[Book]] {})
    books = response.value.get
    books.size should be(2)
    books.get(0).getTitle() should be("Book 1")
    books.get(1).getTitle() should be("Book 2")

    hasExactlyTheseAuthors(books.get(0).getAuthors(), "Author 1") should be(true)
    hasExactlyTheseAuthors(books.get(1).getAuthors(), "Author 1", "Coauthor") should be(true)
  }

  "Books" should "be discoverable by their title" in {
    val a = newAuthor("Author 1")
    val coAuthor = newAuthor("Coauthor")
    val p = newPublisher("Publisher 1")
    var book = newBook("Book 1", "123456789X", 1988, p, 1, a, coAuthor)

    post("/api/books", book);
    book = newBook("Book 2", "123456999X", 2014, p, 1, a, coAuthor)

    var response = get("/api/books/search/Book 1", new TypeReference[java.util.List[Book]] {})
    var books = response.value.get
    books.size should be(1)
    books.get(0).getTitle() should be("Book 1")

    response = get("/api/books/search/Book", new TypeReference[java.util.List[Book]] {})
    books = response.value.get
    books.size should be(2)
    books.get(0).getTitle() should be("Book 1")
    books.get(1).getTitle() should be("Book 2")
  }

  "Queries for boooks that don't match any books" should "return an error status" in {
    var response = get("/api/books/search/unexistable", classOf[ValidationErrorsDTO])
    response.status should be (STATUS_NOT_FOUND)
  }
  
}