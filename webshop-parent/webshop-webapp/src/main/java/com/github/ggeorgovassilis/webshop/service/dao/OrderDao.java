package com.github.ggeorgovassilis.webshop.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.ggeorgovassilis.webshop.model.Order;

/**
 * Declares functions for persisting and accessing orders
 * @author george georgovassilis
 *
 */
public interface OrderDao extends JpaRepository<Order, String>{

}
