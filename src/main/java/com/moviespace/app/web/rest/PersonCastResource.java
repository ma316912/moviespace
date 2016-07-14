package com.moviespace.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.moviespace.app.domain.PersonCast;
import com.moviespace.app.repository.PersonCastRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PersonCast.
 */
@RestController
@RequestMapping("/api")
public class PersonCastResource {

    private final Logger log = LoggerFactory.getLogger(PersonCastResource.class);
        
    @Inject
    private PersonCastRepository personCastRepository;
    
    /**
     * POST  /person-casts : Create a new personCast.
     *
     * @param personCast the personCast to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personCast, or with status 400 (Bad Request) if the personCast has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/person-casts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonCast> createPersonCast(@RequestBody PersonCast personCast) throws URISyntaxException {
        log.debug("REST request to save PersonCast : {}", personCast);
        if (personCast.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("personCast", "idexists", "A new personCast cannot already have an ID")).body(null);
        }
        PersonCast result = personCastRepository.save(personCast);
        return ResponseEntity.created(new URI("/api/person-casts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("personCast", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /person-casts : Updates an existing personCast.
     *
     * @param personCast the personCast to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personCast,
     * or with status 400 (Bad Request) if the personCast is not valid,
     * or with status 500 (Internal Server Error) if the personCast couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/person-casts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonCast> updatePersonCast(@RequestBody PersonCast personCast) throws URISyntaxException {
        log.debug("REST request to update PersonCast : {}", personCast);
        if (personCast.getId() == null) {
            return createPersonCast(personCast);
        }
        PersonCast result = personCastRepository.save(personCast);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("personCast", personCast.getId().toString()))
            .body(result);
    }

    /**
     * GET  /person-casts : get all the personCasts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of personCasts in body
     */
    @RequestMapping(value = "/person-casts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PersonCast> getAllPersonCasts() {
        log.debug("REST request to get all PersonCasts");
        List<PersonCast> personCasts = personCastRepository.findAll();
        return personCasts;
    }

    /**
     * GET  /person-casts/:id : get the "id" personCast.
     *
     * @param id the id of the personCast to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personCast, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/person-casts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonCast> getPersonCast(@PathVariable Long id) {
        log.debug("REST request to get PersonCast : {}", id);
        PersonCast personCast = personCastRepository.findOne(id);
        return Optional.ofNullable(personCast)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /person-casts/:id : delete the "id" personCast.
     *
     * @param id the id of the personCast to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/person-casts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePersonCast(@PathVariable Long id) {
        log.debug("REST request to delete PersonCast : {}", id);
        personCastRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personCast", id.toString())).build();
    }

}
