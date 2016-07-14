package com.moviespace.app.repository;

import com.moviespace.app.domain.Company;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
public interface CompanyRepository extends JpaRepository<Company,Long> {

	Company findOneByExternalId(Integer id);
	
	@Query("Select count(company) from Company company")
	Integer findTotalCompanyCount();
	
}
