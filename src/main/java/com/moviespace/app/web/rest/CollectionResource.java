package com.moviespace.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.moviespace.app.domain.Collection;
import com.moviespace.app.repository.CollectionRepository;
import com.moviespace.app.service.CollectionService;
import com.moviespace.app.service.util.MovieSpaceException;
import com.moviespace.app.web.rest.dto.CollectionDTO;
import com.moviespace.app.web.rest.dto.MovieDTO;
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
 * REST controller for managing Collection.
 */
@RestController
@RequestMapping("/api")
public class CollectionResource {

    private final Logger log = LoggerFactory.getLogger(CollectionResource.class);
        
    @Inject
    private CollectionRepository collectionRepository;
    
    @Inject
    private CollectionService collectionService;
    
    /**
     * POST  /collections : Create a new collection.
     *
     * @param collection the collection to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collection, or with status 400 (Bad Request) if the collection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collections",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collection> createCollection(@RequestBody Collection collection) throws URISyntaxException {
        log.debug("REST request to save Collection : {}", collection);
        if (collection.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("collection", "idexists", "A new collection cannot already have an ID")).body(null);
        }
        Collection result = collectionRepository.save(collection);
        return ResponseEntity.created(new URI("/api/collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("collection", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collections : Updates an existing collection.
     *
     * @param collection the collection to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collection,
     * or with status 400 (Bad Request) if the collection is not valid,
     * or with status 500 (Internal Server Error) if the collection couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collections",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collection> updateCollection(@RequestBody Collection collection) throws URISyntaxException {
        log.debug("REST request to update Collection : {}", collection);
        if (collection.getId() == null) {
            return createCollection(collection);
        }
        Collection result = collectionRepository.save(collection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("collection", collection.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collections : get all the collections.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of collections in body
     */
    @RequestMapping(value = "/collections",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Collection> getAllCollections() {
        log.debug("REST request to get all Collections");
        List<Collection> collections = collectionService.findAll();
        return collections;
    }

    /**
     * GET  /collections/:id : get the "id" collection.
     *
     * @param id the id of the collection to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collection, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/collections/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CollectionDTO> getCollection(@PathVariable Long id) {
        log.debug("REST request to get Collection : {}", id);
        Collection collection = collectionService.findOne(id);
        CollectionDTO col = new CollectionDTO();
        col.setId(collection.getId());
        col.setExternalId(collection.getExternalId());
        col.setTitle(collection.getTitle());
        col.setName(collection.getName());
        col.setBackdropPath(collection.getBackdropPath());
        col.setPosterPath(collection.getPosterPath());
        col.setOverview(collection.getOverview());
        col.setReleaseDate(collection.getReleaseDate());
        col.getParts().addAll(collection.getParts());
        return Optional.ofNullable(col)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /collections/:id : delete the "id" collection.
     *
     * @param id the id of the collection to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/collections/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        log.debug("REST request to delete Collection : {}", id);
        collectionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("collection", id.toString())).build();
    }
    
    
    
    @RequestMapping(value = "/collections",params={"externalId"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Void> searchByExternalIdAndSave(@RequestParam Integer externalId) {
            log.debug("REST request to get Collection : {}", externalId);
            
            return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert("collection", "success")).build();
        }
    
    
    
    
    @RequestMapping(value = "/collections/add/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Void> saveCollectionToMySpace(@PathVariable Integer id) throws MovieSpaceException {
            log.debug("REST request to get external collection to myspace {}", id);
            if(collectionService.isCollectionAlreadyExists(id))
    			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("collection", "idexists", "Collection already exists")).body(null);
            
            Collection status = collectionService.saveCollectionToMySpace(id);
            
            if(status!=null)
            	return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert("collection", ""+status.getId())).build();
            return ResponseEntity.ok().headers(HeaderUtil.createFailureAlert("collection", null, "error")).build();
        }
    
    
    @RequestMapping(value = "/collections/count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Integer> getTotalMovieCount() {
            
    		Integer count = collectionService.getCollectionCount();
    	
            return Optional.ofNullable(count)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        
            }
    
    

}
