package com.github.ggeorgovassilis.webshop

import scala.collection.mutable.Stack
import org.springframework.beans.factory.annotation.Autowired
import com.github.ggeorgovassilis.webshop.dao.HerdDao
import org.junit.Before
import com.github.ggeorgovassilis.webshop.model.Herd
import com.github.ggeorgovassilis.webshop.model.Animal
import com.github.ggeorgovassilis.webshop.model.Production
import org.springframework.test.context.TestContextManager

class ProductionRulesTest extends BaseScalaTest {

@Autowired var herdDao: HerdDao = null;
@Autowired var production: Production = null;

var herd:Herd = null;
var betty1:Animal = null;
var betty2:Animal = null;
var betty3:Animal = null;

new TestContextManager(this.getClass()).prepareTestInstance(this)

before{
	herd = herdDao.find("classpath:customization/herd.xml");
	betty1 = herd.getAnimals().get(0);
	betty2 = herd.getAnimals().get(1);
	betty3 = herd.getAnimals().get(2);
}

"An animal" should "be today exactly at the age it was registered in the database with" in {
	val day = 0
	production.getAnimalAgeInYearsOnDay(betty1, day) should be (4)
	production.getAnimalAgeInYearsOnDay(betty2, day) should be (8)
	production.getAnimalAgeInYearsOnDay(betty3, day) should be (9.5)
}


"An animal" should "age according to the formula (ageToday + days)/days_in_animal_year" in {
	val day = 13
	production.getAnimalAgeInYearsOnDay(betty1, day) should be (4.13)
	production.getAnimalAgeInYearsOnDay(betty2, day) should be (8.13)
	production.getAnimalAgeInYearsOnDay(betty3, day) should be (9.63)
}

"An animal" should "not have produced any milk yet by today in the morning" in {
	val day = 0
	production.getTotalLitersMilkedByAnimalUntilDay(betty1, day) should be (0)
}


"An animal" should "produce an amount of milk for the duration of this day equal to the formula 50.0 - ageToday * 0.03;" in {
	val day = 1
	production.getTotalLitersMilkedByAnimalUntilDay(betty1, day) should be (38.0)
}

"An animal" should "produce a total amount of milk for the duration of a week equal to the formula Σ (n = 0..7) [50.0 - n * 0.03]" in {
	val T = 7
	var sum:Double = 0;
	for (t<- 0 until T){
		sum = sum + production.getLitersMilkedByAnimalOnDay(betty1, t);
	}
	sum should be (production.getTotalLitersMilkedByAnimalUntilDay(betty1, T));
}

"The herd" should "produce a total amount of milk until day 13 that equals to 1104.48 lt" in {
	val day = 13;
	production.getMilkOutputAtDate(herd, day) should be (1104.48);
}

"The herd" should "produce a total amount of milk until day 14 that equals to 1188.81 lt" in {
	val day = 14;
	production.getMilkOutputAtDate(herd, day) should be (1188.81);
}

"The herd" should "produce a total amount of wool until day 13 that equals to 3 hides" in {
	val day = 13;
	production.getWoolOutputAtDate(herd, day) should be (3);
}

"The herd" should "produce a total amount of wool until day 14 that equals to 4 hides" in {
	val day = 14;
	production.getWoolOutputAtDate(herd, day) should be (4);
}

"The herd" should "not produce any hides by 00:00 today" in {
	val day = 0;
	production.getWoolOutputAtDate(herd, day) should be (0);
}

}