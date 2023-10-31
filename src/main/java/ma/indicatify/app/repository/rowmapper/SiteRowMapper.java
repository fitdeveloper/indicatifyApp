package ma.indicatify.app.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import ma.indicatify.app.domain.Site;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Site}, with proper type conversions.
 */
@Service
public class SiteRowMapper implements BiFunction<Row, String, Site> {

    private final ColumnConverter converter;

    public SiteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Site} stored in the database.
     */
    @Override
    public Site apply(Row row, String prefix) {
        Site entity = new Site();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNameSite(converter.fromRow(row, prefix + "_name_site", String.class));
        entity.setAddressSite(converter.fromRow(row, prefix + "_address_site", String.class));
        entity.setDescSite(converter.fromRow(row, prefix + "_desc_site", String.class));
        return entity;
    }
}
