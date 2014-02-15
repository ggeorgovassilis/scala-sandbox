package com.github.ggeorgovassilis.webshop.supplierwebservice;

import org.junit.Before;
import org.junit.Test;

import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Animal;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Configuration;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Herd;

import static org.junit.Assert.*;
/**
 * Tests for production logic
 * @author george georgovassilis
 *
 */
public class ProductionRulesTest {

	protected final double error = 0.01;
	protected Animal betty1;
	protected Animal betty2;
	protected Animal betty3;
	protected Herd herd;
	protected Configuration configuration;
	
	@Before
	public void setup() {
		betty1 = new Animal("Betty-1", 4, 1);
		betty2 = new Animal("Betty-2", 8, 1);
		betty3 = new Animal("Betty-3", 9.5, 1);
		herd = new Herd();
		herd.getHerd().add(betty1);
		herd.getHerd().add(betty2);
		herd.getHerd().add(betty3);
		configuration = new Configuration();
	}
	
	/**
	 * Test animal age at day T+13
	 */
	@Test
	public void testAge() {
		final int day = 13;
		assertEquals(4.13, configuration.getAnimalAgeInYears(betty1, day), error);
		assertEquals(8.13, configuration.getAnimalAgeInYears(betty2, day), error);
		assertEquals(9.63, configuration.getAnimalAgeInYears(betty3, day), error);
	}

	/**
	 * Test animal age at day T=0
	 */
	@Test
	public void testAgeToday() {
		final int day = 0;
		assertEquals(4, configuration.getAnimalAgeInYears(betty1, day), error);
		assertEquals(8, configuration.getAnimalAgeInYears(betty2, day), error);
		assertEquals(9.5, configuration.getAnimalAgeInYears(betty3, day), error);
	}

	/**
	 * Test milk output over an interval of just one day
	 */
	@Test
	public void testMilkOutputOverInterval_sameDay() {
		final int T = 1;
		double sum = 0;
		for (int i=0;i<=T;i++)
			sum+=configuration.getLitersMolkenByAnimalOnDay(betty1, i);
		assertEquals(sum, configuration.getTotalLitersMolkenByAnimalUntilDay(betty1, T), error);
	}

	/**
	 * Test milk output over an interval of a week
	 */
	@Test
	public void testMilkOutputOverInterval_week() {
		final int T = 7;
		double sum = 0;
		for (int i=0;i<=T;i++)
			sum+=configuration.getLitersMolkenByAnimalOnDay(betty1, i);
		assertEquals(sum, configuration.getTotalLitersMolkenByAnimalUntilDay(betty1, T), error);
	}

	/**
	 * Test herd milk output in 13 days
	 */
	@Test
	public void testHerdMilkOutput() {
		final int day = 13;
		assertEquals(1104.48, configuration.getMilkOutputAtDate(herd, day), error);
	}
}
