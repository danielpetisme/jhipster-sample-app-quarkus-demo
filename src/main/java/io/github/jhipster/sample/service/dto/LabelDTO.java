package io.github.jhipster.sample.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.jhipster.sample.domain.Label} entity.
 */
public class LabelDTO implements Serializable {
    
    public Long id;

    @NotNull
    @Size(min = 3)
    public String label;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LabelDTO)) {
            return false;
        }

        return id != null && id.equals(((LabelDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LabelDTO{" +
            "id=" + id +
            ", label='" + label + "'" +
            "}";
    }
}
