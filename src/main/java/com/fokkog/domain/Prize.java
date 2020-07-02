package com.fokkog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Prize.
 */
@Entity
@Table(name = "top_prize")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prize implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "prizes", allowSetters = true)
    private TableOfPrizes tableOfPrizes;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "prizes", allowSetters = true)
    private Image image;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Prize quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public TableOfPrizes getTableOfPrizes() {
        return tableOfPrizes;
    }

    public Prize tableOfPrizes(TableOfPrizes tableOfPrizes) {
        this.tableOfPrizes = tableOfPrizes;
        return this;
    }

    public void setTableOfPrizes(TableOfPrizes tableOfPrizes) {
        this.tableOfPrizes = tableOfPrizes;
    }

    public Image getImage() {
        return image;
    }

    public Prize image(Image image) {
        this.image = image;
        return this;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prize)) {
            return false;
        }
        return id != null && id.equals(((Prize) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prize{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
