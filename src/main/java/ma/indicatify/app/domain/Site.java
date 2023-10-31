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
 * A Site.
 */
@Table("site")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name_site")
    private String nameSite;

    @NotNull(message = "must not be null")
    @Column("address_site")
    private String addressSite;

    @Column("desc_site")
    private String descSite;

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

    public Site id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameSite() {
        return this.nameSite;
    }

    public Site nameSite(String nameSite) {
        this.setNameSite(nameSite);
        return this;
    }

    public void setNameSite(String nameSite) {
        this.nameSite = nameSite;
    }

    public String getAddressSite() {
        return this.addressSite;
    }

    public Site addressSite(String addressSite) {
        this.setAddressSite(addressSite);
        return this;
    }

    public void setAddressSite(String addressSite) {
        this.addressSite = addressSite;
    }

    public String getDescSite() {
        return this.descSite;
    }

    public Site descSite(String descSite) {
        this.setDescSite(descSite);
        return this;
    }

    public void setDescSite(String descSite) {
        this.descSite = descSite;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setSite(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setSite(this));
        }
        this.employees = employees;
    }

    public Site employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Site addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setSite(this);
        return this;
    }

    public Site removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setSite(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        return id != null && id.equals(((Site) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", nameSite='" + getNameSite() + "'" +
            ", addressSite='" + getAddressSite() + "'" +
            ", descSite='" + getDescSite() + "'" +
            "}";
    }
}
