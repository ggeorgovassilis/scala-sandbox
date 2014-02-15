package com.github.ggeorgovassilis.webshop.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.ggeorgovassilis.webshop.model.Availability;
import com.github.ggeorgovassilis.webshop.model.Commodity;

public interface AvailabilityDao extends JpaRepository<Availability, Long>{

	@Query("select a from Availability a where a.commodity = ?1")
	Availability findAvailabilityWithCommodity(Commodity commodity);
}
