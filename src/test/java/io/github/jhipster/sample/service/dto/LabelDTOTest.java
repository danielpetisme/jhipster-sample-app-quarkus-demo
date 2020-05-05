package io.github.jhipster.sample.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.jhipster.sample.TestUtil;

public class LabelDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LabelDTO.class);
        LabelDTO labelDTO1 = new LabelDTO();
        labelDTO1.id = 1L;
        LabelDTO labelDTO2 = new LabelDTO();
        assertThat(labelDTO1).isNotEqualTo(labelDTO2);
        labelDTO2.id = labelDTO1.id;
        assertThat(labelDTO1).isEqualTo(labelDTO2);
        labelDTO2.id = 2L;
        assertThat(labelDTO1).isNotEqualTo(labelDTO2);
        labelDTO1.id = null;
        assertThat(labelDTO1).isNotEqualTo(labelDTO2);
    }
}
