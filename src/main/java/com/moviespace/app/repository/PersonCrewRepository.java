package com.moviespace.app.repository;

import com.moviespace.app.domain.PersonCrew;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PersonCrew entity.
 */
@SuppressWarnings("unused")
public interface PersonCrewRepository extends JpaRepository<PersonCrew,Long> {

	PersonCrew findOneByExternalId(Integer id);
	List<PersonCrew> findByCreditsId(Long id);
}
