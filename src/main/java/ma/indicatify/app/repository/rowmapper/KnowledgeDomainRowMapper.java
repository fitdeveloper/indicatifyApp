package ma.indicatify.app.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import ma.indicatify.app.domain.KnowledgeDomain;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link KnowledgeDomain}, with proper type conversions.
 */
@Service
public class KnowledgeDomainRowMapper implements BiFunction<Row, String, KnowledgeDomain> {

    private final ColumnConverter converter;

    public KnowledgeDomainRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link KnowledgeDomain} stored in the database.
     */
    @Override
    public KnowledgeDomain apply(Row row, String prefix) {
        KnowledgeDomain entity = new KnowledgeDomain();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNameKnowledgeDomain(converter.fromRow(row, prefix + "_name_knowledge_domain", String.class));
        entity.setDescKnowledgeDomain(converter.fromRow(row, prefix + "_desc_knowledge_domain", String.class));
        return entity;
    }
}
