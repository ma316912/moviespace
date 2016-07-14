package com.moviespace.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Credits.
 */
@Entity
@Table(name = "credits")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Credits implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "external_id")
    private Integer externalId;

    @OneToMany(mappedBy = "credits",fetch=FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersonCast> casts = new HashSet<>();

    @OneToMany(mappedBy = "credits",fetch=FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersonCrew> crews = new HashSet<>();

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

    public Set<PersonCast> getCasts() {
        return casts;
    }

    public void setCasts(Set<PersonCast> personCasts) {
        this.casts = personCasts;
    }

    public Set<PersonCrew> getCrews() {
        return crews;
    }

    public void setCrews(Set<PersonCrew> personCrews) {
        this.crews = personCrews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Credits credits = (Credits) o;
        if(credits.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, credits.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Credits{" +
            "id=" + id +
            ", externalId='" + externalId + "'" +
            '}';
    }
}
