package com.moviespace.app.service;

import com.moviespace.app.domain.Collection;
import com.moviespace.app.domain.Movie;
import com.moviespace.app.repository.CollectionRepository;
import com.moviespace.app.service.util.AppConstants;
import com.moviespace.app.service.util.AppUtil;
import com.moviespace.app.service.util.MovieSpaceException;
import com.moviespace.app.web.rest.dto.MovieDTO;

import info.movito.themoviedbapi.model.CollectionInfo;
import info.movito.themoviedbapi.model.MovieDb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Collection.
 */
@Service
@Transactional
public class CollectionService {

    private final Logger log = LoggerFactory.getLogger(CollectionService.class);
    
    @Inject
    private CollectionRepository collectionRepository;
    
    @Inject
    private GlobalCollectionService globalCollectionService;
    
    @Inject
    private GlobalMovieService globalMovieService;
    
    @Inject
    private MovieService movieService;
    
    /**
     * Save a collection.
     * 
     * @param collection the entity to save
     * @return the persisted entity
     */
    public Collection save(Collection collection) {
        log.debug("Request to save Collection : {}", collection);
        Collection result = collectionRepository.save(collection);
        if(collection.getParts()!=null && !collection.getParts().isEmpty()) {
        	collection.getParts().forEach(movie -> {
        		movie.setBelongsToCollection(result);
        		MovieDTO mov = new MovieDTO();
        		mov.setTitle(movie.getTitle());
        		mov.setExternalId(movie.getExternalId());
        		mov.setBelongsToCollectionId(result.getExternalId().longValue());
        		try {
					movieService.save(mov);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("Unable to add movie = {} ",mov);
					//e.printStackTrace();
				}
        	});
        }
        return result;
    }

    /**
     *  Get all the collection.
     *  
     *  @return the list of collections
     */
    @Transactional(readOnly = true) 
    public List<Collection> findAll() {
        log.debug("Request to get all People");
        List<Collection> result = collectionRepository.findAll();
        if(result!=null && !result.isEmpty()) {
        	result.stream().forEach(collection -> {
        		collection.setBackdropPath(AppUtil.resolveImageUrl(collection.getBackdropPath(), AppConstants.BackdropSize.W300.value()));
                collection.setPosterPath(AppUtil.resolveImageUrl(collection.getPosterPath(), AppConstants.PosterSize.W185.value()));
        	});
        }
        return result;
    }

    /**
     *  Get one collection by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Collection findOne(Long id) {
        log.debug("Request to get Collection : {}", id);
        Collection collection = collectionRepository.findOneWithEagerRelationships(id);
        collection.setBackdropPath(AppUtil.resolveImageUrl(collection.getBackdropPath(), AppConstants.BackdropSize.W300.value()));
        collection.setPosterPath(AppUtil.resolveImageUrl(collection.getPosterPath(), AppConstants.PosterSize.W185.value()));
        if(!AppUtil.isEmptyOrNull(collection.getParts())) {
        	collection.getParts().stream().forEach(part -> {
        		part.setBackdropPath(AppUtil.resolveImageUrl(part.getBackdropPath(), AppConstants.BackdropSize.W300.value()));
        		part.setPosterPath(AppUtil.resolveImageUrl(part.getPosterPath(), AppConstants.PosterSize.W185.value()));
        	});
        }
        return collection;
    }

    /**
     *  Delete the  collection by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Collection : {}", id);
        collectionRepository.delete(id);
    }
    
    public boolean isCollectionAlreadyExists(Integer externalId) {
    	Collection collection = collectionRepository.findOneByExternalId(externalId);
    	if(collection!=null)
    		return true;
    	return false;
    }
    
    public boolean addCollectionByTmdbId(Integer id) throws MovieSpaceException {
    	if(isCollectionAlreadyExists(id))
    		throw new MovieSpaceException("Collection Already exists : "+String.valueOf(id));
    		
    		CollectionInfo collectionInfo = globalCollectionService.getCollectionById(id);
    		Collection collection = new Collection();
    		collection.setExternalId(id);
    		collection.setName(collectionInfo.getName());
    		collection.setTitle(collectionInfo.getName());
    		collection.setOverview(collectionInfo.getOverview());
    		if(!AppUtil.isEmptyOrNull(collectionInfo.getPosterPath()))
    			collection.setPosterPath(AppUtil.resolveImageUrl(collectionInfo.getPosterPath(),AppConstants.PosterSize.ORIGINAL.value()));
    		if(!AppUtil.isEmptyOrNull(collectionInfo.getBackdropPath()))
    			collection.setBackdropPath(AppUtil.resolveImageUrl(collectionInfo.getBackdropPath(),AppConstants.BackdropSize.ORIGINAL.value()));
    		if(collectionInfo.getParts()!=null) {
    			collectionInfo.getParts().stream().forEach(c -> {
    				//MovieDb movie = globalMovieService.getMovieById(c.getId());
    				Movie movie = movieService.movieDbToMovie(c.getId());
    				collection.getParts().add(movie);
    			});
    		}
    		save(collection);
    		if(collection.getId()!=null)
    			return true;
    		return false;
    }
    
    
    public Collection saveCollectionToMySpace(Integer externalId) throws MovieSpaceException {
    	if(isCollectionAlreadyExists(externalId))
    		throw new MovieSpaceException("Collection Already exists : "+String.valueOf(externalId));
    		
    		CollectionInfo collectionInfo = globalCollectionService.getCollectionById(externalId);
    		Collection collection = new Collection();
    		collection.setExternalId(externalId);
    		collection.setName(collectionInfo.getName());
    		collection.setTitle(collectionInfo.getName());
    		collection.setOverview(collectionInfo.getOverview());
    		if(!AppUtil.isEmptyOrNull(collectionInfo.getPosterPath()))
    			collection.setPosterPath(collectionInfo.getPosterPath());
    		if(!AppUtil.isEmptyOrNull(collectionInfo.getBackdropPath()))
    			collection.setBackdropPath(collectionInfo.getBackdropPath());
    		if(collectionInfo.getParts()!=null) {
    			collectionInfo.getParts().stream().forEach(c -> {
    				//MovieDb movie = globalMovieService.getMovieById(c.getId());
    				Movie movie = new Movie();
    				movie.setExternalId(c.getId());
    				movie.setTitle(c.getName());
    				collection.getParts().add(movie);
    			});
    		}
    		return save(collection);
	}    
    
    
    public Integer getCollectionCount() {
    	return collectionRepository.findTotalCollectionCount();
    }
    
    
}
