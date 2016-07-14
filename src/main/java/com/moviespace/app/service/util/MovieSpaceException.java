package com.moviespace.app.service.util;

import com.moviespace.app.web.rest.errors.ParameterizedErrorDTO;

public class MovieSpaceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private final String message;
    private final String[] params;

    public MovieSpaceException(String message, String... params) {
        super(message);
        this.message = message;
        this.params = params;
    }

    public ParameterizedErrorDTO getErrorDTO() {
        return new ParameterizedErrorDTO(message, params);
    }
	
}
