package ma.indicatify.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KnowledgeDomainMapperTest {

    private KnowledgeDomainMapper knowledgeDomainMapper;

    @BeforeEach
    public void setUp() {
        knowledgeDomainMapper = new KnowledgeDomainMapperImpl();
    }
}
