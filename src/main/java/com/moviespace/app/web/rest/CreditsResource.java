package com.moviespace.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.moviespace.app.domain.Credits;
import com.moviespace.app.repository.CreditsRepository;
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
 * REST controller for managing Credits.
 */
@RestController
@RequestMapping("/api")
public class CreditsResource {

    private final Logger log = LoggerFactory.getLogger(CreditsResource.class);
        
    @Inject
    private CreditsRepository creditsRepository;
    
    /**
     * POST  /credits : Create a new credits.
     *
     * @param credits the credits to create
     * @return the ResponseEntity with status 201 (Created) and with body the new credits, or with status 400 (Bad Request) if the credits has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/credits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Credits> createCredits(@RequestBody Credits credits) throws URISyntaxException {
        log.debug("REST request to save Credits : {}", credits);
        if (credits.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("credits", "idexists", "A new credits cannot already have an ID")).body(null);
        }
        Credits result = creditsRepository.save(credits);
        return ResponseEntity.created(new URI("/api/credits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("credits", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /credits : Updates an existing credits.
     *
     * @param credits the credits to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated credits,
     * or with status 400 (Bad Request) if the credits is not valid,
     * or with status 500 (Internal Server Error) if the credits couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/credits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Credits> updateCredits(@RequestBody Credits credits) throws URISyntaxException {
        log.debug("REST request to update Credits : {}", credits);
        if (credits.getId() == null) {
            return createCredits(credits);
        }
        Credits result = creditsRepository.save(credits);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("credits", credits.getId().toString()))
            .body(result);
    }

    /**
     * GET  /credits : get all the credits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of credits in body
     */
    @RequestMapping(value = "/credits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Credits> getAllCredits() {
        log.debug("REST request to get all Credits");
        List<Credits> credits = creditsRepository.findAll();
        return credits;
    }

    /**
     * GET  /credits/:id : get the "id" credits.
     *
     * @param id the id of the credits to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the credits, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/credits/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Credits> getCredits(@PathVariable Long id) {
        log.debug("REST request to get Credits : {}", id);
        Credits credits = creditsRepository.findOne(id);
        return Optional.ofNullable(credits)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /credits/:id : delete the "id" credits.
     *
     * @param id the id of the credits to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/credits/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCredits(@PathVariable Long id) {
        log.debug("REST request to delete Credits : {}", id);
        creditsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("credits", id.toString())).build();
    }

}
