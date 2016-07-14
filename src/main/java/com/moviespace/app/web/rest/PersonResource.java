package com.moviespace.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.moviespace.app.domain.Movie;
import com.moviespace.app.domain.Person;
import com.moviespace.app.service.MovieService;
import com.moviespace.app.service.PersonService;
import com.moviespace.app.service.util.AppConstants;
import com.moviespace.app.service.util.MovieSpaceException;
import com.moviespace.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Person.
 */
@RestController
@RequestMapping("/api")
public class PersonResource {

    private final Logger log = LoggerFactory.getLogger(PersonResource.class);
        
    @Inject
    private PersonService personService;
    
    @Inject
    private MovieService movieService;
    
    /**
     * POST  /people : Create a new person.
     *
     * @param person the person to create
     * @return the ResponseEntity with status 201 (Created) and with body the new person, or with status 400 (Bad Request) if the person has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/people",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Person> createPerson(@RequestBody Person person) throws URISyntaxException {
        log.debug("REST request to save Person : {}", person);
        if (person.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("person", "idexists", "A new person cannot already have an ID")).body(null);
        }
        Person result;
		try {
			result = personService.save(person);
		} catch (MovieSpaceException e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("person", "idexists", e.getMessage())).body(null);
		}
        return ResponseEntity.created(new URI("/api/people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("person", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /people : Updates an existing person.
     *
     * @param person the person to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated person,
     * or with status 400 (Bad Request) if the person is not valid,
     * or with status 500 (Internal Server Error) if the person couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/people",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) throws URISyntaxException {
        log.debug("REST request to update Person : {}", person);
        if (person.getId() == null) {
            return createPerson(person);
        }
        Person result;
		try {
			result = personService.save(person);
		} catch (MovieSpaceException e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("person", "idexists", e.getMessage())).body(null);
		}
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("person", person.getId().toString()))
            .body(result);
    }

    /**
     * GET  /people : get all the people.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of people in body
     */
    @RequestMapping(value = "/people",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Person> getAllPeople() {
        log.debug("REST request to get all People");
        return personService.findAll();
    }

    /**
     * GET  /people/:id : get the "id" person.
     *
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the person, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/people/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Person> getPerson(@PathVariable Long id) {
        log.debug("REST request to get Person : {}", id);
        Person person = personService.findOne(id);
        return Optional.ofNullable(person)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /people/:id : delete the "id" person.
     *
     * @param id the id of the person to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/people/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        log.debug("REST request to delete Person : {}", id);
        personService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("person", id.toString())).build();
    }
    
    
    /**
     * GET  /people/:id : get the "id" person.
     *
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the person, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/people/{id}/movies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Movie>> getMoviesByPersonId(@PathVariable Long id) {
        log.debug("REST request to get Person : {}", id);
        Person person = personService.findOne(id);
        List<Movie> movies = new ArrayList<>();
        if(person.getType().equals(AppConstants.PersonType.CAST.value())) {
        	movies.addAll(movieService.getMoviesByCast(person.getId()));
        } else if(person.getType().equals(AppConstants.PersonType.CREW.value())) {
        	movies.addAll(movieService.getMoviesByCrew(person.getId()));
        }
        return Optional.ofNullable(movies)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    @RequestMapping(value = "/people/count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Integer> getTotalMovieCount() {
            
    		Integer count = personService.getPeopleCount();
    	
            return Optional.ofNullable(count)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        
            }
    
    /**
     * GET  /people/add/:id : get the "id" person.
     *
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the person, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/people/add/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Person> addPerson(@PathVariable Integer id) {
    	log.debug("REST request to save Person By Id : {}", id);
        if (personService.isPersonAlreadyExists(id)) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("person", "idexists", "Person already exists")).body(null);
        }
        Person result;
		try {
			result = personService.addPersonByExternalId(id);
		} catch (MovieSpaceException e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("person", "idexists", e.getMessage())).body(null);
		}
        try {
			return ResponseEntity.created(new URI("/api/people/" + result.getId()))
			    .headers(HeaderUtil.createEntityCreationAlert("person", result.getId().toString()))
			    .body(result);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("person", "idexists", e.getMessage())).body(null);
		}
    }    

}
