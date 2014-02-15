package com.github.ggeorgovassilis.webshop.supplierwebservice.service.impl;

import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Herd;
import com.github.ggeorgovassilis.webshop.supplierwebservice.model.Stock;
import com.github.ggeorgovassilis.webshop.supplierwebservice.service.SupplierService;

public class SupplierServiceImpl implements SupplierService{

	protected Herd herd;
	protected int today;
	
	public void setDay(int day) {
		this.today = day;
	}
	
	public void setHerd(Herd herd) {
		this.herd = herd;
	}
	
	@Override
	public Stock getStock(int daysFromNow) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Herd getHerd(int daysFromNow) {
		// TODO Auto-generated method stub
		return null;
	}

}
