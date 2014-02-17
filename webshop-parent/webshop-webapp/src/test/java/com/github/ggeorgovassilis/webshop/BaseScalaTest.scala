package com.github.ggeorgovassilis.webshop

import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.matchers.ShouldMatchers
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.scalatest.BeforeAndAfter

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(
    locations = Array("classpath:webshop/application-context.xml", "classpath:webshop/standalone-environment-context.xml"))
@Transactional
@TransactionConfiguration(defaultRollback=true)
abstract class BaseScalaTest extends FlatSpec with GivenWhenThen with ShouldMatchers with BeforeAndAfter  {

   new TestContextManager(this.getClass()).prepareTestInstance(this)
}