package com.github.ggeorgovassilis.webshop.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.ggeorgovassilis.webshop.model.Commodity;

public interface CommodityDao extends JpaRepository<Commodity, String>{

}
