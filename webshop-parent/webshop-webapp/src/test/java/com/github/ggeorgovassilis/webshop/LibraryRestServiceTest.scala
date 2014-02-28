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

class LibraryRestServiceTest extends BaseScalaWebserviceTest {

  def hasExactlyTheseAuthors(authors: Collection[Author], names: String*): Boolean = {
    if (authors.size() != names.length)
      return false;
    var tmpNames: List[String] = names.toList;
    authors.foreach(a => {
      tmpNames = tmpNames.diff(List(a.getName()));
    });
    tmpNames.isEmpty;
  }

  def checkErrors(errors: ValidationErrorsDTO, expectedErrors: Tuple2[String, String]*): Boolean = {
    val ees = expectedErrors.size
    var ok = true
    if (errors.getFieldErrors().size() == expectedErrors.size) {
      expectedErrors.foreach {
        case (field, expectedMessage) => {
          val actualMessage = errors.getFieldErrors().get(field)
          if (!expectedMessage.equals(actualMessage))
            ok = false
        }
      }
    } else
      ok = false
    return ok
  }

  def newAuthor(name: String) = {
    val a = new Author()
    a.setName(name)
    post("/api/authors", a).value.get
  }

  def newPublisher(name: String) = {
    val p = new Publisher();
    p.setName(name);
    post("/api/publishers", p).value.get
  }

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

    var book = new Book()
    book.getAuthors().add(a);
    book.setPublisher(p);
    book.setTitle("Book 1");
    book.setIsbn("123456789X");
    book.setPublicationYear(1988);

    book = post("/api/books", book).value.get
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

  }

  "A non-existing book" should "not be discoverable" in {
    var response = get("/api/books/123", classOf[Book])
    response.status should be(STATUS_NOT_FOUND)
    response.errors.get.getMessage() should be("Book with id 123 not found")
  }

  "Books" should "be available in a public listing" in {
    val a = newAuthor("Author 1")
    val coAuthor = newAuthor("Coauthor")
    val p = newPublisher("Publisher 1")
    var book = new Book()
    book.getAuthors().add(a)
    book.setPublisher(p)
    book.setTitle("Book 1")
    book.setIsbn("123456789X")
    book.setPublicationYear(1988)

    post("/api/books", book);

    var response = get("/api/books", new TypeReference[java.util.List[Book]] {})
    var books = response.value.get
    books.size should be(1)
    books.get(0).getTitle() should be("Book 1")

    book = new Book();
    book.getAuthors().add(a);
    book.getAuthors().add(coAuthor);
    book.setPublisher(p);
    book.setTitle("Book 2");
    book.setIsbn("123456999X");
    book.setPublicationYear(2014);

    post("/api/books", book);

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
    var book = new Book()
    book.getAuthors().add(a)
    book.setPublisher(p)
    book.setTitle("Book 1")
    book.setIsbn("123456789X")
    book.setPublicationYear(1988)

    post("/api/books", book);
    book = new Book();
    book.getAuthors().add(a);
    book.getAuthors().add(coAuthor);
    book.setPublisher(p);
    book.setTitle("Book 2");
    book.setIsbn("123456999X");
    book.setPublicationYear(2014);
    post("/api/books", book);

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
    var books = get("/api/books/search/unexistable", classOf[ValidationErrorsDTO])
  }

}