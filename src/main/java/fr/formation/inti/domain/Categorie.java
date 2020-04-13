package fr.formation.inti.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

import fr.formation.inti.domain.enumeration.Section;

/**
 * A Categorie.
 */
@Entity
@Table(name = "categorie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Categorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "section")
    private Section section;

    @Column(name = "descrition")
    private String descrition;

    @ManyToMany(mappedBy = "categories")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Club> clubs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Section getSection() {
        return section;
    }

    public Categorie section(Section section) {
        this.section = section;
        return this;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getDescrition() {
        return descrition;
    }

    public Categorie descrition(String descrition) {
        this.descrition = descrition;
        return this;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public Set<Club> getClubs() {
        return clubs;
    }

    public Categorie clubs(Set<Club> clubs) {
        this.clubs = clubs;
        return this;
    }

    public Categorie addClub(Club club) {
        this.clubs.add(club);
        club.getCategories().add(this);
        return this;
    }

    public Categorie removeClub(Club club) {
        this.clubs.remove(club);
        club.getCategories().remove(this);
        return this;
    }

    public void setClubs(Set<Club> clubs) {
        this.clubs = clubs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categorie)) {
            return false;
        }
        return id != null && id.equals(((Categorie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Categorie{" +
            "id=" + getId() +
            ", section='" + getSection() + "'" +
            ", descrition='" + getDescrition() + "'" +
            "}";
    }
}
