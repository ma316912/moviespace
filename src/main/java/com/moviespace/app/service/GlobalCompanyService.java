package com.moviespace.app.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.moviespace.app.service.util.AppConstants;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbCompany;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbReviews;
import info.movito.themoviedbapi.model.Collection;
import info.movito.themoviedbapi.model.Company;
import info.movito.themoviedbapi.model.MovieDb;

@Service
public class GlobalCompanyService {

private final Logger log = LoggerFactory.getLogger(GlobalCompanyService.class);
    
    @Inject
    Environment env;
    
    private TmdbApi tmdbApi;
    
    private TmdbCompany companies;
    
    @Inject
    public GlobalCompanyService(Environment env) {
    	this.env = env;
		tmdbApi = new TmdbApi(env.getProperty("ws.tmdb.api_key"));
		companies = tmdbApi.getCompany();
	}
	
    
    public Company getCompanyById(Integer id) {
    	return companies.getCompanyInfo(id);
    }
    
    public List<Collection> getMoviesByCompanyId(Integer id) {
    	return companies.getCompanyMovies(id, AppConstants.Language.EN.value(),0).getResults();
    }
    
}
