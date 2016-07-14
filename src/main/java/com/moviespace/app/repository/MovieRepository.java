package com.moviespace.app.repository;

import com.moviespace.app.domain.Genre;
import com.moviespace.app.domain.Movie;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Movie entity.
 */
@SuppressWarnings("unused")
public interface MovieRepository extends JpaRepository<Movie,Long> {
	
	Movie findOneByExternalId(Integer id);
	
    @Query("select distinct movie from Movie movie left join fetch movie.genres left join fetch movie.productionCompanies left join fetch movie.productionCountries left join fetch movie.spokenLanguages")
    List<Movie> findAllWithEagerRelationships();

    @Query("select movie from Movie movie left join fetch movie.genres left join fetch movie.productionCompanies left join fetch movie.productionCountries left join fetch movie.spokenLanguages left join fetch movie.reviews where movie.id =:id")
    Movie findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select count(movie) from Movie movie")
    Integer findTotalMovieCount();
    
    Movie findByCreditsId(Long id);
    
    
/*    @Query("select movie from Movie movie left join fetch movie.genres where movie.genres.id=:id")
    List<Movie> findAllByGenre(Long id);
*/
    @Query("select DISTINCT movie from Movie movie left join fetch movie.genres genre where genre.id=:id")
    List<Movie> findAllByGenres(@Param("id")Long id);

    @Query("select DISTINCT movie from Movie movie left join fetch movie.credits credits left join fetch credits.casts cast where cast.person.id=:id")
    List<Movie> findAllByCast(@Param("id")Long id);
    
    @Query("select DISTINCT movie from Movie movie left join fetch movie.credits credits left join fetch credits.crews crew where crew.person.id=:id")
    List<Movie> findAllByCrew(@Param("id")Long id);
    
    @Query("select DISTINCT movie from Movie movie left join fetch movie.productionCompanies company where company.id=:id")
    List<Movie> findAllByCompany(@Param("id")Long id);
    
/*    @Query("select DISTINCT movie from Movie movie left join fetch movie.spo genre where genre.id=:id")
    List<Movie> findAllByLanguage(@Param("id")Long id);
*/    
    
    
}
