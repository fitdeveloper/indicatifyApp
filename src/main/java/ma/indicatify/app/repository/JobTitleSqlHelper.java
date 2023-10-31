package ma.indicatify.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class JobTitleSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name_job_title", table, columnPrefix + "_name_job_title"));
        columns.add(Column.aliased("desc_job_title", table, columnPrefix + "_desc_job_title"));

        return columns;
    }
}
