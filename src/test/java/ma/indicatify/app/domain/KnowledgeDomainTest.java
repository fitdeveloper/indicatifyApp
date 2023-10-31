package ma.indicatify.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.indicatify.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KnowledgeDomainTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KnowledgeDomain.class);
        KnowledgeDomain knowledgeDomain1 = new KnowledgeDomain();
        knowledgeDomain1.setId(1L);
        KnowledgeDomain knowledgeDomain2 = new KnowledgeDomain();
        knowledgeDomain2.setId(knowledgeDomain1.getId());
        assertThat(knowledgeDomain1).isEqualTo(knowledgeDomain2);
        knowledgeDomain2.setId(2L);
        assertThat(knowledgeDomain1).isNotEqualTo(knowledgeDomain2);
        knowledgeDomain1.setId(null);
        assertThat(knowledgeDomain1).isNotEqualTo(knowledgeDomain2);
    }
}
