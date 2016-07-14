package com.moviespace.app.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moviespace.app.domain.Company;
import com.moviespace.app.domain.Movie;
import com.moviespace.app.domain.Person;
import com.moviespace.app.domain.PersonCast;
import com.moviespace.app.repository.CompanyRepository;
import com.moviespace.app.repository.PersonRepository;
import com.moviespace.app.service.util.AppConstants;
import com.moviespace.app.service.util.AppUtil;
import com.moviespace.app.service.util.MovieSpaceException;

import info.movito.themoviedbapi.model.people.PersonPeople;

@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);
    
    @Inject
    private CompanyRepository companyRepository;
    
    @Inject
    private CastService castService;
    
    @Inject
    private CrewService crewService;
    
    @Inject
    private MovieService movieService;
    
    @Inject
    private GlobalCompanyService globalCompanyService;
    
    
    
    /**
     * Save a company.
     * 
     * @param company the entity to save
     * @return the persisted entity
     * @throws MovieSpaceException 
     */
    public Company save(Company company) throws MovieSpaceException {
        log.debug("Request to save Company : {}", company);
        if(company.getExternalId()!=null && isCompanyAlreadyExists(company.getExternalId()))
        	throw new MovieSpaceException("Company already exists");
        if(company.getParentCompany()!=null) {
        	if(company.getParentCompany().getId()==null)
        		companyRepository.save(company.getParentCompany());
        }
        Company result = companyRepository.save(company);
        return result;
    }

    /**
     *  Get all the companies.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Company> findAll() {
        log.debug("Request to get all Companies");
        List<Company> result = companyRepository.findAll();
        if(result!=null) {
        	result.stream().forEach(company -> {
        		company.setLogoPath(AppUtil.resolveImageUrl(company.getLogoPath(), AppConstants.ProfileSize.W185.value()));
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
    public Company findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        Company company = companyRepository.findOne(id);
        company.setLogoPath(AppUtil.resolveImageUrl(company.getLogoPath(), AppConstants.ProfileSize.W185.value()));
        return company;
    }

    /**
     *  Delete the company by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.delete(id);
    }
    
    public Company findOneByExternalId(Integer id) {
    	return companyRepository.findOneByExternalId(id);
    }
    
    public boolean isCompanyAlreadyExists(Integer id) {
    	Company company = companyRepository.findOneByExternalId(id);
    	if(company!=null)
    		return true;
    	return false;
    }
    
    

    @Transactional(readOnly = true) 
    public List<Movie> getMoviesByCompany(Long id) {
        log.debug("Request to get Company : {}", id);
        Company company = companyRepository.findOne(id);
        List<Movie> movies = movieService.getMoviesByCompany(company.getId());
        return movies;
    }
    
    public Company addCompanyByExternalId(Integer id) throws MovieSpaceException {
    	if(isCompanyAlreadyExists(id))
    		throw new MovieSpaceException("Company already exists");
    	info.movito.themoviedbapi.model.Company companyInfo = globalCompanyService.getCompanyById(id);
    	Company company = new Company();
    	company.setExternalId(companyInfo.getId());
    	company.setName(companyInfo.getName());
    	company.setDescription(companyInfo.getDescription());
    	company.setHeadquarters(companyInfo.getHeadquarters());
    	company.setHomepage(companyInfo.getHomepage());
    	company.setLogoPath(companyInfo.getLogoPath());
    	if(companyInfo.getParentCompany()!=null) {
    		Company existParentCompany = companyRepository.findOneByExternalId(companyInfo.getParentCompany().getId());
    		if(existParentCompany!=null)
    			company.setParentCompany(existParentCompany);
    		else {
    		info.movito.themoviedbapi.model.Company parentCompanyInfo = globalCompanyService.getCompanyById(companyInfo.getParentCompany().getId());
    		Company parentCompany = companyInfoToCompany(parentCompanyInfo);
    		company.setParentCompany(parentCompany);
    		}
    	}
    	save(company);
    return company;
    }
    
    
    public Integer getPeopleCount() {
    	return companyRepository.findTotalCompanyCount();
    }
	
    
    public Company companyInfoToCompany(info.movito.themoviedbapi.model.Company companyInfo) {
    	Company company = new Company();
    	company.setExternalId(companyInfo.getId());
    	company.setName(companyInfo.getName());
    	company.setDescription(companyInfo.getDescription());
    	company.setHeadquarters(companyInfo.getHeadquarters());
    	company.setHomepage(companyInfo.getHomepage());
    	company.setLogoPath(companyInfo.getLogoPath());
    	return company;
    }
	
}
