package com.moviespace.app.repository;

import com.moviespace.app.domain.Genre;
import com.moviespace.app.domain.Movie;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Genre entity.
 */
@SuppressWarnings("unused")
public interface GenreRepository extends JpaRepository<Genre,Long> {
	Genre findByExternalId(Integer id);
	
	//@Query("select DISTINCT movie from Genre genre join fetch genre.movies movie where genre.id=:id")
	//Set<Movie> findMoviesByGenre(Long id);
	
}
