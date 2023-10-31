package ma.indicatify.app.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import ma.indicatify.app.domain.Perimeter;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Perimeter}, with proper type conversions.
 */
@Service
public class PerimeterRowMapper implements BiFunction<Row, String, Perimeter> {

    private final ColumnConverter converter;

    public PerimeterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Perimeter} stored in the database.
     */
    @Override
    public Perimeter apply(Row row, String prefix) {
        Perimeter entity = new Perimeter();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNamePerimeter(converter.fromRow(row, prefix + "_name_perimeter", String.class));
        entity.setDescPerimeter(converter.fromRow(row, prefix + "_desc_perimeter", String.class));
        entity.setActivityId(converter.fromRow(row, prefix + "_activity_id", Long.class));
        return entity;
    }
}
