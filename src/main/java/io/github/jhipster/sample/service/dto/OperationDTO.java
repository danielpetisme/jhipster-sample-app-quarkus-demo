package io.github.jhipster.sample.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.jhipster.sample.domain.Operation} entity.
 */
public class OperationDTO implements Serializable {
    
    public Long id;

    @NotNull
    public Instant date;

    public String description;

    @NotNull
    public BigDecimal amount;

    public Long bankAccountId;
    public String bankAccountName;
    public Set<LabelDTO> labels = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationDTO)) {
            return false;
        }

        return id != null && id.equals(((OperationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OperationDTO{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", description='" + description + "'" +
            ", amount=" + amount +
            ", bankAccountId=" + bankAccountId +
            ", bankAccountName='" + bankAccountName + "'" +
            ", labels='" + labels + "'" +
            "}";
    }
}
