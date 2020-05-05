package io.github.jhipster.sample.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

/**
 * A Operation.
 */
@Entity
@Table(name = "operation")
public class Operation extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    public Instant date;

    @Column(name = "description")
    public String description;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false, unique = true)
    public BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    @JsonbTransient
    public BankAccount bankAccount;

    @ManyToMany
    @JoinTable(name = "operation_label",
               joinColumns = @JoinColumn(name = "operation_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
    @JsonbTransient
    public Set<Label> labels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operation)) {
            return false;
        }
        return id != null && id.equals(((Operation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Operation{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", description='" + description + "'" +
            ", amount=" + amount +
            "}";
    }
}
