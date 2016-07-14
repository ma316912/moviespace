package com.moviespace.app.service.util;

public class AppConstants {

	public final static String API_KEY_KEY = "api_key";
	public final static String API_KEY_VALUE = "c8a9820e15d305d084c1847470191f3f";
	
	public enum PosterSize {
		ORIGINAL("original"),
		W92("w92"),
		W154("w154"),
		W185("w185"),
		W342("w342"),
		W500("w500"),
		W780("w780");
		
		private final String value;
		PosterSize(final String value) {
			this.value = value;
		}
		
		public String value() {
	        return value;
	    }

	    @Override
	    public String toString() {
	        return this.value();
	    }
	}
	
	
	public enum BackdropSize {
		ORIGINAL("original"),
		W300("w300"),
		W780("w780"),
		W1280("w1280");
		
		private final String value;
		
		BackdropSize(final String value) {
			this.value = value;
		}
		
		public String value() {
	        return value;
	    }

	    @Override
	    public String toString() {
	        return this.value();
	    }
	}
	
	
	public enum ProfileSize {
		ORIGINAL("original"),
		W45("w45"),
		W185("w185"),
		H632("h632");
		
		private final String value;
		
		ProfileSize(final String value) {
			this.value = value;
		}
		
		public String value() {
	        return value;
	    }

	    @Override
	    public String toString() {
	        return this.value();
	    }
	}
	
	public enum PersonType {
		PERSON("person"),
		CAST("cast"),
		CREW("crew");
		
		private final String value;
		
		PersonType(final String value) {
			this.value = value;
		}
		
		public String value() {
	        return value;
	    }

	    @Override
	    public String toString() {
	        return this.value();
	    }
	}
	
	
	public enum Language {
		EN("en");
		private final String value;

		Language(final String value) {
	        this.value = value;
	    }

	    public String value() {
	        return value;
	    }

	    @Override
	    public String toString() {
	        return this.value();
	    }

	}
	
	
	public enum ImageSize {
		
	}
	
	
}
