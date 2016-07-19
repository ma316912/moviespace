package com.moviespace.app.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.moviespace.app.service.util.AppConstants;
import com.moviespace.app.service.util.AppUtil;
import com.moviespace.app.web.rest.dto.MovieDTO;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TmdbFind.ExternalSource;
import info.movito.themoviedbapi.model.Collection;
import info.movito.themoviedbapi.model.CollectionInfo;
import info.movito.themoviedbapi.model.Company;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi.MediaType;
import info.movito.themoviedbapi.model.people.Person;

@Service
public class GlobalSearchService {

	
private final Logger log = LoggerFactory.getLogger(GlobalSearchService.class);
    
    @Inject
    private TmdbApi tmdbApi;
    
    private TmdbSearch search;
    
    @Inject
    public GlobalSearchService(final TmdbApi tmdbApi) {
    	this.tmdbApi = tmdbApi;
    	search = tmdbApi.getSearch();
    }
    
	public List<MovieDb> searchMovie(String query) {
		List<MovieDb> resultMovies = search.searchMovie(query, 0, AppConstants.Language.EN.value(), false, 0).getResults();
		if(!AppUtil.isEmptyOrNull(resultMovies)) {
			resultMovies.stream().filter(movie -> movie.getMediaType()==MediaType.MOVIE).collect(Collectors.toList());
			resultMovies.stream().forEach(movie -> {
				MovieDTO movieDTO = movieDbToMovieDTO(movie);
			});
		}
		return resultMovies;
	}
	
	private MovieDTO movieDbToMovieDTO(MovieDb movie) {
		MovieDTO dto = new MovieDTO();
		dto.setTitle(movie.getOriginalTitle());
		dto.setBackdropPath(AppUtil.resolveImageUrl(movie.getBackdropPath(),AppConstants.BackdropSize.W300.value()));
		dto.setPosterPath(AppUtil.resolveImageUrl(movie.getPosterPath(),AppConstants.PosterSize.W342.value()));
		dto.setVoteAverage(movie.getVoteAverage());
		dto.setVoteCount(movie.getVoteCount());
		dto.setExternalId(movie.getId());
		dto.setOverview(movie.getOverview());
		return dto;
	}


	public List<Person> searchPerson(String query) {
		return search.searchPerson(query, false, 0).getResults();
	}
	
	
	public List<Company> searchCompany(String query) {
		return search.searchCompany(query, 0).getResults();
	}
	
	public List<Collection> searchCollection(String query) {
		return search.searchCollection(query, AppConstants.Language.EN.value(), 0).getResults();
	}
}
