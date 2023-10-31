package ma.indicatify.app.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import ma.indicatify.app.domain.Level;
import ma.indicatify.app.domain.enumeration.LevelEnum;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Level}, with proper type conversions.
 */
@Service
public class LevelRowMapper implements BiFunction<Row, String, Level> {

    private final ColumnConverter converter;

    public LevelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Level} stored in the database.
     */
    @Override
    public Level apply(Row row, String prefix) {
        Level entity = new Level();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNameLevel(converter.fromRow(row, prefix + "_name_level", String.class));
        entity.setValueLevel(converter.fromRow(row, prefix + "_value_level", LevelEnum.class));
        entity.setDescLevel(converter.fromRow(row, prefix + "_desc_level", String.class));
        return entity;
    }
}
