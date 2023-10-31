package ma.indicatify.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EmployeeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("firstname_employee", table, columnPrefix + "_firstname_employee"));
        columns.add(Column.aliased("lastname_employee", table, columnPrefix + "_lastname_employee"));
        columns.add(Column.aliased("matriculation_number_employee", table, columnPrefix + "_matriculation_number_employee"));
        columns.add(Column.aliased("date_of_birth_employee", table, columnPrefix + "_date_of_birth_employee"));
        columns.add(Column.aliased("email_employee", table, columnPrefix + "_email_employee"));
        columns.add(Column.aliased("phone_employee", table, columnPrefix + "_phone_employee"));
        columns.add(Column.aliased("hire_date_employee", table, columnPrefix + "_hire_date_employee"));
        columns.add(Column.aliased("gender_employee", table, columnPrefix + "_gender_employee"));
        columns.add(Column.aliased("desc_employee", table, columnPrefix + "_desc_employee"));

        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        columns.add(Column.aliased("superior_employee_id", table, columnPrefix + "_superior_employee_id"));
        columns.add(Column.aliased("job_title_id", table, columnPrefix + "_job_title_id"));
        columns.add(Column.aliased("level_id", table, columnPrefix + "_level_id"));
        columns.add(Column.aliased("site_id", table, columnPrefix + "_site_id"));
        return columns;
    }
}
