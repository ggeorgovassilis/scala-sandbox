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
import org.springframework.web.context.WebApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import javax.servlet.ServletContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import com.github.ggeorgovassilis.webshop.service.LibraryService

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(
  locations = Array("classpath:webshop/application-context.xml", "classpath:webshop/standalone-environment-context.xml"))
@Transactional
@TransactionConfiguration(defaultRollback = true)
abstract class BaseScalaTest extends FlatSpec with GivenWhenThen with ShouldMatchers with BeforeAndAfter {

  @Autowired var service: LibraryService = null
  
  new TestContextManager(this.getClass()).prepareTestInstance(this)

}