package com.moviespace.app.service;

import com.moviespace.app.domain.Person;
import com.moviespace.app.domain.PersonCast;
import com.moviespace.app.repository.PersonRepository;
import com.moviespace.app.service.util.AppConstants;
import com.moviespace.app.service.util.AppUtil;
import com.moviespace.app.service.util.MovieSpaceException;

import info.movito.themoviedbapi.model.people.PersonPeople;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Person.
 */
@Service
@Transactional
public class PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonService.class);
    
    @Inject
    private PersonRepository personRepository;
    
    @Inject
    private CastService castService;
    
    @Inject
    private CrewService crewService;
    
    @Inject
    private MovieService movieService;
    
    @Inject
    private GlobalPeopleService globalPeopleService;
    
    
    
    /**
     * Save a person.
     * 
     * @param person the entity to save
     * @return the persisted entity
     * @throws MovieSpaceException 
     */
    public Person save(Person person) throws MovieSpaceException {
        log.debug("Request to save Person : {}", person);
        if(person.getExternalId()!=null && isPersonAlreadyExists(person.getExternalId()))
        	throw new MovieSpaceException("Person already exists");
        Person result = personRepository.save(person);
        return result;
    }

    /**
     *  Get all the people.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Person> findAll() {
        log.debug("Request to get all People");
        List<Person> result = personRepository.findAll();
        if(result!=null) {
        	result.stream().forEach(person -> {
        		person.setProfilePath(AppUtil.resolveImageUrl(person.getProfilePath(), AppConstants.ProfileSize.W185.value()));
        	});
        }
        return result;
    }

    /**
     *  Get one person by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Person findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        Person person = personRepository.findOne(id);
        person.setProfilePath(AppUtil.resolveImageUrl(person.getProfilePath(), AppConstants.ProfileSize.W185.value()));
        return person;
    }

    /**
     *  Delete the  person by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.delete(id);
    }
    
    public Person findOneByExternalId(Integer id) {
    	return personRepository.findOneByExternalId(id);
    }
    
    public boolean isPersonAlreadyExists(Integer id) {
    	Person person = personRepository.findOneByExternalId(id);
    	if(person!=null)
    		return true;
    	return false;
    }
    
    

    @Transactional(readOnly = true) 
    public void getMoviesByPerson(Long id) {
        log.debug("Request to get Person : {}", id);
        Person person = personRepository.findOne(id);
        List<PersonCast> casts = castService.findAllByPersonId(person.getId());
        
        casts.get(0).getCredits().getId();
        
    }
    
    public Person addPersonByExternalId(Integer id) throws MovieSpaceException {
    	PersonPeople personInfo = globalPeopleService.getPersonById(id);
    	Person person = new Person();
    	person.setExternalId(personInfo.getId());
    	person.setName(personInfo.getName());
    	person.setBirthday(AppUtil.convertYYYYMMDDToZonedDateTime(personInfo.getBirthday()));
    	person.setBirthPlace(personInfo.getBirthplace());
    	person.setDeathday(AppUtil.convertYYYYMMDDToZonedDateTime(personInfo.getDeathday()));
    	person.setBiography(personInfo.getBiography());
    	person.setImdbId(personInfo.getImdbId());
    	person.setHomepage(personInfo.getHomepage());
    	person.setPopularity(personInfo.getPopularity());
    	person.setAka(String.join(",", personInfo.getAka()));
    	person.setProfilePath(personInfo.getProfilePath());
    	person.setType(AppUtil.getPersonType(personInfo.getPersonType()));
    	save(person);
    return person;
    }
    
    
    public Integer getPeopleCount() {
    	return personRepository.findTotalPeopleCount();
    }
    
}
