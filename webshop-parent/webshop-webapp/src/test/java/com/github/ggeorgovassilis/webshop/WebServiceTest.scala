package com.github.ggeorgovassilis.webshop
import org.springframework.test.context.TestContextManager
import com.github.ggeorgovassilis.webshop.dto.OrderDTO
import com.github.ggeorgovassilis.webshop.dto.StockDTO
import scala.collection.JavaConversions._
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import java.util.Date

/**
 * Tests the webservice which provides access to the herd database and production functions
 * @author George Georgovassilis
 *
 */
class WebServiceTest extends BaseScalaTest {

new TestContextManager(this.getClass()).prepareTestInstance(this)

before{
	service.importHerd();
}

"The herd stock" should "be on day 13 1104.48 lt milk and 3 hides" in {
	val stock = service.getStock(13)
	stock.getMilk() should be (1104.48)
	stock.getSkins() should be (3)
}

"Betty-1" should "be in the herd and have aged by 13 days on day 13" in {
	val herd = service.getHerd(13).getBody();
	val betty1 = herd.getHerd().get(0)

	herd.getHerd().size() should be (3)
	betty1.getName() should be ("Betty-1")
	betty1.getAge() should be (4.13)
	betty1.getAgeLastShaved() should be (0)
}

"Betty-1" should "have been shaved at age 3.9 after that has been committed to the database" in {
	service.updateAnimal("Betty-1", 4, 3.9);
	val herd = service.getHerd(13).getBody();
	val betty1 = herd.getHerd().get(0)
		
	herd.getHerd().size() should be (3);
	betty1.getName() should be ("Betty-1")
	betty1.getAge() should be (4.13)
	betty1.getAgeLastShaved() should be (3.9)
}

"A small order" should "be placed on day 13 and fully serviced" in {
	val day = 13;
	val order = new OrderDTO()
	val	stock = new StockDTO()
	stock.setMilk(1100);
	stock.setSkins(3);
	order.setCustomer("customer 1");
	order.setOrder(stock);
	
	val result = service.placeOrder(order, day);
	val receipt = result.getBody();
	result.getStatusCode().value() should be (201)
	receipt.getMilk() should be (1100)
	receipt.getSkins() should be (3)
	TestSupport.getDifferenceInDays(new Date(), receipt.getDate()) should be (day);
	receipt.getCustomerName() should be ("customer 1")
}

"A large order" should "be placed on day 14 and partially serviced" in {
	val order = new OrderDTO()
	val	stock = new StockDTO()
	stock.setMilk(1200);
	stock.setSkins(3);
	order.setCustomer("customer 1");
	order.setOrder(stock);
	
	val result = service.placeOrder(order, 14);
	result.getStatusCode().value() should be (206)
	result.getBody().getMilk() should be (0)
	result.getBody().getSkins() should be (3)
}

"A vast order" should "be placed on day 13 and not be serviced" in {
	val order = new OrderDTO()
	val	stock = new StockDTO()
	stock.setMilk(9999);
	stock.setSkins(9999);
	order.setCustomer("customer 1");
	order.setOrder(stock);
	
	val result = service.placeOrder(order, 14);
	result.getStatusCode().value() should be (404)
	result.getBody().getMilk() should be (0)
	result.getBody().getSkins() should be (0)
}

"An order that was placed" should "be retrievable with the receipt ID" in {
	val order = new OrderDTO()
	val	stock = new StockDTO()
	stock.setMilk(12);
	order.setCustomer("customer 1");
	order.setOrder(stock);
	
	val receipt = service.placeOrder(order, 14).getBody();
	val originalOrder = service.findOrder(receipt.getId()).getBody();
	
	originalOrder.getMilk() should be (12)
	TestSupport.getDifferenceInDays(new Date(), originalOrder.getDate()) should be (14)
	originalOrder.getCustomerName() should be ("customer 1")
}

}