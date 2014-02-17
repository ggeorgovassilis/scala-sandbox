package com.github.ggeorgovassilis.webshop

import scala.collection.mutable.Stack
import org.springframework.beans.factory.annotation.Autowired
import com.github.ggeorgovassilis.webshop.dao.HerdDao
import org.junit.Before
import com.github.ggeorgovassilis.webshop.model.Herd
import com.github.ggeorgovassilis.webshop.model.Animal
import com.github.ggeorgovassilis.webshop.model.Production
import org.springframework.test.context.TestContextManager
import com.github.ggeorgovassilis.webshop.service.SupplierService
import com.github.ggeorgovassilis.webshop.dto.AnimalDTO
import com.github.ggeorgovassilis.webshop.dto.OrderDTO
import com.github.ggeorgovassilis.webshop.dto.StockDTO

/**
 * Tests the webservice which provides access to the herd database and production functions
 * @author George Georgovassilis
 *
 */
class WebServiceTest extends BaseScalaTest {

@Autowired val service:SupplierService = null;

new TestContextManager(this.getClass()).prepareTestInstance(this)

"The herd stock" should "be on day 13 1104.48 lt milk and 3 hides" in {
	val stock = service.getStock(13)
	stock.getMilk() should be (1104.48)
	stock.getSkins() should be (3)
}

"Betty-1" should "be in the herd and have aged by 13 days on day 13" in {
	val herd = service.getHerd(13);
	val betty1 = herd.getAnimals().get(0)

	herd.getAnimals().size() should be (3)
	betty1.getName() should be ("Betty-1")
	betty1.getAge() should be (4.13)
	betty1.getAgeLastShaved() should be (0)
}

"Betty-1" should "have been shaved at age 3.9 after that has been committed to the database" in {
	service.updateAnimal("Betty-1", 4, 3.9);
	val herd = service.getHerd(13);
	val betty1 = herd.getAnimals().get(0)
		
	herd.getAnimals().size() should be (3);
	betty1.getName() should be ("Betty-1")
	betty1.getAge() should be (4.13)
	betty1.getAgeLastShaved() should be (3.9)
}

"A small order" should "be placed on day 13 and fully serviced" in {
	val order = new OrderDTO()
	val	stock = new StockDTO()
	stock.setMilk(1100);
	stock.setSkins(3);
	order.setCustomer("customer 1");
	order.setOrder(stock);
	
	val result = service.placeOrder(order, 13);
	result.getStatusCode().value() should be (201)
	result.getBody().getMilk() should be (1100)
	result.getBody().getSkins() should be (3)
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
}