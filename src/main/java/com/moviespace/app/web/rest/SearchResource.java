package com.moviespace.app.web.rest;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.moviespace.app.service.GlobalCollectionService;
import com.moviespace.app.service.GlobalSearchService;
import com.moviespace.app.web.rest.dto.MovieDTO;

import info.movito.themoviedbapi.model.Collection;
import info.movito.themoviedbapi.model.CollectionInfo;
import info.movito.themoviedbapi.model.Company;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.Person;

/**
 * REST controller for managing Movie.
 */
@RestController
@RequestMapping("/api")
public class SearchResource {

    private final Logger log = LoggerFactory.getLogger(SearchResource.class);
    
    @Inject
    private GlobalSearchService searchService;
    
    @Inject
    private GlobalCollectionService globalCollectionService;

    @RequestMapping(value = "/search/movie",params={"query"},
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MovieDb>> getMoviesByQuery(@RequestParam String query) {
        log.debug("REST request to get Movie : {}", query);
        List<MovieDb> results = searchService.searchMovie(query);
        
        return Optional.ofNullable(results)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @RequestMapping(value = "/search/person",params={"query"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<Person>> getPersonsByQuery(@RequestParam String query) {
            log.debug("REST request to get Company : {}", query);
            List<Person> results = searchService.searchPerson(query);
            
            return Optional.ofNullable(results)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    @RequestMapping(value = "/search/company",params={"query"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<Company>> getCompaniesByQuery(@RequestParam String query) {
            log.debug("REST request to get Company : {}", query);
            List<Company> results = searchService.searchCompany(query);
            
            return Optional.ofNullable(results)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    @RequestMapping(value = "/search/collection",params={"query"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<Collection>> getCollectionsByQuery(@RequestParam String query) {
            log.debug("REST request to get Collection : {}", query);
            List<Collection> results = searchService.searchCollection(query);
            
            return Optional.ofNullable(results)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    @RequestMapping(value = "/search/collection/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<CollectionInfo> getCollectionInfoById(@PathVariable Integer id) {
            log.debug("REST request to get Collection Info by : {}", id);
            CollectionInfo results = globalCollectionService.getCollectionById(id);
            
            return Optional.ofNullable(results)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    
}
