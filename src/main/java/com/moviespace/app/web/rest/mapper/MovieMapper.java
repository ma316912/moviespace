package com.moviespace.app.web.rest.mapper;

import com.moviespace.app.domain.*;
import com.moviespace.app.web.rest.dto.MovieDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Movie and its DTO MovieDTO.
 */
@Mapper(componentModel = "spring", uses = {LanguageMapper.class})
public interface MovieMapper {

    @Mapping(source = "credits.id", target = "creditsId")
    @Mapping(source = "belongsToCollection.id", target = "belongsToCollectionId")
    @Mapping(target = "backdropPath", expression = "java(com.moviespace.app.service.util.AppUtil.resolveImageUrl(movie.getBackdropPath(),com.moviespace.app.service.util.AppConstants.BackdropSize.W300.value()))")
    @Mapping(target = "posterPath", expression = "java(com.moviespace.app.service.util.AppUtil.resolveImageUrl(movie.getPosterPath(),com.moviespace.app.service.util.AppConstants.PosterSize.W342.value()))")
    MovieDTO movieToMovieDTO(Movie movie);

    List<MovieDTO> moviesToMovieDTOs(List<Movie> movies);

    @Mapping(target = "reviews", ignore = true)
    @Mapping(source = "creditsId", target = "credits")
    @Mapping(source = "belongsToCollectionId", target = "belongsToCollection")
    Movie movieDTOToMovie(MovieDTO movieDTO);

    List<Movie> movieDTOsToMovies(List<MovieDTO> movieDTOs);

    default Genre genreFromId(Long id) {
        if (id == null) {
            return null;
        }
        Genre genre = new Genre();
        genre.setId(id);
        return genre;
    }

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }

    default Language languageFromId(Long id) {
        if (id == null) {
            return null;
        }
        Language language = new Language();
        language.setId(id);
        return language;
    }
    
    default Review reviewFromId(Long id) {
        if (id == null) {
            return null;
        }
        Review review = new Review();
        review.setId(id);
        return review;
    }

    default Credits creditsFromId(Long id) {
        if (id == null) {
            return null;
        }
        Credits credits = new Credits();
        credits.setId(id);
        return credits;
    }

    default Collection collectionFromId(Long id) {
        if (id == null) {
            return null;
        }
        Collection collection = new Collection();
        collection.setId(id);
        return collection;
    }
}
