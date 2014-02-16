package com.github.ggeorgovassilis.webshop.supplierwebservice.application;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.github.ggeorgovassilis.webshop.supplierwebservice.dao.HerdDao;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Animal;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Herd;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Production;

/**
 * Predicts production and prints a herd overview for a certain day.
 * 
 * Usage:
 * 
 * ProductionPrediction path_to_herd_xml_file number_of_days_from_today
 * @author george georgovassilis
 *
 */
public class ProductionPrediction {

	public void predict(String pathToXml, int days) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"webshop/application-context.xml","webshop/standalone-environment-context.xml"});
		Production production = context.getBean(Production.class);
		HerdDao dao = context.getBean(HerdDao.class);
		Herd herd = dao.find(pathToXml);
		
		double milk = production.getMilkOutputAtDate(herd, days);
		int wool = production.getWoolOutputAtDate(herd, days);

		System.out.println("In Stock:");
		System.out.println(String.format("\t%1.3f liters of milk", milk));
		System.out.println("\t"+wool+" skins of wool");
		System.out.println("Herd:");
		for (Animal animal:herd.getAnimals())
			System.out.println(String.format("\t%s %1.2f years old",animal.getName(),production.getAnimalAgeInYearsOnDay(animal, days)));
	}

	public static void showUsage() {
		System.out.println("Usage:");
		System.out.println("ProductionPrediction path_to_herd_xml_file number_of_days_from_today");
		System.out.println();
		System.out.println("path_to_herd_xml can be a classpath (prefix with classpath:) or file path.");
		System.out.println("see herd.xml in test resources for an example");
		System.out.println();
		System.out.println("https://github.com/ggeorgovassilis/scala-sandbox/tree/master/webshop-parent");
	}

	public static boolean checkArguments(String... strings) {
		if (strings.length != 2) {
			return false;
		}
		String pathToXml = strings[0];
		int days = 0;
		try {
			days = Integer.parseInt(strings[1]);
			if (days < 0) {
				System.out.println("Days should be a positive integer");
				return false;
			}

		} catch (Exception e) {
			System.out.println("Please provide number of days.");
			return false;
		}
		return true;
	}

	public static void main(String... strings) {
		if (!checkArguments(strings)) {
			showUsage();
			System.exit(1);
		}
		ProductionPrediction pp = new ProductionPrediction();
		pp.predict(strings[0], Integer.parseInt(strings[1]));
	}
}
