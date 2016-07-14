package com.moviespace.app.repository;

import com.moviespace.app.domain.Person;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
public interface PersonRepository extends JpaRepository<Person,Long> {
	
	Person findOneByExternalId(Integer id);
	
    @Query("select count(person) from Person person")
    Integer findTotalPeopleCount();
	
}
