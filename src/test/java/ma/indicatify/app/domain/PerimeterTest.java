package ma.indicatify.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.indicatify.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerimeterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Perimeter.class);
        Perimeter perimeter1 = new Perimeter();
        perimeter1.setId(1L);
        Perimeter perimeter2 = new Perimeter();
        perimeter2.setId(perimeter1.getId());
        assertThat(perimeter1).isEqualTo(perimeter2);
        perimeter2.setId(2L);
        assertThat(perimeter1).isNotEqualTo(perimeter2);
        perimeter1.setId(null);
        assertThat(perimeter1).isNotEqualTo(perimeter2);
    }
}
