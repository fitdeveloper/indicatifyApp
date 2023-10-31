package ma.indicatify.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class KnowledgeDomainSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name_knowledge_domain", table, columnPrefix + "_name_knowledge_domain"));
        columns.add(Column.aliased("desc_knowledge_domain", table, columnPrefix + "_desc_knowledge_domain"));

        return columns;
    }
}
