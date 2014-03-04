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
class CrudLibraryServiceTest extends BaseLibraryRestServiceTest {

  "An author" should "be found in the database exectly the way he was persisted" in {
    var a = new Author()
    a.setName("Author 1")
    var response = post("/api/authors", a)
    response.status should be(STATUS_CREATED)

    a = response.value.get
    a.getName() should be("Author 1")

    a.setName("Author 1 updated");
    response = post("/api/authors", a)
    a = response.value.get
    a.getName() should be("Author 1 updated")

  }

  "An author" should "not persist if the name is missing" in {
    var a = new Author()
    a.setName(null)
    val response = post("/api/authors", a)
    response.status should be(STATUS_BAD)

    //TODO: overload equality operator or have a look how "should be" might work nicer
    checkErrors(response.errors.get, ("name", "may not be empty")) should be(true)
  }

  "A publisher" should "be found in the database exectly the way he was persisted" in {
    var p = new Publisher()
    p.setName("Publisher 1")
    var response = post("/api/publishers", p)
    p = response.value.get
    p.getName() should be("Publisher 1")

    p.setName("Publisher 1 updated")
    response = post("/api/publishers", p)
    p = response.value.get
    p.getName() should be("Publisher 1 updated")
  }

  "A publisher" should "not be persisted when his name is missing" in {
    val p = new Publisher()
    val response = post("/api/publishers", p)
    response.status should be(STATUS_BAD)
    checkErrors(response.errors.get, ("name", "may not be empty")) should be(true)
  }

  "A book" should "be found exactly the way it was persisted" in {
    val a = newAuthor("Author 1")
    val p = newPublisher("Publisher 1")
    var book = newBook("Book 1", "123456789X", 1988, p, 1, a);

    val id = book.getId()
    var response = get("/api/books/" + book.getId(), classOf[Book])
    book = response.value.get

    book.getId() should be(id)
    book.getTitle() should be("Book 1")
    hasExactlyTheseAuthors(book.getAuthors(), "Author 1") should be(true)
    book.getPublisher().getId() should be(p.getId())
    book.getPublicationYear() should be(1988)

    val coAuthor = newAuthor("Coauthor");
    book.getAuthors().add(coAuthor);
    book = post("/api/books", book).value.get
    book.getId() should be(id)
    book.getTitle() should be("Book 1")
    hasExactlyTheseAuthors(book.getAuthors(), "Author 1", "Coauthor") should be(true)
    book.getPublisher().getId() should be(p.getId())
    book.getPublicationYear() should be(1988)
    book.getAvailability() should be (1)

  }

}