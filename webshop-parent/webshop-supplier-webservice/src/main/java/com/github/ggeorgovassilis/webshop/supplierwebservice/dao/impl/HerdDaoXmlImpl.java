package com.github.ggeorgovassilis.webshop.supplierwebservice.dao.impl;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.ggeorgovassilis.webshop.supplierwebservice.dao.HerdDao;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Animal;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Herd;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Production;

/**
 * Imports herds from XML.Identifiers are xml file names.
 * @author george georgovassilis
 *
 */
@Repository
public class HerdDaoXmlImpl implements HerdDao {

	@Autowired
	protected Production production;
	
	@Override
	public Herd find(String identifier) {
		try {
			File file = ResourceUtils.getFile(identifier);
			Herd herd = new Herd();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(file);
			
			NodeList animals = document.getElementsByTagName("labyak");
			for (int i=0;i<animals.getLength();i++) {
				Element e = (Element)animals.item(i);
				String name = e.getAttribute("name");
				int age = production.days(Double.parseDouble(e.getAttribute("age")));
				//String gender = e.getAttribute("gender");
				Animal animal = new Animal(name, age, 0);
				herd.getHerd().add(animal);
			}
			return herd;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
