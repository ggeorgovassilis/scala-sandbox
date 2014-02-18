package com.github.ggeorgovassilis.webshop.model

import org.springframework.stereotype.Component

import scala.collection.JavaConversions._
/**
 * Models herd production (the production logic / 'physics' of production)
 * @author george georgovassilis
 *
 */
@Component
class ProductionLogic {

  	/**
	 * Calendar days in an animal year
	 */
	val daysPerAnimalYear = 100;
	
	/**
	 * Animals can be shorn if at least this old
	 */
	val minimumAgeForShearingInDays:Double = 1*daysPerAnimalYear;

	/**
	 * An animal is removed from the heard once it is so old
	 */
	val maximumAnimalAgeInDays = 10*daysPerAnimalYear;
	
	/**
	 * Convert days to animal years
	 * @param days
	 * @return
	 */
	def years(days:Int) = days.toDouble/daysPerAnimalYear.toDouble;

	/**
	 * Convert animal years to days. Rounding behavior is unspecified.
	 * @param years
	 * @return
	 */
	def days(years:Double) = (years*daysPerAnimalYear.toDouble).toInt;

	/**
	 * Get animal age as it will be on a certain day in the future. This may return
	 * ages past the animal's nominal life expectancy
	 * 
	 * @param animal
	 *            Animal to compute age of
	 * @param day
	 *            Positive number. So many days from now in the future
	 * @return Age in animal years
	 */
	def getAnimalAgeInYearsOnDay(animal: Animal, day: Int) = years(animal.age + day);

	/**
	 * Return the numbers of productive days an animal has remaining at a certain target day
	 * @param animal
	 * @param day
	 * @return
	 */
	def getProductiveDaysRemainingFor(animal: Animal, day: Int) = {
		val daysRemaining = maximumAnimalAgeInDays - animal.age - day;
		if (daysRemaining<0)
			0
		else daysRemaining
	}

	/**
	 * Get liters that can be milked for an animal on a certain day
	 * @param animal
	 * @param day
	 * @return
	 */
	def getLitersMilkedByAnimalOnDay(animal: Animal, day: Int) = {
		val ageAtTargetDay = animal.age + day
		if (ageAtTargetDay >= daysPerAnimalYear*maximumAnimalAgeInDays)
			0
		else 50.0 - ageAtTargetDay * 0.03
	}

	/**
	 * Get the total quantity milked by an animal until (excluding) day 'day'
	 * @param animal
	 * @param day positive number
	 * @return
	 */
	def getTotalLitersMilkedByAnimalUntilDay(animal: Animal, day: Int) = {
		val daysElapsed = day - 1
		if (daysElapsed < 0) 0
		val lastDayOfMilking = Math.min(daysElapsed, getProductiveDaysRemainingFor(animal, 0))
		val daysOfMilking = lastDayOfMilking+1
		val outputToday = getLitersMilkedByAnimalOnDay(animal, 0)
		val outputOnLastDay = getLitersMilkedByAnimalOnDay(animal, lastDayOfMilking)
		0.5*(outputToday+outputOnLastDay)*daysOfMilking.toDouble
	}

	/**
	 * Determine whether an animal can be shorn on a specified day
	 * @param animal
	 * @param day
	 * @return
	 */
	def canShearOn(animal: Animal, day: Int):Boolean = {
		val ageAtTargetDay = animal.age + day
		// too young or too old?
		if (ageAtTargetDay < minimumAgeForShearingInDays || ageAtTargetDay >= maximumAnimalAgeInDays)
			return false
		val daysSinceLastShearing = ageAtTargetDay - animal.ageLastShaved
		val canShearEverySoManyDays = 8.0 + ageAtTargetDay.toDouble * 0.01
		if (canShearEverySoManyDays > daysSinceLastShearing)
			return false
		return true
	}

	/**
	 * Gets the herd milk output at a certain target date assuming that every animal is milked
	 * daily
	 * @param herd
	 * @param day
	 * @return
	 */
	def getMilkOutputAtDate(herd: Herd, day: Int) = {
		var sum:Double = 0
		herd.animals.foreach(animal => sum=sum+getTotalLitersMilkedByAnimalUntilDay(animal, day))
		sum
	}

	/**
	 * Gets the herd wool output (in units of shearing sessions) at a certain target date assuming that the animal is shorn
	 * at the earliest possible time
	 * @param animal
	 * @param day
	 * @return
	 */
	def getWoolOutputAtDate(animal: Animal, day:Int) = {
		// TODO: replace this implementation with an efficient one for large values of 'day'
	  val copy = animal.clone()
		var units = 0
		for (d <- 0 until day)
			if (canShearOn(copy, d)) {
				copy.ageLastShaved = copy.age+d
				units = units + 1
			}
		units
	}
	
	/**
	 * Gets the herd wool output (in units of shearing sessions) at a certain target date assuming that every animal is shorn
	 * at the earliest possible time
	 * @param herd
	 * @param day
	 * @return
	 */
	def getWoolOutputAtDate(herd: Herd, day: Int):Int = {
		var units = 0
		herd.animals.foreach(animal => {
		  units=units+getWoolOutputAtDate(animal, day)
		  })
		units
	}

}