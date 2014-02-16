package com.github.ggeorgovassilis.webshop.supplierwebservice.model;

import org.springframework.stereotype.Component;

/**
 * Models herd production
 * @author george georgovassilis
 *
 */
@Component
public class Production {

	/**
	 * Calendar days in an animal year
	 */
	protected final int daysPerAnimalYear = 100;
	
	/**
	 * Animals can be shorn if at least this old
	 */
	protected final int minimumAgeForShearingInDays = 1*daysPerAnimalYear;

	/**
	 * An animal is removed from the heard once it is so old
	 */
	protected final int maximumAnimalAgeInDays = 10*daysPerAnimalYear;
	
	/**
	 * Convert days to animal years
	 * @param days
	 * @return
	 */
	public double years(int days) {
		return (double)days/(double)daysPerAnimalYear;
	}

	/**
	 * Convert animal years to days. Rounding behavior is unspecified.
	 * @param years
	 * @return
	 */
	public int days(double years) {
		return (int)(years*(double)daysPerAnimalYear);
	}

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
	public double getAnimalAgeInYearsOnDay(Animal animal, int day) {
		return years(animal.age + day);
	}

	/**
	 * Return the numbers of productive days an animal has remaining at a certain target day
	 * @param animal
	 * @param day
	 * @return
	 */
	protected int getProductiveDaysRemainingFor(Animal animal, int day) {
		int daysRemaining = maximumAnimalAgeInDays - animal.age - day;
		if (daysRemaining<0)
			return 0;
		return daysRemaining;
	}

	/**
	 * Get liters that can be milked for an animal on a certain day
	 * @param animal
	 * @param day
	 * @return
	 */
	public double getLitersMilkedByAnimalOnDay(Animal animal, int day) {
		int ageAtTargetDay = animal.age + day;
		if (ageAtTargetDay >= daysPerAnimalYear*maximumAnimalAgeInDays)
			return 0;
		return 50.0 - ageAtTargetDay * 0.03;
	}

	/**
	 * Get the total quantity milked by an animal until (excluding) day 'day'
	 * @param animal
	 * @param day positive number
	 * @return
	 */
	public double getTotalLitersMilkedByAnimalUntilDay(Animal animal, int day) {
		int daysElapsed = day - 1;
		if (daysElapsed < 0)
			return 0;
		int lastDayOfMilking = Math.min(daysElapsed, getProductiveDaysRemainingFor(animal, 0));
		int daysOfMilking = lastDayOfMilking+1;
		double outputToday = getLitersMilkedByAnimalOnDay(animal, 0);
		double outputOnLastDay = getLitersMilkedByAnimalOnDay(animal, lastDayOfMilking);
		return 0.5*(outputToday+outputOnLastDay)*(double)(daysOfMilking);
	}

	/**
	 * Determine whether an animal can be shorn on a specified day
	 * @param animal
	 * @param day
	 * @return
	 */
	public boolean canShearOn(Animal animal, int day) {
		int ageAtTargetDay = animal.age + day;
		// too young or too old?
		if (ageAtTargetDay < minimumAgeForShearingInDays || ageAtTargetDay >= maximumAnimalAgeInDays)
			return false;
		int daysSinceLastShearing = ageAtTargetDay - animal.getAgeLastShaved();
		double canShearEverySoManyDays = 8.0 + ageAtTargetDay * 0.01;
		if (canShearEverySoManyDays > daysSinceLastShearing)
			return false;
		return true;
	}

	/**
	 * Gets the herd milk output at a certain target date assuming that every animal is milked
	 * daily
	 * @param herd
	 * @param day
	 * @return
	 */
	public double getMilkOutputAtDate(Herd herd, int day) {
		double sum = 0;
		for (Animal animal : herd.getHerd())
			sum += getTotalLitersMilkedByAnimalUntilDay(animal, day);
		return sum;
	}

	/**
	 * Gets the herd wool output (in units of shearing sessions) at a certain target date assuming that the animal is shorn
	 * at the earliest possible time
	 * @param animal
	 * @param day
	 * @return
	 */
	public int getWoolOutputAtDate(Animal animal,int day) {
		// TODO: replace this implementation with an efficient one for large values of 'day'
		Animal copy = animal.clone();
		int units = 0;
		for (int d=0;d<day;d++)
			if (canShearOn(copy, d)) {
				copy.ageLastShaved = copy.age+d;
				units++;
			}
		return units;
	}
	
	/**
	 * Gets the herd wool output (in units of shearing sessions) at a certain target date assuming that every animal is shorn
	 * at the earliest possible time
	 * @param herd
	 * @param day
	 * @return
	 */
	public int getWoolOutputAtDate(Herd herd, int day) {
		int units = 0;
		for (Animal animal:herd.getHerd())
			units+=getWoolOutputAtDate(animal, day);
		return units;
	}
}
