package com.github.ggeorgovassilis.webshop.service.dao;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.ggeorgovassilis.webshop.model.Animal;
import com.github.ggeorgovassilis.webshop.model.Herd;
import com.github.ggeorgovassilis.webshop.service.logic.ProductionLogic;

/**
 * Imports herds from XML. Identifiers are xml file names.
 * @author george georgovassilis
 *
 */
@Repository
public class HerdDao {

	@Autowired
	protected ProductionLogic production;
	
	@Value("${herdXmlElementName}")
	protected String xmlElementNameString;
	
	public Herd find(String identifier) {
		try {
			File file = ResourceUtils.getFile(identifier);
			Herd herd = new Herd();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(file);
			
			NodeList animals = document.getElementsByTagName(xmlElementNameString);
			for (int i=0;i<animals.getLength();i++) {
				Element e = (Element)animals.item(i);
				String name = e.getAttribute("name");
				int age = production.days(Double.parseDouble(e.getAttribute("age")));
				//String gender = e.getAttribute("gender");
				Animal animal = new Animal(name, age, 0);
				herd.getAnimals().add(animal);
			}
			return herd;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
