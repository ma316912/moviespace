package com.moviespace.app.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.moviespace.app.service.util.AppConstants;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbGenre;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;

@Service
public class GlobalGenreService {

private final Logger log = LoggerFactory.getLogger(GlobalGenreService.class);
    
    @Inject
    Environment env;
    
    private TmdbApi tmdbApi;
    
    private TmdbGenre genres;
    
    @Inject
    public GlobalGenreService(Environment env) {
    	this.env = env;
		tmdbApi = new TmdbApi(env.getProperty("ws.tmdb.api_key"));
		genres = tmdbApi.getGenre();
	}
	
    
    public List<Genre> getGenres() {
    	return genres.getGenreList(AppConstants.Language.EN.value());
    }
    
    public List<MovieDb> getMoviesByGenre(Integer id) {
    	return genres.getGenreMovies(id, AppConstants.Language.EN.value(), 0, false).getResults();
    }
    
}
