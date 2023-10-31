package ma.indicatify.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ma.indicatify.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerimeterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerimeterDTO.class);
        PerimeterDTO perimeterDTO1 = new PerimeterDTO();
        perimeterDTO1.setId(1L);
        PerimeterDTO perimeterDTO2 = new PerimeterDTO();
        assertThat(perimeterDTO1).isNotEqualTo(perimeterDTO2);
        perimeterDTO2.setId(perimeterDTO1.getId());
        assertThat(perimeterDTO1).isEqualTo(perimeterDTO2);
        perimeterDTO2.setId(2L);
        assertThat(perimeterDTO1).isNotEqualTo(perimeterDTO2);
        perimeterDTO1.setId(null);
        assertThat(perimeterDTO1).isNotEqualTo(perimeterDTO2);
    }
}
