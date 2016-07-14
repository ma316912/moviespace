package com.moviespace.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.moviespace.app.domain.PersonCrew;
import com.moviespace.app.repository.PersonCrewRepository;
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
 * REST controller for managing PersonCrew.
 */
@RestController
@RequestMapping("/api")
public class PersonCrewResource {

    private final Logger log = LoggerFactory.getLogger(PersonCrewResource.class);
        
    @Inject
    private PersonCrewRepository personCrewRepository;
    
    /**
     * POST  /person-crews : Create a new personCrew.
     *
     * @param personCrew the personCrew to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personCrew, or with status 400 (Bad Request) if the personCrew has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/person-crews",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonCrew> createPersonCrew(@RequestBody PersonCrew personCrew) throws URISyntaxException {
        log.debug("REST request to save PersonCrew : {}", personCrew);
        if (personCrew.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("personCrew", "idexists", "A new personCrew cannot already have an ID")).body(null);
        }
        PersonCrew result = personCrewRepository.save(personCrew);
        return ResponseEntity.created(new URI("/api/person-crews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("personCrew", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /person-crews : Updates an existing personCrew.
     *
     * @param personCrew the personCrew to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personCrew,
     * or with status 400 (Bad Request) if the personCrew is not valid,
     * or with status 500 (Internal Server Error) if the personCrew couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/person-crews",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonCrew> updatePersonCrew(@RequestBody PersonCrew personCrew) throws URISyntaxException {
        log.debug("REST request to update PersonCrew : {}", personCrew);
        if (personCrew.getId() == null) {
            return createPersonCrew(personCrew);
        }
        PersonCrew result = personCrewRepository.save(personCrew);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("personCrew", personCrew.getId().toString()))
            .body(result);
    }

    /**
     * GET  /person-crews : get all the personCrews.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of personCrews in body
     */
    @RequestMapping(value = "/person-crews",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PersonCrew> getAllPersonCrews() {
        log.debug("REST request to get all PersonCrews");
        List<PersonCrew> personCrews = personCrewRepository.findAll();
        return personCrews;
    }

    /**
     * GET  /person-crews/:id : get the "id" personCrew.
     *
     * @param id the id of the personCrew to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personCrew, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/person-crews/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonCrew> getPersonCrew(@PathVariable Long id) {
        log.debug("REST request to get PersonCrew : {}", id);
        PersonCrew personCrew = personCrewRepository.findOne(id);
        return Optional.ofNullable(personCrew)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /person-crews/:id : delete the "id" personCrew.
     *
     * @param id the id of the personCrew to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/person-crews/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePersonCrew(@PathVariable Long id) {
        log.debug("REST request to delete PersonCrew : {}", id);
        personCrewRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personCrew", id.toString())).build();
    }

}
