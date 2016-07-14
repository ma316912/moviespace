package com.moviespace.app.config;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import info.movito.themoviedbapi.TmdbApi;

@Configuration
public class TmdbConfiguration {

	private final Logger log = LoggerFactory.getLogger(TmdbConfiguration.class);

    @Inject
    private Environment env;
	
    
    @Bean
    public TmdbApi tmdbApi() {
    	TmdbApi api = new TmdbApi(env.getProperty("ws.tmdb.api_key"));
    	if(api!=null)
    		log.info("Instantiated TMDB Connection");
   	return api;
    }
    
}
