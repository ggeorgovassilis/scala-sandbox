package com.github.ggeorgovassilis.webshop.supplierwebservice.model;

public class Configuration {

	protected final double daysPerAnimalYear = 100;
	protected final double minimumAgeForShearingInYears = 1;
	protected final double maximumAnimalAgeInYears = 10;

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
	public double getAnimalAgeInYears(Animal animal, int day) {
		return getAnimalAgeInDays(animal, day) / daysPerAnimalYear;
	}

	/**
	 * Get animal age as it will be on a certain day in the future. This may return
	 * ages past the animal's nominal life expectancy
	 * 
	 * @param animal
	 *            Animal to compute age of
	 * @param day
	 *            Positive number. So many days from now in the future
	 * @return Age in days
	 */
	public double getAnimalAgeInDays(Animal animal, int day) {
		return (animal.getAge() * daysPerAnimalYear + (double) day);
	}
	
	/**
	 * Return the numbers of productive days an animal has remaining at a certain target day
	 * @param animal
	 * @param day
	 * @return
	 */
	public int getProductiveDaysRemainingFor(Animal animal, int day) {
		double ageInDays = getAnimalAgeInDays(animal, 0);
		double daysRemaining = maximumAnimalAgeInYears*daysPerAnimalYear - ageInDays - day;
		return (int)daysRemaining;
	}

	/**
	 * Get liters that can be molken for an animal on a certain day
	 * @param animal
	 * @param day
	 * @return
	 */
	public double getLitersMolkenByAnimalOnDay(Animal animal, int day) {
		double ageAtTargetDay = getAnimalAgeInDays(animal, day);
		if (ageAtTargetDay >= maximumAnimalAgeInYears * daysPerAnimalYear)
			return 0;
		return 50.0 - ageAtTargetDay * 0.03;
	}

	public double getTotalLitersMolkenByAnimalUntilDay(Animal animal, int day) {
		int lastDayOfMilking = Math.min(day, getProductiveDaysRemainingFor(animal, 0));
		double outputToday = getLitersMolkenByAnimalOnDay(animal, 0);
		double outputOnLastDay = getLitersMolkenByAnimalOnDay(animal, lastDayOfMilking);
		return 0.5*(outputToday+outputOnLastDay)*(double)(lastDayOfMilking+1.0);
	}

	public boolean canShearOn(Animal animal, int day) {
		double ageAtTargetDay = getAnimalAgeInDays(animal, day);
		// too young or too old?
		if (ageAtTargetDay < minimumAgeForShearingInYears || ageAtTargetDay >= maximumAnimalAgeInYears)
			return false;
		double daysSinceLastShearing = ageAtTargetDay - animal.getAgeLastShaved();
		double canShearEverySoManyDays = 8.0 + ageAtTargetDay * 0.01;
		if (canShearEverySoManyDays < daysSinceLastShearing)
			return false;
		return true;
	}

	/**
	 * Gets the herd milk output at a certain target date assuming that every animal is molken
	 * daily
	 * @param herd
	 * @param day
	 * @return
	 */
	public double getMilkOutputAtDate(Herd herd, int day) {
		double sum = 0;
		for (Animal animal : herd.getHerd())
			sum += getTotalLitersMolkenByAnimalUntilDay(animal, day);
		return sum;
	}
}
