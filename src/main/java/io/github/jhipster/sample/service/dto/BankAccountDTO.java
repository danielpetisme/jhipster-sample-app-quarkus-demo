package io.github.jhipster.sample.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.jhipster.sample.domain.BankAccount} entity.
 */
public class BankAccountDTO implements Serializable {
    
    public Long id;

    @NotNull
    public String name;

    @NotNull
    public BigDecimal balance;

    public Long userId;
    public String userLogin;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankAccountDTO)) {
            return false;
        }

        return id != null && id.equals(((BankAccountDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BankAccountDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", balance=" + balance +
            ", userId=" + userId +
            ", userLogin='" + userLogin + "'" +
            "}";
    }
}
