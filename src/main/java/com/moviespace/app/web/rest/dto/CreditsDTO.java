package com.moviespace.app.web.rest.dto;

import java.util.ArrayList;
import java.util.List;

import com.moviespace.app.domain.PersonCast;
import com.moviespace.app.domain.PersonCrew;

public class CreditsDTO {

	private Long id;
	
	private Integer externalId;
	
	private List<PersonCast> cast = new ArrayList<>();
	
	private List<PersonCrew> crew = new ArrayList<>();
	
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

	public List<PersonCast> getCast() {
		return cast;
	}

	public void setCast(List<PersonCast> cast) {
		this.cast = cast;
	}

	public List<PersonCrew> getCrew() {
		return crew;
	}

	public void setCrew(List<PersonCrew> crew) {
		this.crew = crew;
	}
	
	
	
	
}
