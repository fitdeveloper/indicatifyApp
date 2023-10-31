package ma.indicatify.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SiteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name_site", table, columnPrefix + "_name_site"));
        columns.add(Column.aliased("address_site", table, columnPrefix + "_address_site"));
        columns.add(Column.aliased("desc_site", table, columnPrefix + "_desc_site"));

        return columns;
    }
}
