package ma.indicatify.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Perimeter.
 */
@Table("perimeter")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Perimeter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name_perimeter")
    private String namePerimeter;

    @Column("desc_perimeter")
    private String descPerimeter;

    @Transient
    @JsonIgnoreProperties(value = { "perimeters" }, allowSetters = true)
    private Activity activity;

    @Transient
    @JsonIgnoreProperties(
        value = { "user", "superiorEmployee", "jobTitle", "level", "knowledgeDomains", "perimeters", "site", "subordinateEmployees" },
        allowSetters = true
    )
    private Set<Employee> employees = new HashSet<>();

    @Column("activity_id")
    private Long activityId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Perimeter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamePerimeter() {
        return this.namePerimeter;
    }

    public Perimeter namePerimeter(String namePerimeter) {
        this.setNamePerimeter(namePerimeter);
        return this;
    }

    public void setNamePerimeter(String namePerimeter) {
        this.namePerimeter = namePerimeter;
    }

    public String getDescPerimeter() {
        return this.descPerimeter;
    }

    public Perimeter descPerimeter(String descPerimeter) {
        this.setDescPerimeter(descPerimeter);
        return this;
    }

    public void setDescPerimeter(String descPerimeter) {
        this.descPerimeter = descPerimeter;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        this.activityId = activity != null ? activity.getId() : null;
    }

    public Perimeter activity(Activity activity) {
        this.setActivity(activity);
        return this;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.removePerimeter(this));
        }
        if (employees != null) {
            employees.forEach(i -> i.addPerimeter(this));
        }
        this.employees = employees;
    }

    public Perimeter employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Perimeter addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getPerimeters().add(this);
        return this;
    }

    public Perimeter removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getPerimeters().remove(this);
        return this;
    }

    public Long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Long activity) {
        this.activityId = activity;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Perimeter)) {
            return false;
        }
        return id != null && id.equals(((Perimeter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Perimeter{" +
            "id=" + getId() +
            ", namePerimeter='" + getNamePerimeter() + "'" +
            ", descPerimeter='" + getDescPerimeter() + "'" +
            "}";
    }
}
