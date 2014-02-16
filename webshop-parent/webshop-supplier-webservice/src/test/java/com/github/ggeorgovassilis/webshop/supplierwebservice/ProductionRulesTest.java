package com.github.ggeorgovassilis.webshop.supplierwebservice;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.ggeorgovassilis.webshop.supplierwebservice.dao.HerdDao;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Animal;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Production;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Herd;

import static org.junit.Assert.*;
/**
 * Tests for production logic
 * @author george georgovassilis
 *
 */
public class ProductionRulesTest extends BaseTest{

	
	protected final double error = 0.01;

	@Autowired
	protected HerdDao herdDao;
	@Autowired
	protected Production production;

	protected Herd herd;
	protected Animal betty1;
	protected Animal betty2;
	protected Animal betty3;
	
	
	@Before
	public void setup() {
		herd = herdDao.find("classpath:herd.xml");
		betty1 = herd.getAnimals().get(0);
		betty2 = herd.getAnimals().get(1);
		betty3 = herd.getAnimals().get(2);
		//		betty1 = new Animal("Betty-1", 400, 0);
//		betty2 = new Animal("Betty-2", 800, 0);
//		betty3 = new Animal("Betty-3", 950, 0);
//		herd = new Herd();
//		herd.getHerd().add(betty1);
//		herd.getHerd().add(betty2);
//		herd.getHerd().add(betty3);
//		production = new Production();
	}
	
	/**
	 * Test animal age at day T+13
	 */
	@Test
	public void testAge() {
		final int day = 13;
		assertEquals(4.13, production.getAnimalAgeInYearsOnDay(betty1, day), error);
		assertEquals(8.13, production.getAnimalAgeInYearsOnDay(betty2, day), error);
		assertEquals(9.63, production.getAnimalAgeInYearsOnDay(betty3, day), error);
	}

	/**
	 * Test animal age at day T=0
	 */
	@Test
	public void testAgeToday() {
		final int day = 0;
		assertEquals(4, production.getAnimalAgeInYearsOnDay(betty1, day), error);
		assertEquals(8, production.getAnimalAgeInYearsOnDay(betty2, day), error);
		assertEquals(9.5, production.getAnimalAgeInYearsOnDay(betty3, day), error);
	}

	/**
	 * Test milk output for today. Since 'today' has not elapsed, the output should be 0
	 */
	@Test
	public void testMilkOutputOverInterval_sameDay() {
		assertEquals(0, production.getTotalLitersMilkedByAnimalUntilDay(betty1, 0), error);
	}

	/**
	 * Test milk output over an interval of just one day
	 */
	@Test
	public void testMilkOutputOverInterval_tomorrow() {
		assertEquals(38.0, production.getTotalLitersMilkedByAnimalUntilDay(betty1, 1), error);
	}

	/**
	 * Test milk output over an interval of a week
	 */
	@Test
	public void testMilkOutputOverInterval_week() {
		final int T = 7;
		double sum = 0;
		for (int i=0;i<T;i++)
			sum+=production.getLitersMilkedByAnimalOnDay(betty1, i);
		assertEquals(sum, production.getTotalLitersMilkedByAnimalUntilDay(betty1, T), error);
	}

	/**
	 * Test herd milk output in 13 days
	 */
	@Test
	public void testHerdMilkOutput_13_days() {
		final int day = 13;
		assertEquals(1104.48, production.getMilkOutputAtDate(herd, day), error);
	}

	/**
	 * Test herd milk output in 14 days
	 */
	@Test
	public void testHerdMilkOutput_14_days() {
		final int day = 14;
		assertEquals(1188.81, production.getMilkOutputAtDate(herd, day), error);
	}

	/**
	 * Test wool output in 13 days
	 */
	@Test
	public void testHerdWoolOutput_14_days() {
		final int day = 14;
		assertEquals(4, production.getWoolOutputAtDate(herd, day));
	}

	/**
	 * Test wool output today
	 */
	@Test
	public void testHerdWoolOutput_today() {
		final int day = 0;
		assertEquals(0, production.getWoolOutputAtDate(herd, day));
	}

	/**
	 * Test wool output tomorrow
	 */
	@Test
	public void testHerdWoolOutput_tomorrow() {
		final int day = 1;
		assertEquals(3, production.getWoolOutputAtDate(herd, day));
	}

}
