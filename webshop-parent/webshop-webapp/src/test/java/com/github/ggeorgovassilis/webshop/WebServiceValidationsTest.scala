package com.github.ggeorgovassilis.webshop
import org.springframework.test.context.TestContextManager
import com.github.ggeorgovassilis.webshop.dto.OrderDTO
import com.github.ggeorgovassilis.webshop.dto.StockDTO
import scala.collection.JavaConversions._
import org.springframework.http.HttpStatus
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional
import javax.xml.bind.ValidationException
import javax.validation.ConstraintViolationException

/**
 * Verifies that the web service will reject wrong input for the right reasons
 * @author George Georgovassilis
 *
 */
class WebServiceValidationsTest extends BaseScalaTest {


  new TestContextManager(this.getClass()).prepareTestInstance(this)

  "The service" should "return a 404 for non existing orders" in {
    val response = service.findOrder("123")
    response.getStatusCode() should be(HttpStatus.NOT_FOUND)
  }

  "The service" should "not return herds for negative days" in {
    val herd = service.getHerd(-1)
    herd.getStatusCode() should be(HttpStatus.NOT_FOUND)
  }

  

  "The service" should "not allow orders without customer names" in {

    val order = new OrderDTO()
    val stock = new StockDTO()
    stock.setMilk(1)
    stock.setSkins(1)
    order.setOrder(stock)
    val e = intercept[ConstraintViolationException]{
    	service.placeOrder(order,2)
    }
    val error = e.getConstraintViolations().iterator().next()
    (error.getMessage()) should be("may not be null")
    (error.getPropertyPath().toString()) should be("customer")

    // alternative spelling of the two lines above (just for the fun of it): in scala you can work with tuples :)
    (error.getMessage(), error.getPropertyPath().toString()) should be(("may not be null","customer"))

  }

    "The service" should "not allow orders for negative quantities" in {

    val order = new OrderDTO();
    val stock = new StockDTO();
    stock.setMilk(-1)
    stock.setSkins(1)
    order.setCustomer("mr x")
    order.setOrder(stock)
    val e = intercept[ConstraintViolationException]{
    	service.placeOrder(order,2)
    }
    val error = e.getConstraintViolations().iterator().next()
    (error.getMessage()) should be("must be greater than or equal to 0")
    (error.getPropertyPath().toString()) should be("milk")

  }

}