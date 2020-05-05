package io.github.jhipster.sample.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.jhipster.sample.TestUtil;

public class OperationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationDTO.class);
        OperationDTO operationDTO1 = new OperationDTO();
        operationDTO1.id = 1L;
        OperationDTO operationDTO2 = new OperationDTO();
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
        operationDTO2.id = operationDTO1.id;
        assertThat(operationDTO1).isEqualTo(operationDTO2);
        operationDTO2.id = 2L;
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
        operationDTO1.id = null;
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
    }
}
