package com.moviespace.app.repository;

import com.moviespace.app.domain.PersonCast;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PersonCast entity.
 */
@SuppressWarnings("unused")
public interface PersonCastRepository extends JpaRepository<PersonCast,Long> {

	PersonCast findOneByExternalId(Integer id);
	List<PersonCast> findByCreditsId(Long id);
	
	List<PersonCast> findByPersonId(Long id);
	
}
