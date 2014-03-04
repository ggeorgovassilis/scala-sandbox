package com.github.ggeorgovassilis.webshop.support
import org.scalatest.GivenWhenThen
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfter
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
abstract class BaseScalaTest extends FlatSpec with GivenWhenThen with ShouldMatchers with BeforeAndAfter {

  
  //new TestContextManager(this.getClass()).prepareTestInstance(this)

}