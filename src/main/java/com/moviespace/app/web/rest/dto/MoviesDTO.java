package com.moviespace.app.web.rest.dto;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MoviesDTO {

	private List<MovieDb> popularMovies = new ArrayList<>();
	private List<MovieDb> upcomingMovies = new ArrayList<>();
	private List<MovieDb> latestMovies = new ArrayList<>();
	private List<MovieDb> topRatedMovies = new ArrayList<>();

	public MoviesDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public MoviesDTO(List<MovieDb> popularMovies,List<MovieDb> upcomingMovies,List<MovieDb> latestMovies,List<MovieDb> topRatedMovies) {
		this.popularMovies = popularMovies;
		this.upcomingMovies = upcomingMovies;
		this.latestMovies = latestMovies;
		this.topRatedMovies = topRatedMovies;
	}
	
	public List<MovieDb> getPopularMovies() {
		return popularMovies!=null?popularMovies:new ArrayList<>();
	}

	public void setPopularMovies(List<MovieDb> popularMovies) {
		this.popularMovies = popularMovies;
	}

	public List<MovieDb> getUpcomingMovies() {
		return upcomingMovies!=null?upcomingMovies:new ArrayList<>();
	}

	public void setUpcomingMovies(List<MovieDb> upcomingMovies) {
		this.upcomingMovies = upcomingMovies;
	}

	public List<MovieDb> getLatestMovies() {
		return latestMovies!=null?latestMovies:new ArrayList<>();
	}

	public void setLatestMovies(List<MovieDb> latestMovies) {
		this.latestMovies = latestMovies;
	}

	public List<MovieDb> getTopRatedMovies() {
		return topRatedMovies!=null?topRatedMovies:new ArrayList<>();
	}

	public void setTopRatedMovies(List<MovieDb> topRatedMovies) {
		this.topRatedMovies = topRatedMovies;
	}

	@Override
	public String toString() {
		return "MoviesDTO [" + (popularMovies != null ? "popularMovies=" + popularMovies + ", " : "")
				+ (upcomingMovies != null ? "upcomingMovies=" + upcomingMovies + ", " : "")
				+ (latestMovies != null ? "latestMovies=" + latestMovies + ", " : "")
				+ (topRatedMovies != null ? "topRatedMovies=" + topRatedMovies : "") + "]";
	}
	
	
	
}
