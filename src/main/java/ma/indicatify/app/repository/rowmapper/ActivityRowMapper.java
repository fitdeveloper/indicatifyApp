package ma.indicatify.app.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import ma.indicatify.app.domain.Activity;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Activity}, with proper type conversions.
 */
@Service
public class ActivityRowMapper implements BiFunction<Row, String, Activity> {

    private final ColumnConverter converter;

    public ActivityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Activity} stored in the database.
     */
    @Override
    public Activity apply(Row row, String prefix) {
        Activity entity = new Activity();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNameActivity(converter.fromRow(row, prefix + "_name_activity", String.class));
        entity.setDescActivity(converter.fromRow(row, prefix + "_desc_activity", String.class));
        return entity;
    }
}
