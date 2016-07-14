package com.moviespace.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "external_id")
    private Integer externalId;

    @NotNull
    @Size(max = 50)
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "original_title")
    private String originalTitle;

    @Column(name = "popularity")
    private Float popularity;

    @Column(name = "backdrop_path")
    private String backdropPath;

    @Column(name = "poster_path")
    private String posterPath;

    @Column(name = "release_date")
    private ZonedDateTime releaseDate;

    @Column(name = "adult")
    private Boolean adult;

    @Column(name = "budget")
    private Long budget;

    @Column(name = "home_page")
    private String homePage;

    @Column(name = "overview")
    private String overview;

    @Column(name = "imdb_id")
    private String imdbId;

    @Column(name = "revenue")
    private Long revenue;

    @Column(name = "runtime")
    private Integer runtime;

    @Column(name = "tagline")
    private String tagline;

    @Column(name = "user_rating")
    private Float userRating;

    @Column(name = "vote_average")
    private Float voteAverage;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Column(name = "status")
    private String status;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "movie_genre",
               joinColumns = @JoinColumn(name="movies_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="genres_id", referencedColumnName="ID"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "movie_production_company",
               joinColumns = @JoinColumn(name="movies_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="production_companies_id", referencedColumnName="ID"))
    private Set<Company> productionCompanies = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "movie_production_country",
               joinColumns = @JoinColumn(name="movies_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="production_countries_id", referencedColumnName="ID"))
    private Set<Country> productionCountries = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "movie_spoken_language",
               joinColumns = @JoinColumn(name="movies_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="spoken_languages_id", referencedColumnName="ID"))
    private Set<Language> spokenLanguages = new HashSet<>();

    @OneToMany(mappedBy = "movie",cascade = CascadeType.REMOVE)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Review> reviews = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private Credits credits;

    @ManyToOne
    private Collection belongsToCollection;

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

    public Boolean isAdult() {
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

    public Set<Language> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(Set<Language> languages) {
        this.spokenLanguages = languages;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Credits getCredits() {
        return credits;
    }

    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    public Collection getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(Collection collection) {
        this.belongsToCollection = collection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        if(movie.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Movie{" +
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
            ", genres='"+ genres!=null?genres.toString():"" +"'"+
       		", languages='"+ spokenLanguages!=null?spokenLanguages.toString():"" +"'"+
            '}';
    }
}
