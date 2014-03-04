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
class LoanBooksTest extends BaseLibraryRestServiceTest {

  "A client" should "be able to borrow an available book by providing his name" in{
    val a = newAuthor("Author 1")
    val p = newPublisher("Publisher 1")
    val book = newBook("Book 1", "123456789X", 1988, p, 1, a)
    val response = post[LoanedBook]("/api/borrow/"+book.getId()+"", new LoanedBook(), false, Map("clientName"->"test client"))
    response.status should be (STATUS_CREATED)
    response.value.get.getBook().getTitle() should be ("Book 1")
    response.value.get.getClientName() should be ("test client")
  }

    "A client" should "not be able to borrow an available book when hiding his name" in{
    val a = newAuthor("Author 1")
    val p = newPublisher("Publisher 1")
    val book = newBook("Book 1", "123456789X", 1988, p, 1, a)
    val response = post[LoanedBook]("/api/borrow/"+book.getId()+"", new LoanedBook(), false)
    response.status should be (STATUS_BAD)
  }

    "A client" should "be able to borrow a book that is not available or doesn't exist" in{
    val a = newAuthor("Author 1")
    val p = newPublisher("Publisher 1")
    val book = newBook("Book 1", "123456789X", 1988, p, 0, a)
    val response = post[LoanedBook]("/api/borrow/"+book.getId()+"", new LoanedBook(), false, Map("clientName"->"test client"))
    response.status should be (STATUS_NOT_FOUND)
  }

}