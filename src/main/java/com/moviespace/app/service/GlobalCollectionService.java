package com.moviespace.app.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.moviespace.app.service.util.AppConstants;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbCollections;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.CollectionInfo;

@Service
public class GlobalCollectionService {

private final Logger log = LoggerFactory.getLogger(GlobalCollectionService.class);
    
    @Inject
    Environment env;
    
    private TmdbApi tmdbApi;
    
    private TmdbCollections collections;
    
    @Inject
    public GlobalCollectionService(Environment env) {
    	this.env = env;
		tmdbApi = new TmdbApi(env.getProperty("ws.tmdb.api_key"));
		collections = tmdbApi.getCollections();
	}
    
    public CollectionInfo getCollectionById(Integer id) {
    	return collections.getCollectionInfo(id, AppConstants.Language.EN.value());
    }
	
    public List<Artwork> getImagesByCollectionId(Integer id) {
    	return collections.getCollectionImages(id, AppConstants.Language.EN.value());
    }
    
}
