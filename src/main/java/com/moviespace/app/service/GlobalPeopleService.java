package com.moviespace.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.moviespace.app.service.util.AppUtil;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.ArtworkType;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonPeople;

@Service
public class GlobalPeopleService {

	
    private final Logger log = LoggerFactory.getLogger(GlobalPeopleService.class);
        
    @Inject
    Environment env;
    
    private TmdbApi tmdbApi;
    
    private TmdbPeople people;
    
    @Inject
    public GlobalPeopleService(Environment env) {
    	this.env = env;
		tmdbApi = new TmdbApi(env.getProperty("ws.tmdb.api_key"));
		people = tmdbApi.getPeople();
	}
	
    
    public PersonPeople getPersonById(Integer id) {
    	return people.getPersonInfo(id);
    }
    
    public List<Artwork> getImagesForPersonById(Integer id) {
    	List<Artwork> images = people.getPersonImages(id);
    	List<Artwork> profileImages = new ArrayList<>();
    	if(!AppUtil.isEmptyOrNull(images)) {
    		profileImages = images.stream().filter(a -> a.getArtworkType().equals(ArtworkType.PROFILE)).collect(Collectors.toList());
    	}
    	return profileImages;
    }
    
    
    public List<Person> getPopularPeople() {
    	List<Person> popularPeople = people.getPersonPopular(null).getResults();
    return (popularPeople == null ? new ArrayList<>() : popularPeople);	
    }
    
    public List<PersonCredit> getCastCreditByPersonId(Integer id) {
    	return people.getPersonCredits(id).getCast();
    }
    
    public List<PersonCredit> getCrewCreditByPersonId(Integer id) {
    	return people.getPersonCredits(id).getCrew();
    }
    
    
    
}
