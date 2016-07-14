package com.moviespace.app.web.rest.dto;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.moviespace.app.domain.Movie;

public class CollectionDTO {

    private Long id;

    private Integer externalId;

    private String name;

    private String title;

    private String overview;

    private String posterPath;

    private String backdropPath;

    private ZonedDateTime releaseDate;

    private Set<Movie> parts = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}

	public String getBackdropPath() {
		return backdropPath;
	}

	public void setBackdropPath(String backdropPath) {
		this.backdropPath = backdropPath;
	}

	public ZonedDateTime getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(ZonedDateTime releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Set<Movie> getParts() {
		return parts;
	}

	public void setParts(Set<Movie> parts) {
		this.parts = parts;
	}

	@Override
	public String toString() {
		return "CollectionDTO [id=" + id + ", externalId=" + externalId + ", name=" + name + ", title=" + title
				+ ", overview=" + overview + ", posterPath=" + posterPath + ", backdropPath=" + backdropPath
				+ ", releaseDate=" + releaseDate +"]";
	}

    
    
    
}
