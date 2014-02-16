package com.github.ggeorgovassilis.webshop.supplierwebservice;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test support for further unit tests. Loads the main application context
 * but overrides database settings to use an in-memory database.
 * Any changes to the database are rolled back after each test method.
 * @author George Georgovassilis
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:webservice/application-context.xml", "classpath:webservice/environment-context.xml"})
@Transactional
@TransactionConfiguration(defaultRollback=true)
abstract class BaseTest {
	


}
