package com.github.ggeorgovassilis.webshop
import org.scalatest.GivenWhenThen
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.springframework.test.context.TestContextManager
import org.scalatest.BeforeAndAfter
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.context.WebApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import com.github.ggeorgovassilis.webshop.service.SupplierService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import javax.servlet.ServletContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(classOf[SpringJUnit4ClassRunner])
@WebAppConfiguration
@ContextConfiguration(
  locations = Array("file:src/main/webapp/WEB-INF/spring-servlet.xml", "classpath:webshop/application-context.xml", "classpath:webshop/standalone-environment-context.xml"))
@Transactional
@TransactionConfiguration(defaultRollback = true)
abstract class BaseScalaTest extends FlatSpec with GivenWhenThen with ShouldMatchers with BeforeAndAfter {

  @Autowired var service: SupplierService = null
  
  new TestContextManager(this.getClass()).prepareTestInstance(this)

  before {
  }

}