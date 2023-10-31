package ma.indicatify.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PerimeterSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name_perimeter", table, columnPrefix + "_name_perimeter"));
        columns.add(Column.aliased("desc_perimeter", table, columnPrefix + "_desc_perimeter"));

        columns.add(Column.aliased("activity_id", table, columnPrefix + "_activity_id"));
        return columns;
    }
}
