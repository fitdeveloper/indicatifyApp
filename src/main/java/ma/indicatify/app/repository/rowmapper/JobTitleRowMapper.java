package ma.indicatify.app.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import ma.indicatify.app.domain.JobTitle;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link JobTitle}, with proper type conversions.
 */
@Service
public class JobTitleRowMapper implements BiFunction<Row, String, JobTitle> {

    private final ColumnConverter converter;

    public JobTitleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link JobTitle} stored in the database.
     */
    @Override
    public JobTitle apply(Row row, String prefix) {
        JobTitle entity = new JobTitle();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNameJobTitle(converter.fromRow(row, prefix + "_name_job_title", String.class));
        entity.setDescJobTitle(converter.fromRow(row, prefix + "_desc_job_title", String.class));
        return entity;
    }
}
