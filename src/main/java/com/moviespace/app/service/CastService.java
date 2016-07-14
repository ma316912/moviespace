package com.moviespace.app.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moviespace.app.domain.PersonCast;
import com.moviespace.app.repository.PersonCastRepository;
import com.moviespace.app.service.util.AppConstants;
import com.moviespace.app.service.util.AppUtil;

/**
 * Service Implementation for managing Cast.
 */

@Service
@Transactional
public class CastService {

    private final Logger log = LoggerFactory.getLogger(CastService.class);
    
    @Inject
    private PersonCastRepository personCastRepository;
    
    /**
     * Save a cast.
     * 
     * @param person the entity to save
     * @return the persisted entity
     */
    public PersonCast save(PersonCast personCast) {
        log.debug("Request to save Person Cast : {}", personCast);
        PersonCast result = personCastRepository.save(personCast);
        return result;
    }

    /**
     *  Get all the cast.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<PersonCast> findAll() {
        log.debug("Request to get all Cast");
        List<PersonCast> result = personCastRepository.findAll();
        if(result!=null) {
        	result.stream().forEach(cast -> {
        		cast.getPerson().setProfilePath(AppUtil.resolveImageUrl(cast.getPerson().getProfilePath(), AppConstants.ProfileSize.W45.value()));
        	});
        }
        return result;
    }

    /**
     *  Get one person cast by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PersonCast findOne(Long id) {
        log.debug("Request to get Person Cast : {}", id);
        PersonCast personCast = personCastRepository.findOne(id);
        personCast.getPerson().setProfilePath(AppUtil.resolveImageUrl(personCast.getPerson().getProfilePath(), AppConstants.ProfileSize.H632.value()));
        return personCast;
    }

    /**
     *  Delete the  person cast by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Person Cast : {}", id);
        personCastRepository.delete(id);
    }
    
    /**
     *  Get all the cast by credit
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<PersonCast> findAllByCreditsId(Long creditsId) {
        log.debug("Request to get all Cast");
        List<PersonCast> result = personCastRepository.findByCreditsId(creditsId);
        if(result!=null) {
        	result.stream().forEach(cast -> {
        		cast.getPerson().setProfilePath(AppUtil.resolveImageUrl(cast.getPerson().getProfilePath(), AppConstants.ProfileSize.W185.value()));
        	});
        }
        return result;
    }
 
    @Transactional(readOnly = true) 
    public List<PersonCast> findAllByPersonId(Long personId) {
        log.debug("Request to get all Cast");
        List<PersonCast> result = personCastRepository.findByPersonId(personId);
        if(result!=null) {
        	result.stream().forEach(cast -> {
        		cast.getPerson().setProfilePath(AppUtil.resolveImageUrl(cast.getPerson().getProfilePath(), AppConstants.ProfileSize.W185.value()));
        	});
        }
        return result;
    }
    
}
