package ma.indicatify.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ma.indicatify.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobTitleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTitleDTO.class);
        JobTitleDTO jobTitleDTO1 = new JobTitleDTO();
        jobTitleDTO1.setId(1L);
        JobTitleDTO jobTitleDTO2 = new JobTitleDTO();
        assertThat(jobTitleDTO1).isNotEqualTo(jobTitleDTO2);
        jobTitleDTO2.setId(jobTitleDTO1.getId());
        assertThat(jobTitleDTO1).isEqualTo(jobTitleDTO2);
        jobTitleDTO2.setId(2L);
        assertThat(jobTitleDTO1).isNotEqualTo(jobTitleDTO2);
        jobTitleDTO1.setId(null);
        assertThat(jobTitleDTO1).isNotEqualTo(jobTitleDTO2);
    }
}
