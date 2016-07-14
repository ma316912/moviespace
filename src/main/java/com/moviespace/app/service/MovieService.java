package com.moviespace.app.service;

import com.moviespace.app.domain.Collection;
import com.moviespace.app.domain.Company;
import com.moviespace.app.domain.Genre;
import com.moviespace.app.domain.Movie;
import com.moviespace.app.domain.Person;
import com.moviespace.app.domain.PersonCast;
import com.moviespace.app.domain.PersonCrew;
import com.moviespace.app.domain.Review;
import com.moviespace.app.repository.CollectionRepository;
import com.moviespace.app.repository.CreditsRepository;
import com.moviespace.app.repository.MovieRepository;
import com.moviespace.app.repository.PersonCastRepository;
import com.moviespace.app.repository.PersonCrewRepository;
import com.moviespace.app.repository.PersonRepository;
import com.moviespace.app.repository.ReviewRepository;
import com.moviespace.app.service.util.AppConstants;
import com.moviespace.app.service.util.AppUtil;
import com.moviespace.app.service.util.MovieSpaceException;
import com.moviespace.app.web.rest.dto.CreditsDTO;
import com.moviespace.app.web.rest.dto.MovieDTO;
import com.moviespace.app.web.rest.mapper.MovieMapper;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.CollectionInfo;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.ProductionCompany;
import info.movito.themoviedbapi.model.Reviews;
import info.movito.themoviedbapi.model.people.PersonPeople;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Movie.
 */
@Service
@Transactional
public class MovieService {

	private final Logger log = LoggerFactory.getLogger(MovieService.class);

	@Inject
	private MovieRepository movieRepository;

	@Inject
	private CollectionRepository collectionRepository;

	@Inject
	private MovieMapper movieMapper;
	
	@Inject
	Environment env;

	private TmdbApi tmdbApi;

	@Inject
	private GlobalMovieService globalMovieService;

	@Inject
	private GlobalPeopleService globalPeopleService;
	
	@Inject
	private GlobalCollectionService globalCollectionService;
	
	@Inject
	private GlobalCompanyService globalCompanyService;
	
	@Inject
	private GenreService genreService;

	@Inject
	private ReviewRepository reviewRepository;
	
	@Inject
	private PersonCastRepository personCastRepository;
	
	@Inject
	private PersonCrewRepository personCrewRepository;
	
	@Inject
	private PersonRepository personRepository;
	
	@Inject
	private PersonService personService;
	
	@Inject
	private CompanyService companyService;
	
	@Inject
	private CreditsRepository creditRepository;
	
	
	@Inject
	private CastService castService;
	
	@Inject
	private CrewService crewService;
	
	@Inject
	public MovieService(Environment env) {
		this.env = env;
		tmdbApi = new TmdbApi(env.getProperty("ws.tmdb.api_key"));
	}
	
	
	public Movie save(Movie movie) {
		return movieRepository.save(movie);
	}

	/**
	 * Save a movie.
	 * 
	 * @param movieDTO
	 *            the entity to save
	 * @return the persisted entity
	 * @throws MovieSpaceException 
	 */
	public MovieDTO save(MovieDTO movieDTO) throws MovieSpaceException {
		log.debug("Request to save Movie : {}", movieDTO);
		// Movie movie = movieMapper.movieDTOToMovie(movieDTO);
		// movie = movieRepository.save(movie);
		// Get Movie
		Movie movie = null;
		movie = movieRepository.findOneByExternalId(movieDTO.getExternalId());
		if (movie == null) {
			movie = movieDbToMovie(movieDTO.getExternalId());
			Collection collection = null;
			if (movieDTO.getBelongsToCollectionId() != null) {
				Integer collectionId = movieDTO.getBelongsToCollectionId().intValue();
				collection = collectionRepository.findOneByExternalId(collectionId);
				if (collection == null) {
					CollectionInfo collectionEntity = globalCollectionService.getCollectionById(collectionId);
					collection = new Collection();
					collection.setExternalId(collectionEntity.getId());
					collection.setName(collectionEntity.getName());
					collection.setBackdropPath(collectionEntity.getBackdropPath());
					collection.setPosterPath(collectionEntity.getPosterPath());
					collection.setOverview(collectionEntity.getOverview());
					collection.setTitle(collectionEntity.getName());
					collection = collectionRepository.save(collection);
				}
				movie.setBelongsToCollection(collection);
			}
			
			if(movie.getCredits() != null) {
				movie.getCredits().setExternalId(movie.getExternalId());
				com.moviespace.app.domain.Credits credits = creditRepository.save(movie.getCredits());
				if(!AppUtil.isEmptyOrNull(movie.getCredits().getCasts())) {
					movie.getCredits().getCasts().stream().forEach(cast -> {
						/*PersonCast personCast = personCastRepository.findOneByExternalId(cast.getExternalId());
						if(personCast != null) {
							personCast.setCredits(credits);
						}*/
						Person p = personRepository.findOneByExternalId(cast.getPerson().getExternalId());
						if(p != null) {
							cast.setPerson(p);
						} else {
							personRepository.save(cast.getPerson());
						}
						cast.setCredits(credits);
						personCastRepository.save(cast);
					});
				}
				if(!AppUtil.isEmptyOrNull(movie.getCredits().getCrews())) {
					movie.getCredits().getCrews().stream().forEach(crew -> {
						/*PersonCast personCast = personCastRepository.findOneByExternalId(cast.getExternalId());
						if(personCast != null) {
							personCast.setCredits(credits);
						}*/
						Person p = personRepository.findOneByExternalId(crew.getPerson().getExternalId());
						if(p != null) {
							crew.setPerson(p);
						} else {
							personRepository.save(crew.getPerson());
						}
						crew.setCredits(credits);
						personCrewRepository.save(crew);
					});
				}
			}
			
			movie = movieRepository.save(movie);
			if(!AppUtil.isEmptyOrNull(movie.getGenres())) {
				for(Genre g:movie.getGenres()) {
					Genre gen = genreService.findOneByExternalId(g.getExternalId());
					if(gen!=null) {
						g.setId(gen.getId());
						g.getMovies().add(movie);
						genreService.save(g);
					} else {
						g.getMovies().add(movie);
						genreService.save(g);
					}
				}
			}
			
			if(!AppUtil.isEmptyOrNull(movie.getProductionCompanies())) {
				for(Company c:movie.getProductionCompanies()) {
					c.getMovies().add(movie);
					//companyService.save(c);
				}
			}
			
			if(!AppUtil.isEmptyOrNull(movie.getReviews())) {
				for(Review r:movie.getReviews()) {
					Review rev = reviewRepository.findByExternalId(r.getExternalId());
					if(rev!=null) {
						r.setId(rev.getId());
						r.setMovie(movie);
						reviewRepository.save(r);
					} else {
						r.setMovie(movie);
						reviewRepository.save(r);
					}
				}
			}
			
		} else {
			throw new MovieSpaceException("Movie already exists");
		}
		MovieDTO result = movieMapper.movieToMovieDTO(movie);
		return result;
	}

	/**
	 * Get all the movies.
	 * 
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<MovieDTO> findAll() {
		log.debug("Request to get all Movies");
		List<MovieDTO> result = movieRepository.findAllWithEagerRelationships().stream()
				.map(movieMapper::movieToMovieDTO).collect(Collectors.toCollection(LinkedList::new));
		/*result.stream().map(m -> {
			m.setPosterPath(AppUtil.resolveImageUrl(m.getPosterPath(), AppConstants.PosterSize.ORIGINAL.value()));
		});*/
		return result;
	}

	/**
	 * Get one movie by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public MovieDTO findOne(Long id) {
		log.debug("Request to get Movie : {}", id);
		Movie movie = movieRepository.findOneWithEagerRelationships(id);
		movie.getCredits();
		MovieDTO movieDTO = movieMapper.movieToMovieDTO(movie);
		CreditsDTO credits = new CreditsDTO();
		credits.setId(movieDTO.getId());
		credits.setExternalId(movie.getExternalId());
		credits.getCast().addAll(castService.findAllByCreditsId(movieDTO.getCreditsId()));
		/*credits.getCast().stream().forEach(cast -> {
			cast.getPerson().setProfilePath(AppUtil.resolveImageUrl(cast.getPerson().getProfilePath(), AppConstants.ProfileSize.W185.value()));
		});*/
		credits.getCrew().addAll(crewService.findAllByCreditsId(movieDTO.getCreditsId()));
		/*credits.getCrew().stream().forEach(crew -> {
			crew.getPerson().setProfilePath(AppUtil.resolveImageUrl(crew.getPerson().getProfilePath(), AppConstants.ProfileSize.W185.value()));
		});*/
		movieDTO.setCredit(credits);
		//movieDTO.setBackdropPath(AppUtil.resolveImageUrl(movieDTO.getBackdropPath(),"original"));
		//movieDTO.setPosterPath(AppUtil.resolveImageUrl(movieDTO.getPosterPath(),"original"));
		return movieDTO;
	}

	/**
	 * Delete the movie by id.
	 * 
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Movie : {}", id);
		//Movie movie = movieRepository.findOne(id);
		//reviewRepository.deleteInBatch(movie.getReviews());
		movieRepository.delete(id);
	}
	
	public Movie movieDbToMovie(Integer id) {
		Movie movie = null;
		MovieDb movieEntity = globalMovieService.getMovieById(id);
		if (movieEntity == null) {
			return movie;
		}
		movie = new Movie();
		movie.setExternalId(movieEntity.getId());
		movie.setAdult(movieEntity.isAdult());
		movie.setBackdropPath(movieEntity.getBackdropPath());
		movie.setPosterPath(movieEntity.getPosterPath());
		movie.setBudget(movieEntity.getBudget());
		movie.setHomePage(movieEntity.getHomepage());
		movie.setImdbId(movieEntity.getImdbID());
		movie.setOriginalTitle(movieEntity.getOriginalTitle());
		movie.setOverview(movieEntity.getOverview());
		movie.setPopularity(movieEntity.getPopularity());
		movie.setReleaseDate(AppUtil.convertYYYYMMDDToZonedDateTime(movieEntity.getReleaseDate()));
		movie.setRevenue(movieEntity.getRevenue());
		movie.setRuntime(movieEntity.getRuntime());
		movie.setStatus(movieEntity.getStatus());
		movie.setTagline(movieEntity.getTagline());
		movie.setTitle(movieEntity.getTitle());
		movie.setUserRating(movieEntity.getUserRating());
		movie.setVoteAverage(movieEntity.getVoteAverage());
		movie.setVoteCount(movieEntity.getVoteCount());
		List<Genre> genres = new ArrayList<>();
		if(!AppUtil.isEmptyOrNull(movieEntity.getGenres())) {
			movieEntity.getGenres().stream().forEach(g->{
				Genre gen = new Genre();
				gen.setExternalId(g.getId());
				gen.setName(g.getName());
				genres.add(gen);
			});
		movie.getGenres().addAll(genres);
		}
		
		List<Reviews> movieReviews = globalMovieService.getReviewsByMovieId(id);
		log.info("Retrieved {} reviews for movie {}",(movieReviews!=null?movieReviews.size():0),id);
		if(movieReviews!=null) {
			List<Review> reviews= new ArrayList<>();
			movieReviews.stream().forEach(r -> {
				Review review = new Review();
				review.setExternalId(r.getId());
				review.setContent(r.getContent());
				review.setAuthor(r.getAuthor());
				review.setUrl(r.getUrl());
				//log.info("{}",review);
				reviews.add(review);
			});
			movie.getReviews().addAll(reviews);
		}
		
		List<ProductionCompany> productionCompanies = movieEntity.getProductionCompanies();
		if(!AppUtil.isEmptyOrNull(productionCompanies)) {
			List<Company> companies = new ArrayList<>();
			productionCompanies.stream().forEach(productionCompany -> {
				Company existingCompany = companyService.findOneByExternalId(productionCompany.getId());
				if(existingCompany!=null)
					companies.add(existingCompany);
				else {
					try {
						Company company = companyService.addCompanyByExternalId(productionCompany.getId());
						companies.add(company);
					} catch (Exception e) {
						log.error("ERROR = "+e.getMessage());
					}
				}
			});
			movie.getProductionCompanies().addAll(companies);
		}
		
		// Adding Credits
		Credits movieCredits = null;
		final com.moviespace.app.domain.Credits credits =  new com.moviespace.app.domain.Credits();
		movieCredits = globalMovieService.getCreditsByMovieId(id);
		if(movieCredits!=null) {
			if(movieCredits.getCast()!=null) {
				movieCredits.getCast().stream().forEach(c->{
					//log.info("Adding Cast {} {}",c.getCastId(),c.getName());
					PersonCast personCast = new PersonCast();
					personCast.setExternalId(c.getId());
					personCast.setOrder(c.getOrder());
					personCast.setCharacter(c.getCharacter());
					Person p = personService.findOneByExternalId(c.getId());
					if(p!=null) {
						p.setType(AppConstants.PersonType.CAST.value());
						personCast.setPerson(p);
					}
					else {
					PersonPeople personInfo = globalPeopleService.getPersonById(c.getId());
					Person person = convertPersonPeopletoPerson(personInfo);
					person.setType(AppConstants.PersonType.CAST.value());
					personCast.setPerson(person);
					}
					credits.getCasts().add(personCast);
				});
			}
			if(movieCredits.getCrew()!=null) {
				movieCredits.getCrew().stream().forEach(c->{
					//log.info("Adding Crew {} {}",c.getId(),c.getName());
					PersonCrew personCrew = new PersonCrew();
					personCrew.setDepartment(c.getDepartment());
					personCrew.setJob(c.getJob());
					personCrew.setExternalId(c.getId());
					Person p = personService.findOneByExternalId(c.getId());
					if(p!=null) {
						p.setType(AppConstants.PersonType.CREW.value());
						personCrew.setPerson(p);
					}
					else {
					PersonPeople personInfo = globalPeopleService.getPersonById(c.getId());
					Person person = convertPersonPeopletoPerson(personInfo);
					person.setType(AppConstants.PersonType.CREW.value());
					personCrew.setPerson(person);
					}
					credits.getCrews().add(personCrew);
				});
			}
		}
		movie.setCredits(credits);
		
		
		return movie;
	}
	
	private Person convertPersonPeopletoPerson(PersonPeople personInfo) {
	Person person = null;
	if(personInfo==null)
		return null;
	person = new Person();
	person.setExternalId(personInfo.getId());
	person.setImdbId(personInfo.getImdbId());
	person.setName(personInfo.getName());
	person.setBiography(personInfo.getBiography());
	person.setPopularity(personInfo.getPopularity());
	person.setProfilePath(AppUtil.isEmptyOrNull(personInfo.getProfilePath())?null:personInfo.getProfilePath());
	person.setBirthday(AppUtil.convertYYYYMMDDToZonedDateTime(personInfo.getBirthday()));
	person.setDeathday(AppUtil.convertYYYYMMDDToZonedDateTime(personInfo.getDeathday()));
	person.setBirthPlace(personInfo.getBirthplace());
	person.setAka(personInfo.getAka()!=null?String.join(",", personInfo.getAka()):null);
	person.setHomepage(personInfo.getHomepage());
	return person;	
	}
	
	
public MovieDTO saveMovieToMySpace(Integer externalId) throws MovieSpaceException {
		MovieDTO dto = new MovieDTO();
		dto.setExternalId(externalId);
		dto = save(dto);
		if(dto.getId()!=null)
			return dto;
		return null;
	}

@Transactional(readOnly = true)
public List<Movie> getMoviesByGenre(Long genreId) {
	List<Movie> movies = movieRepository.findAllByGenres(genreId);
	//List<MovieDTO> movieList = new ArrayList<>();
		if(!AppUtil.isEmptyOrNull(movies)) {
			//movieList = movies.stream().map(movieMapper::movieToMovieDTO).collect(Collectors.toCollection(ArrayList::new));
			movies.stream().forEach(movie -> {
				movie.setBackdropPath(AppUtil.resolveImageUrl(movie.getBackdropPath(),AppConstants.BackdropSize.W300.value()));
				movie.setPosterPath(AppUtil.resolveImageUrl(movie.getPosterPath(),AppConstants.PosterSize.W342.value()));
			});
		}
		return movies;
}

@Transactional(readOnly = true)
public List<Movie> getMoviesByCast(Long genreId) {
	List<Movie> movies = movieRepository.findAllByCast(genreId);
	//List<MovieDTO> movieList = new ArrayList<>();
		if(!AppUtil.isEmptyOrNull(movies)) {
			//movieList = movies.stream().map(movieMapper::movieToMovieDTO).collect(Collectors.toCollection(ArrayList::new));
			movies.stream().forEach(movie -> {
				movie.setBackdropPath(AppUtil.resolveImageUrl(movie.getBackdropPath(),AppConstants.BackdropSize.W300.value()));
				movie.setPosterPath(AppUtil.resolveImageUrl(movie.getPosterPath(),AppConstants.PosterSize.W342.value()));
			});
		}
		return movies;
}
	
@Transactional(readOnly = true)
public List<Movie> getMoviesByCrew(Long genreId) {
	List<Movie> movies = movieRepository.findAllByCrew(genreId);
	//List<MovieDTO> movieList = new ArrayList<>();
	if(!AppUtil.isEmptyOrNull(movies)) {
		//movieList = movies.stream().map(movieMapper::movieToMovieDTO).collect(Collectors.toCollection(ArrayList::new));
		movies.stream().forEach(movie -> {
			movie.setBackdropPath(AppUtil.resolveImageUrl(movie.getBackdropPath(),AppConstants.BackdropSize.W300.value()));
			movie.setPosterPath(AppUtil.resolveImageUrl(movie.getPosterPath(),AppConstants.PosterSize.W342.value()));
		});
	}
	return movies;
}
	

@Transactional(readOnly = true)
public List<Movie> getMoviesByCompany(Long companyId) {
	List<Movie> movies = movieRepository.findAllByCompany(companyId);
	//List<MovieDTO> movieList = new ArrayList<>();
	if(!AppUtil.isEmptyOrNull(movies)) {
		//movieList = movies.stream().map(movieMapper::movieToMovieDTO).collect(Collectors.toCollection(ArrayList::new));
		movies.stream().forEach(movie -> {
			movie.setBackdropPath(AppUtil.resolveImageUrl(movie.getBackdropPath(),AppConstants.BackdropSize.W300.value()));
			movie.setPosterPath(AppUtil.resolveImageUrl(movie.getPosterPath(),AppConstants.PosterSize.W342.value()));
		});
	}
	return movies;
}


}
