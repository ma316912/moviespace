package com.moviespace.app.service.util;

import java.time.ZonedDateTime;
import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import info.movito.themoviedbapi.model.people.PersonType;

public class AppUtil {

	
	public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
	
	
	public static String resolveImageUrl(String url,String size) {
		if(isEmptyOrNull(url))
			return null;
		String absoluteUrl = "";
		absoluteUrl = IMAGE_BASE_URL;
		//absoluteUrl = absoluteUrl + "/" + ( isEmptyOrNull(size) ? "original": size ) + "" + url + "?"+AppConstants.API_KEY_KEY+"="+AppConstants.API_KEY_VALUE;		
		absoluteUrl = absoluteUrl + "/" + ( isEmptyOrNull(size) ? "original": size ) + "" + url;
		return absoluteUrl;
	}
	
	
	
	public static boolean isEmptyOrNull(String str) {
		return (str == null || str.isEmpty());
	}
	
	
	public static boolean isEmptyOrNull(Collection collection) {
		return (collection==null || collection.isEmpty());
	}
	
	public static ZonedDateTime convertYYYYMMDDToZonedDateTime(String date) {
		if(isEmptyOrNull(date))
			return null;
		DateTime fd = null;
		if(date.length()==4)
			fd = DateTimeFormat.forPattern("yyyy").parseDateTime(date);
		else
			fd = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(date);
		return convertDateTimeToZonedDateTime(fd);
	}
	
	public static ZonedDateTime convertDateTimeToZonedDateTime(DateTime dateTime) {
		return dateTime.toGregorianCalendar().toZonedDateTime();
	}
	
	public static String getPersonType(Enum type) {
		if(type.equals(PersonType.CAST))
			return "cast";
		else if(type.equals(PersonType.CREW))
			return "crew";
		else
			return "person";
	}
	
}
