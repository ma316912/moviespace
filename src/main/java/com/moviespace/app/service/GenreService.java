package com.moviespace.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moviespace.app.domain.Genre;
import com.moviespace.app.repository.GenreRepository;
import com.moviespace.app.service.util.MovieSpaceException;

import info.movito.themoviedbapi.TmdbApi;

@Service
public class GenreService {

	private final Logger log = LoggerFactory.getLogger(GenreService.class);
	
	@Inject
	private GenreRepository genreRepository;
	
	
	@Inject
	private TmdbApi tmdbApi;
	
	@Inject
	private GlobalGenreService globalGenreService;
	
	public Genre save(Genre genre) {
		return genreRepository.save(genre);
	}
	
	public Genre addGenre(Integer id) throws MovieSpaceException {
		if(genreRepository.findByExternalId(id)!=null)
			throw new MovieSpaceException("Genre already exists");
		Genre gen = globalGenreService.getGenres().stream()
									  .filter(g -> g.getId()==id)
									  .findFirst().map(g1->
									  	{
									  	 Genre genre = new Genre();
									  	 genre.setExternalId(g1.getId());
									  	 genre.setName(g1.getName());
									  	 return genre;
									  	}).orElse(null);
		if(gen!=null)
		  gen = save(gen);
		else
		  throw new MovieSpaceException("Unable to add Genre");
	return gen;	
	}
	
	public boolean addAllGenres() {
		final List<Genre> genres = new ArrayList<>();
		List<Genre> existingGenres = findAll();
		List<info.movito.themoviedbapi.model.Genre> remainingGenres = globalGenreService.getGenres()
							.stream()
							.filter(g1 -> existingGenres.stream()
														.filter(g2 -> g1.getId()==g2.getExternalId())
														.collect(Collectors.toSet()).size()==0)
							.collect(Collectors.toList());
		remainingGenres.stream().forEach(g->{
			Genre gen = new Genre();
			gen.setExternalId(g.getId());
			gen.setName(g.getName());
			genres.add(gen);
		});
		genreRepository.save(genres);
		if(genres!=null && genres.size()!=0) {
			return true;
		}
		return false;
	}
	
    /**
     *  Get all the genres.
     *  
     *  @return the list of genres
     */
    @Transactional(readOnly = true) 
    public List<Genre> findAll() {
        log.debug("Request to get all Genres");
        List<Genre> result = genreRepository.findAll();
        return result;
    }

    /**
     *  Get one genre by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Genre findOne(Long id) {
        log.debug("Request to get Genre : {}", id);
        Genre genre = genreRepository.findOne(id);
        return genre;
    }

    /**
     *  Delete the  genre by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Genre : {}", id);
        genreRepository.delete(id);
    }
	
    @Transactional(readOnly = true) 
    public Genre findOneByExternalId(Integer id) {
        log.debug("Request to get Genre By External Id : {}", id);
        Genre genre = genreRepository.findByExternalId(id);
        return genre;
    }
    
}
