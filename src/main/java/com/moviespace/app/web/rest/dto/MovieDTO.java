package com.moviespace.app.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;

import com.moviespace.app.domain.Company;
import com.moviespace.app.domain.Country;
import com.moviespace.app.domain.Credits;
import com.moviespace.app.domain.Genre;
import com.moviespace.app.domain.Review;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Movie entity.
 */
public class MovieDTO implements Serializable {

    private Long id;

    private Integer externalId;

    @NotNull
    @Size(max = 50)
    private String title;

    private String originalTitle;

    private Float popularity;

    private String backdropPath;

    private String posterPath;

    private ZonedDateTime releaseDate;

    private Boolean adult;

    private Long budget;

    private String homePage;

    private String overview;

    private String imdbId;

    private Long revenue;

    private Integer runtime;

    private String tagline;

    private Float userRating;

    private Float voteAverage;

    private Integer voteCount;

    private String status;


    private Set<Genre> genres = new HashSet<>();

    private Set<Company> productionCompanies = new HashSet<>();

    private Set<Country> productionCountries = new HashSet<>();

    private Set<LanguageDTO> spokenLanguages = new HashSet<>();
    
    private Set<Review> reviews = new HashSet<>();
    
    private CreditsDTO credit;

    private Long creditsId;
    
    private Long belongsToCollectionId;
    

    
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }
    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    public ZonedDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }
    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }
    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }
    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }
    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }
    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }
    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
    public Float getUserRating() {
        return userRating;
    }

    public void setUserRating(Float userRating) {
        this.userRating = userRating;
    }
    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }
    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Company> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(Set<Company> companies) {
        this.productionCompanies = companies;
    }

    public Set<Country> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(Set<Country> countries) {
        this.productionCountries = countries;
    }

    public Set<LanguageDTO> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(Set<LanguageDTO> languages) {
        this.spokenLanguages = languages;
    }

    public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public CreditsDTO getCredit() {
		return credit;
	}

	public void setCredit(CreditsDTO credits) {
		this.credit = credits;
	}

	public Long getCreditsId() {
        return creditsId;
    }

    public void setCreditsId(Long creditsId) {
        this.creditsId = creditsId;
    }

    public Long getBelongsToCollectionId() {
        return belongsToCollectionId;
    }

    public void setBelongsToCollectionId(Long collectionId) {
        this.belongsToCollectionId = collectionId;
    }


	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MovieDTO movieDTO = (MovieDTO) o;

        if ( ! Objects.equals(id, movieDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MovieDTO{" +
            "id=" + id +
            ", externalId='" + externalId + "'" +
            ", title='" + title + "'" +
            ", originalTitle='" + originalTitle + "'" +
            ", popularity='" + popularity + "'" +
            ", backdropPath='" + backdropPath + "'" +
            ", posterPath='" + posterPath + "'" +
            ", releaseDate='" + releaseDate + "'" +
            ", adult='" + adult + "'" +
            ", budget='" + budget + "'" +
            ", homePage='" + homePage + "'" +
            ", overview='" + overview + "'" +
            ", imdbId='" + imdbId + "'" +
            ", revenue='" + revenue + "'" +
            ", runtime='" + runtime + "'" +
            ", tagline='" + tagline + "'" +
            ", userRating='" + userRating + "'" +
            ", voteAverage='" + voteAverage + "'" +
            ", voteCount='" + voteCount + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
