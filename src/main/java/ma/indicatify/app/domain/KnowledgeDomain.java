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
 * A KnowledgeDomain.
 */
@Table("knowledge_domain")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KnowledgeDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name_knowledge_domain")
    private String nameKnowledgeDomain;

    @Column("desc_knowledge_domain")
    private String descKnowledgeDomain;

    @Transient
    @JsonIgnoreProperties(
        value = { "user", "superiorEmployee", "jobTitle", "level", "knowledgeDomains", "perimeters", "site", "subordinateEmployees" },
        allowSetters = true
    )
    private Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KnowledgeDomain id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameKnowledgeDomain() {
        return this.nameKnowledgeDomain;
    }

    public KnowledgeDomain nameKnowledgeDomain(String nameKnowledgeDomain) {
        this.setNameKnowledgeDomain(nameKnowledgeDomain);
        return this;
    }

    public void setNameKnowledgeDomain(String nameKnowledgeDomain) {
        this.nameKnowledgeDomain = nameKnowledgeDomain;
    }

    public String getDescKnowledgeDomain() {
        return this.descKnowledgeDomain;
    }

    public KnowledgeDomain descKnowledgeDomain(String descKnowledgeDomain) {
        this.setDescKnowledgeDomain(descKnowledgeDomain);
        return this;
    }

    public void setDescKnowledgeDomain(String descKnowledgeDomain) {
        this.descKnowledgeDomain = descKnowledgeDomain;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.removeKnowledgeDomain(this));
        }
        if (employees != null) {
            employees.forEach(i -> i.addKnowledgeDomain(this));
        }
        this.employees = employees;
    }

    public KnowledgeDomain employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public KnowledgeDomain addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getKnowledgeDomains().add(this);
        return this;
    }

    public KnowledgeDomain removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getKnowledgeDomains().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KnowledgeDomain)) {
            return false;
        }
        return id != null && id.equals(((KnowledgeDomain) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KnowledgeDomain{" +
            "id=" + getId() +
            ", nameKnowledgeDomain='" + getNameKnowledgeDomain() + "'" +
            ", descKnowledgeDomain='" + getDescKnowledgeDomain() + "'" +
            "}";
    }
}
