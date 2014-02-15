package com.github.ggeorgovassilis.webshop.service.impl;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ggeorgovassilis.webshop.model.Availability;
import com.github.ggeorgovassilis.webshop.model.Commodity;
import com.github.ggeorgovassilis.webshop.model.Supplier;
import com.github.ggeorgovassilis.webshop.persistence.AvailabilityDao;
import com.github.ggeorgovassilis.webshop.persistence.CommodityDao;
import com.github.ggeorgovassilis.webshop.persistence.SupplierDao;
import com.github.ggeorgovassilis.webshop.service.SupplierService;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	protected CommodityDao commodityDao;

	@Autowired
	protected SupplierDao supplierDao;

	@Autowired
	protected AvailabilityDao availabilityDao;

	@Autowired
	protected Validator validator;

	@Override
	public List<Commodity> getAllCommodities() {
		return commodityDao.findAll();
	}

	protected void validate(Object o) {
		Set<ConstraintViolation<Object>> violations = validator.validate(o);
		if (!violations.isEmpty()) {
			String message = "";
			String prefix = "";
			for (ConstraintViolation<?> v : violations) {
				message += prefix + v.getPropertyPath()+": "+v.getMessage();
				prefix=", ";
			}
			throw new ValidationException(message);
		}
	}

	@Override
	public Supplier persist(Supplier supplier) {
		validate(supplier);
		return supplierDao.saveAndFlush(supplier);
	}

	@Override
	public Commodity persist(Commodity commodity) {
		validate(commodity);
		commodity = commodityDao.save(commodity);
		Availability availability = findAvailability(commodity);
		// new commodities must be resitered with an availability of 0
		if (availability == null) {
			availability = new Availability();
			availability.setCommodity(commodity);
			availability.setQuantity(0);
			availabilityDao.saveAndFlush(availability);
		}
		return commodity;
	}

	@Override
	public Commodity findCommodity(String name) {
		return commodityDao.findOne(name);
	}

	@Override
	public Supplier findSupplier(String name) {
		return supplierDao.findOne(name);
	}

	@Override
	public Availability findAvailability(Commodity commodity) {
		return availabilityDao.findAvailabilityWithCommodity(commodity);
	}

	@Override
	public Availability modifyAvailability(Commodity commodity, int delta) {
		Availability availability = findAvailability(commodity);
		availability.setQuantity(availability.getQuantity() + delta);
		availability = availabilityDao.saveAndFlush(availability);
		return availability;
	}

}
