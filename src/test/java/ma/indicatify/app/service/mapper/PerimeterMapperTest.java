package ma.indicatify.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PerimeterMapperTest {

    private PerimeterMapper perimeterMapper;

    @BeforeEach
    public void setUp() {
        perimeterMapper = new PerimeterMapperImpl();
    }
}
