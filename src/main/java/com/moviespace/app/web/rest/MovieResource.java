package com.moviespace.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.moviespace.app.domain.Movie;
import com.moviespace.app.domain.PersonCast;
import com.moviespace.app.repository.MovieRepository;
import com.moviespace.app.service.GlobalMovieService;
import com.moviespace.app.service.MovieService;
import com.moviespace.app.service.util.MovieSpaceException;
import com.moviespace.app.web.rest.util.HeaderUtil;
import com.moviespace.app.web.rest.errors.CustomParameterizedError;

import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;

import com.moviespace.app.web.rest.dto.MovieDTO;
import com.moviespace.app.web.rest.dto.MoviesDTO;
import com.moviespace.app.web.rest.mapper.MovieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Movie.
 */
@RestController
@RequestMapping("/api")
public class MovieResource {

    private final Logger log = LoggerFactory.getLogger(MovieResource.class);
        
    @Inject
    private MovieRepository movieRepository;
    
    @Inject
    private MovieService movieService;
    
    @Inject
    private MovieMapper movieMapper;
    
    @Inject
    private GlobalMovieService globalMovieService;
    
    /**
     * POST  /movies : Create a new movie.
     *
     * @param movieDTO the movieDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movieDTO, or with status 400 (Bad Request) if the movie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws MovieSpaceException 
     */
    @RequestMapping(value = "/movies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) throws URISyntaxException {
        log.debug("REST request to save Movie : {}", movieDTO);
        if (movieDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "idexists", "A new movie cannot already have an ID")).body(null);
        }
        MovieDTO result = new MovieDTO();
        //Movie movie = movieMapper.movieDTOToMovie(movieDTO);
        //movie = movieRepository.save(movie);
        //MovieDTO result = movieMapper.movieToMovieDTO(movie);
        try {
			result = movieService.saveMovieToMySpace(movieDTO.getExternalId());
		} catch (MovieSpaceException e) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "idexists", e.getErrorDTO().getMessage())).body(null);
		}
        return ResponseEntity.created(new URI("/api/movies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("movie", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movies : Updates an existing movie.
     *
     * @param movieDTO the movieDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movieDTO,
     * or with status 400 (Bad Request) if the movieDTO is not valid,
     * or with status 500 (Internal Server Error) if the movieDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws MovieSpaceException 
     */
    @RequestMapping(value = "/movies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MovieDTO> updateMovie(@Valid @RequestBody MovieDTO movieDTO) throws URISyntaxException {
        log.debug("REST request to update Movie : {}", movieDTO);
        if (movieDTO.getId() == null) {
            return createMovie(movieDTO);
        }
        Movie movie = movieMapper.movieDTOToMovie(movieDTO);
        movie = movieRepository.save(movie);
        MovieDTO result = movieMapper.movieToMovieDTO(movie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("movie", movieDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movies : get all the movies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of movies in body
     */
    @RequestMapping(value = "/movies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<MovieDTO> getAllMovies() {
        log.debug("REST request to get all Movies");
        //List<Movie> movies = movieRepository.findAllWithEagerRelationships();
        List<MovieDTO> movies = movieService.findAll();
        return movies;
    }

    /**
     * GET  /movies/:id : get the "id" movie.
     *
     * @param id the id of the movieDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movieDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/movies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MovieDTO> getMovie(@PathVariable Long id) {
        log.debug("REST request to get Movie : {}", id);
        //Movie movie = movieRepository.findOneWithEagerRelationships(id);
        //MovieDTO movieDTO = movieMapper.movieToMovieDTO(movie);
        MovieDTO movieDTO = movieService.findOne(id);
        return Optional.ofNullable(movieDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /movies/:id : delete the "id" movie.
     *
     * @param id the id of the movieDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/movies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.debug("REST request to delete Movie : {}", id);
        movieRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("movie", id.toString())).build();
    }

    
    
    @RequestMapping(value = "/movies/popular",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<MovieDb>> getPopularMovies() {
            log.debug("REST request to get Popular Movies");
            List<MovieDb> movies = globalMovieService.getPopularMovies();
            return Optional.ofNullable(movies)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    
    @RequestMapping(value = "/movies/latest",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<MovieDb>> getLatestMovies() {
            log.debug("REST request to get Latest Movies");
            List<MovieDb> movies = globalMovieService.getLatestMovies();
            return Optional.ofNullable(movies)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    @RequestMapping(value = "/movies/upcoming",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<MovieDb>> getUpcomingMovies() {
            log.debug("REST request to get Upcoming Movies");
            List<MovieDb> movies = globalMovieService.getUpcomingMovies();
            return Optional.ofNullable(movies)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    @RequestMapping(value = "/movies/top-rated",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<MovieDb>> getTopRatedMovies() {
            log.debug("REST request to get Top Rated Movies");
            List<MovieDb> movies = globalMovieService.getTopRatedMovies();
            return Optional.ofNullable(movies)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    @RequestMapping(value = "/movies/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<MoviesDTO> getAllMoviesExternal() {
            log.debug("REST request to get Top Rated Movies");
            
            List<MovieDb> popularMovies = globalMovieService.getPopularMovies();
            List<MovieDb> upcomingMovies = globalMovieService.getUpcomingMovies();
            List<MovieDb> latestMovies = globalMovieService.getLatestMovies();
            List<MovieDb> topRatedmovies = globalMovieService.getTopRatedMovies();
            
            MoviesDTO movies = new MoviesDTO(popularMovies,upcomingMovies,latestMovies,topRatedmovies);
            
            return Optional.ofNullable(movies)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    
    @RequestMapping(value = "/movie/add/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Void> saveMovieToMySpace(@PathVariable Integer id) throws CustomParameterizedError {
            log.debug("REST request to get external movie to myspace {}", id);
            MovieDTO result;
			try {
				result = movieService.saveMovieToMySpace(id);
			} catch (MovieSpaceException e) {
				//return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "idexists", "error")).body(null);
				throw new CustomParameterizedError(e.getMessage());
			}
            if(result!=null)
            	return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert("movie", ""+result.getId())).body(null);
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "idexists", "error")).body(null);
            //return ResponseEntity.ok().headers(HeaderUtil.createFailureAlert("movie", null, "error")).build();
        }
    
    
    /**
     * GET  /movies/:id : get the "id" movie.
     *
     * @param id the id of the movieDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movieDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/movies/{id}/cast",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getMovieCast(@PathVariable Long id) {
        log.debug("REST request to get Movie : {}", id);
        Movie movie = movieRepository.findOne(id);
        if(movie==null)
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "idexists", "Movie does not exists")).body(null);
        
        Credits credits = globalMovieService.getCreditsByMovieId(movie.getExternalId());
        
        /*log.info("Credits is : {}",credits);
        if(credits!=null) {
        	List<info.movito.themoviedbapi.model.people.PersonCast> cast = credits.getCast();
        	if(cast!=null)
        		credits.getCast().stream().forEach(c->{
        			log.info("{} , {} , {}",c.getCastId(),c.getCharacter(),c.getName());
        		});
        }*/
		return null;
        
/*        return Optional.ofNullable(movieDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
*/    
        }
    
    
    @RequestMapping(value = "/movies/{id}/credits",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Credits> getMovieCredits(@PathVariable Long id) {
            log.debug("REST request to get Movie : {}", id);
            Movie movie = movieRepository.findOne(id);
            if(movie==null)
            	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("credits", "idexists", "Movie Credits does not exist")).body(null);
            
            Credits credits = globalMovieService.getCreditsByMovieId(movie.getExternalId());
            
            return Optional.ofNullable(credits)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        
            }
    
    @RequestMapping(value = "/movies/count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<Integer> getTotalMovieCount() {
            
    		Integer count = movieRepository.findTotalMovieCount();
    	
            return Optional.ofNullable(count)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        
            }
    
}
