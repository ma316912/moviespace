package com.moviespace.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.moviespace.app.domain.Company;
import com.moviespace.app.domain.Movie;
import com.moviespace.app.domain.Person;
import com.moviespace.app.repository.CompanyRepository;
import com.moviespace.app.service.CompanyService;
import com.moviespace.app.service.MovieService;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource {

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);
        
    @Inject
    private CompanyRepository companyRepository;
    
    @Inject
    private CompanyService companyService;
    
    @Inject
    private MovieService movieService;
    
    /**
     * POST  /companies : Create a new company.
     *
     * @param company the company to create
     * @return the ResponseEntity with status 201 (Created) and with body the new company, or with status 400 (Bad Request) if the company has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/companies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Company> createCompany(@RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to save Company : {}", company);
        if (company.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("company", "idexists", "A new company cannot already have an ID")).body(null);
        }
        Company result;
		try {
			result = companyService.save(company);
		} catch (MovieSpaceException e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("company", "idexists", e.getMessage())).body(null);
		}
        return ResponseEntity.created(new URI("/api/companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("company", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /companies : Updates an existing company.
     *
     * @param company the company to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated company,
     * or with status 400 (Bad Request) if the company is not valid,
     * or with status 500 (Internal Server Error) if the company couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/companies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Company> updateCompany(@RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to update Company : {}", company);
        if (company.getId() == null) {
            return createCompany(company);
        }
        Company result;
		try {
			result = companyService.save(company);
		} catch (MovieSpaceException e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("company", "idexists", e.getMessage())).body(null);
		}
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("company", company.getId().toString()))
            .body(result);
    }

    /**
     * GET  /companies : get all the companies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of companies in body
     */
    @RequestMapping(value = "/companies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Company> getAllCompanies() {
        log.debug("REST request to get all Companies");
        List<Company> companies = companyService.findAll();
        return companies;
    }

    /**
     * GET  /companies/:id : get the "id" company.
     *
     * @param id the id of the company to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the company, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/companies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        log.debug("REST request to get Company : {}", id);
        Company company = companyService.findOne(id);
        return Optional.ofNullable(company)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /companies/:id : delete the "id" company.
     *
     * @param id the id of the company to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/companies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        log.debug("REST request to delete Company : {}", id);
        companyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("company", id.toString())).build();
    }
    
    
    
    
    /**
     * GET  /companies/add/:id : add company by external "id".
     *
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the person, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/companies/add/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Company> addCompany(@PathVariable Integer id) {
    	log.debug("REST request to save Person By Id : {}", id);
        if (companyService.isCompanyAlreadyExists(id)) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("company", "idexists", "Company already exists")).body(null);
        }
        Company result;
		try {
			result = companyService.addCompanyByExternalId(id);
		} catch (MovieSpaceException e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("company", "idexists", e.getMessage())).body(null);
		}
        try {
			return ResponseEntity.created(new URI("/api/companies/" + result.getId()))
			    .headers(HeaderUtil.createEntityCreationAlert("company", result.getId().toString()))
			    .body(result);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("company", "idexists", e.getMessage())).body(null);
		}
    } 
    
    
    
    @RequestMapping(value = "/companies/count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Integer> getTotalMovieCount() {
            
    		Integer count = companyService.getPeopleCount();
    	
            return Optional.ofNullable(count)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        
            }
    
    
    /**
     * GET  /companies/:id/movies : get movies by company id.
     *
     * @param id the id of the genre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the genre, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/companies/{id}/movies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Movie>> getMoviesByCompanyId(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        List<Movie> moviesByCompany = movieService.getMoviesByCompany(id);
        return Optional.ofNullable(moviesByCompany)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    

}
