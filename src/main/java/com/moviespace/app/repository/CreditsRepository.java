package com.moviespace.app.repository;

import com.moviespace.app.domain.Credits;
import com.moviespace.app.domain.Movie;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Credits entity.
 */
@SuppressWarnings("unused")
public interface CreditsRepository extends JpaRepository<Credits,Long> {

	/*@Query("select credits from Credits credits left join fetch credits. left join fetch movie.productionCompanies left join fetch movie.productionCountries left join fetch movie.spokenLanguages left join fetch movie.reviews where movie.id =:id")
    Movie findOneWithEagerRelationships(@Param("id") Long id);*/
	
	Credits findOneByExternalId(Integer id);
	
	@Query("select credits from Credits credits where externalId=:id")
	Credits findByMovieId(@Param("id")Integer id);
	
}
