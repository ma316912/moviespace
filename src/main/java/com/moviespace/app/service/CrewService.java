package com.moviespace.app.service;


import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moviespace.app.domain.PersonCrew;
import com.moviespace.app.repository.PersonCrewRepository;
import com.moviespace.app.service.util.AppConstants;
import com.moviespace.app.service.util.AppUtil;

/**
 * Service Implementation for managing Crew.
 */

@Service
@Transactional
public class CrewService {

    private final Logger log = LoggerFactory.getLogger(CrewService.class);
    
    @Inject
    private PersonCrewRepository personCrewRepository;
    
    /**
     * Save a crew.
     * 
     * @param person the entity to save
     * @return the persisted entity
     */
    public PersonCrew save(PersonCrew personCrew) {
        log.debug("Request to save Person Cast : {}", personCrew);
        PersonCrew result = personCrewRepository.save(personCrew);
        return result;
    }

    /**
     *  Get all the crew.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<PersonCrew> findAll() {
        log.debug("Request to get all Cast");
        List<PersonCrew> result = personCrewRepository.findAll();
        if(result!=null) {
        	result.stream().forEach(crew -> {
        		crew.getPerson().setProfilePath(AppUtil.resolveImageUrl(crew.getPerson().getProfilePath(), AppConstants.ProfileSize.W45.value()));
        	});
        }
        return result;
    }

    /**
     *  Get one person crew by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PersonCrew findOne(Long id) {
        log.debug("Request to get Person Crew : {}", id);
        PersonCrew personCrew = personCrewRepository.findOne(id);
        personCrew.getPerson().setProfilePath(AppUtil.resolveImageUrl(personCrew.getPerson().getProfilePath(), AppConstants.ProfileSize.W185.value()));
        return personCrew;
    }

    /**
     *  Delete the  person crew by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Person Crew : {}", id);
        personCrewRepository.delete(id);
    }
    
    /**
     *  Get all the crew by credit
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<PersonCrew> findAllByCreditsId(Long creditsId) {
        log.debug("Request to get all Crew");
        List<PersonCrew> result = personCrewRepository.findByCreditsId(creditsId);
        if(result!=null) {
        	result.stream().forEach(crew -> {
        		crew.getPerson().setProfilePath(AppUtil.resolveImageUrl(crew.getPerson().getProfilePath(), AppConstants.ProfileSize.W45.value()));
        	});
        }
        return result;
    }
 
	
}