package com.moviespace.app.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.moviespace.app.domain.Movie;
import com.moviespace.app.domain.Review;
import com.moviespace.app.service.util.AppConstants;
import com.moviespace.app.service.util.AppUtil;
import com.moviespace.app.web.rest.dto.MovieDTO;
import com.moviespace.app.web.rest.mapper.MovieMapper;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbFind;
import info.movito.themoviedbapi.TmdbFind.ExternalSource;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbReviews;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.ArtworkType;
import info.movito.themoviedbapi.model.Company;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.Language;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.ProductionCompany;
import info.movito.themoviedbapi.model.Reviews;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCrew;

@Service
public class GlobalMovieService {

	private final Logger log = LoggerFactory.getLogger(GlobalMovieService.class);
    
    @Inject
    private MovieMapper movieMapper;
    
    @Inject
    private TmdbApi tmdbApi;
    
    private TmdbMovies movies;
    
    private TmdbReviews reviews;
    
    private TmdbFind find;
    
    @Inject
    public GlobalMovieService(final TmdbApi tmdbApi) {
    	this.tmdbApi = tmdbApi;
    	movies = tmdbApi.getMovies();
    	reviews = tmdbApi.getReviews();
    	find = tmdbApi.getFind();
    }
    
    public MovieDb getMovieById(Integer id) {
    	return movies.getMovie(id, AppConstants.Language.EN.value());
    }
    
    public List<Artwork> getImagesByMovieId(Integer id) {    
    	return movies.getImages(id, AppConstants.Language.EN.value()).getAll(ArtworkType.POSTER,ArtworkType.BACKDROP);
    }
    
    public List<Reviews> getReviewsByMovieId(Integer id) {
    	List<Reviews> revs = reviews.getReviews(id, AppConstants.Language.EN.value(),0).getResults();
    	log.info("Retrieved {} for movie id {}",revs.size(),id);
    	return revs;
    }
    
    public List<MovieDb> getLatestMovies() {
    	return movies.getNowPlayingMovies(AppConstants.Language.EN.value(),null).getResults();
    }
    
    public List<MovieDb> getPopularMovies() {
    	return movies.getPopularMovies(AppConstants.Language.EN.value(),null).getResults();
    }
    
    
    public List<MovieDb> getUpcomingMovies() {
    	return movies.getUpcoming(AppConstants.Language.EN.value(),null).getResults();
    }
    
    
    public List<MovieDb> getTopRatedMovies() {
    	return movies.getTopRatedMovies(AppConstants.Language.EN.value(),null).getResults();
    }
    
    
    public List<MovieDb> findMoviesByImdbId(String id) {
    	return find.find(id, ExternalSource.imdb_id, AppConstants.Language.EN.value()).getMovieResults();
    }
    
    public List<Person> findPeopleByImdbId(String id) {
    	return find.find(id, ExternalSource.imdb_id, AppConstants.Language.EN.value()).getPersonResults();
    }
    
    
    public Credits getCreditsByMovieId(Integer id) {
    	return movies.getCredits(id);
    }
    
public MovieDTO getMovieFromImdb(int id) {
    	
    	MovieDb movieResponse = movies.getMovie(id, "en");
    	MovieDTO movie = new MovieDTO();
    	Movie mov = new Movie();
    	if(movieResponse!=null) {
 		
    		log.info("Title = {}",movieResponse.getTitle());
    		mov.setTitle(movieResponse.getTitle());
    		mov.setOriginalTitle(movieResponse.getOriginalTitle());
    		mov.setAdult(movieResponse.isAdult());
    		mov.setBackdropPath(AppUtil.resolveImageUrl(movieResponse.getBackdropPath(), null));
    		mov.setPosterPath(AppUtil.resolveImageUrl(movieResponse.getPosterPath(), null));
    		mov.setOverview(movieResponse.getOverview());
    		mov.setBudget(movieResponse.getBudget());
    		mov.setImdbId(movieResponse.getImdbID());
    		mov.setPopularity(movieResponse.getPopularity());
    		mov.setVoteAverage(movieResponse.getVoteAverage());
    		mov.setVoteCount(movieResponse.getVoteCount());
    		
    		
    		List<PersonCrew> crew = movieResponse.getCrew();
    		if(crew == null) {
    			log.info("No Crew available");
    		} else {
    		   log.info("Crew Available");
    		   crew.stream().forEach(m -> {
    			   log.info("Cast Id {}",m.getCastId());
    			   log.info("Cast Name {}",m.getName());
    			   log.info("Person Id {}",m.getId());
    			   log.info("Person Profile Path {}",m.getProfilePath());
    		   });
    		}
    		List<Reviews> reviews = tmdbApi.getReviews().getReviews(movieResponse.getId(),AppConstants.Language.EN.value(),null).getResults();
    		if(reviews==null) {
    			log.info("No reviews found");
    		} else {
    			log.info("Reviews available");
    			reviews.stream().forEach(review -> {
    				log.info("--------- Review Start ---------");
    				log.info("Author = {}",review.getAuthor());
    				log.info("Comment = {}",review.getContent());
    				log.info("Author Id = {}",review.getId());
    				log.info("--------- Review End ---------");
     			   Review rev = new Review();
     			   rev.setAuthor(review.getAuthor());
     			   rev.setContent(review.getContent());
     			   mov.getReviews().add(rev);
    			});
    		}
    		
    		List<Language> languages = movieResponse.getSpokenLanguages();
    		if(languages==null) {
    			log.info("No Languages found");
    		} else {
    			languages.stream().forEach(l -> {
    				com.moviespace.app.domain.Language lang = new com.moviespace.app.domain.Language();
    				lang.setIsoCode(l.getIsoCode());
    				lang.setName(l.getName());
    			});
    		}
    		
    		
    		List<ProductionCompany> productionCompanies = movieResponse.getProductionCompanies();
    		if(productionCompanies==null) {
    			log.info("No Production companies found");
    		} else {
    			productionCompanies.stream().forEach(pc -> {
    				log.info("Name = {}",pc.getName());
    				Company company = tmdbApi.getCompany().getCompanyInfo(pc.getId());
    				log.info("Headquarters = {}",company.getHeadquarters());
    				log.info("Company Desc = {}",company.getDescription());
    				
    			});
    		}
    		
    		
    	//List<MovieDb> movies = tmdbApi.getFind().find(movieResponse.getImdbID(), ExternalSource.imdb_id, AppConstants.Language.EN.value()).getMovieResults();	
    		
    	}
    	movieMapper.movieToMovieDTO(mov);
    	return movie;
    }
    
    
}
