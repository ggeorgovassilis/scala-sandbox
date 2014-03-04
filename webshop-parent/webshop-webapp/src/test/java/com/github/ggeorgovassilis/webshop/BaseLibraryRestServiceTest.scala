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

class BaseLibraryRestServiceTest extends BaseScalaWebserviceTest {

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
  
  def newBook(title:String, isbn:String, publicationYear:Integer, publisher:Publisher, availability:Integer, authors:Author*):Book={
    var book = new Book()
    book.getAuthors().addAll(authors);
    book.setPublisher(publisher)
    book.setTitle(title)
    book.setIsbn(isbn)
    book.setPublicationYear(publicationYear)
    book.setAvailability(availability);
    post("/api/books", book).value.get
  }

}