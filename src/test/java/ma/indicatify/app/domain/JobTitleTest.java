package ma.indicatify.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.indicatify.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobTitleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTitle.class);
        JobTitle jobTitle1 = new JobTitle();
        jobTitle1.setId(1L);
        JobTitle jobTitle2 = new JobTitle();
        jobTitle2.setId(jobTitle1.getId());
        assertThat(jobTitle1).isEqualTo(jobTitle2);
        jobTitle2.setId(2L);
        assertThat(jobTitle1).isNotEqualTo(jobTitle2);
        jobTitle1.setId(null);
        assertThat(jobTitle1).isNotEqualTo(jobTitle2);
    }
}
