package ma.indicatify.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ma.indicatify.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KnowledgeDomainDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KnowledgeDomainDTO.class);
        KnowledgeDomainDTO knowledgeDomainDTO1 = new KnowledgeDomainDTO();
        knowledgeDomainDTO1.setId(1L);
        KnowledgeDomainDTO knowledgeDomainDTO2 = new KnowledgeDomainDTO();
        assertThat(knowledgeDomainDTO1).isNotEqualTo(knowledgeDomainDTO2);
        knowledgeDomainDTO2.setId(knowledgeDomainDTO1.getId());
        assertThat(knowledgeDomainDTO1).isEqualTo(knowledgeDomainDTO2);
        knowledgeDomainDTO2.setId(2L);
        assertThat(knowledgeDomainDTO1).isNotEqualTo(knowledgeDomainDTO2);
        knowledgeDomainDTO1.setId(null);
        assertThat(knowledgeDomainDTO1).isNotEqualTo(knowledgeDomainDTO2);
    }
}
