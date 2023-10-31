package ma.indicatify.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobTitleMapperTest {

    private JobTitleMapper jobTitleMapper;

    @BeforeEach
    public void setUp() {
        jobTitleMapper = new JobTitleMapperImpl();
    }
}
