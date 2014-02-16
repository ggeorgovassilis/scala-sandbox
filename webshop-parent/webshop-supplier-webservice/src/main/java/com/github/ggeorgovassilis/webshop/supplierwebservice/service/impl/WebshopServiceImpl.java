package com.github.ggeorgovassilis.webshop.supplierwebservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.ggeorgovassilis.webshop.api.CommodityDTO;
import com.github.ggeorgovassilis.webshop.api.ProductCatalogue;
import com.github.ggeorgovassilis.webshop.supplierwebservice.service.SupplierService;

@Controller
@RequestMapping("/webshop")
public class WebshopServiceImpl implements ProductCatalogue{

	@Autowired
	protected SupplierService supplierService;
	@Autowired
	protected List<CommodityDTO> commodities;
	
	@RequestMapping("/commodities")
	@Override
	public @ResponseBody List<CommodityDTO> getCommodities() {
		return commodities;
	}

}
