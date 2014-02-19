package com.github.ggeorgovassilis.webshop.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.ggeorgovassilis.webshop.model.Animal;

/**
 * Persistence for herd animals. Only one (global) herd supported
 * @author George Georgovassilis
 *
 */
public interface AnimalDao extends JpaRepository<Animal, String>{

}
