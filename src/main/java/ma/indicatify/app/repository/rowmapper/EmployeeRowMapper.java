package ma.indicatify.app.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import ma.indicatify.app.domain.Employee;
import ma.indicatify.app.domain.enumeration.GenderEnum;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Employee}, with proper type conversions.
 */
@Service
public class EmployeeRowMapper implements BiFunction<Row, String, Employee> {

    private final ColumnConverter converter;

    public EmployeeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Employee} stored in the database.
     */
    @Override
    public Employee apply(Row row, String prefix) {
        Employee entity = new Employee();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFirstnameEmployee(converter.fromRow(row, prefix + "_firstname_employee", String.class));
        entity.setLastnameEmployee(converter.fromRow(row, prefix + "_lastname_employee", String.class));
        entity.setMatriculationNumberEmployee(converter.fromRow(row, prefix + "_matriculation_number_employee", String.class));
        entity.setDateOfBirthEmployee(converter.fromRow(row, prefix + "_date_of_birth_employee", LocalDate.class));
        entity.setEmailEmployee(converter.fromRow(row, prefix + "_email_employee", String.class));
        entity.setPhoneEmployee(converter.fromRow(row, prefix + "_phone_employee", String.class));
        entity.setHireDateEmployee(converter.fromRow(row, prefix + "_hire_date_employee", LocalDate.class));
        entity.setGenderEmployee(converter.fromRow(row, prefix + "_gender_employee", GenderEnum.class));
        entity.setDescEmployee(converter.fromRow(row, prefix + "_desc_employee", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setSuperiorEmployeeId(converter.fromRow(row, prefix + "_superior_employee_id", Long.class));
        entity.setJobTitleId(converter.fromRow(row, prefix + "_job_title_id", Long.class));
        entity.setLevelId(converter.fromRow(row, prefix + "_level_id", Long.class));
        entity.setSiteId(converter.fromRow(row, prefix + "_site_id", Long.class));
        return entity;
    }
}
