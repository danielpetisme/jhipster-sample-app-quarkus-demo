package io.github.jhipster.sample.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.jhipster.sample.TestUtil;

public class BankAccountDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankAccountDTO.class);
        BankAccountDTO bankAccountDTO1 = new BankAccountDTO();
        bankAccountDTO1.id = 1L;
        BankAccountDTO bankAccountDTO2 = new BankAccountDTO();
        assertThat(bankAccountDTO1).isNotEqualTo(bankAccountDTO2);
        bankAccountDTO2.id = bankAccountDTO1.id;
        assertThat(bankAccountDTO1).isEqualTo(bankAccountDTO2);
        bankAccountDTO2.id = 2L;
        assertThat(bankAccountDTO1).isNotEqualTo(bankAccountDTO2);
        bankAccountDTO1.id = null;
        assertThat(bankAccountDTO1).isNotEqualTo(bankAccountDTO2);
    }
}
