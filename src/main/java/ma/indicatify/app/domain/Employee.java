package ma.indicatify.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import ma.indicatify.app.domain.enumeration.GenderEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Employee.
 */
@Table("employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("firstname_employee")
    private String firstnameEmployee;

    @NotNull(message = "must not be null")
    @Column("lastname_employee")
    private String lastnameEmployee;

    @NotNull(message = "must not be null")
    @Column("matriculation_number_employee")
    private String matriculationNumberEmployee;

    @NotNull(message = "must not be null")
    @Column("date_of_birth_employee")
    private LocalDate dateOfBirthEmployee;

    @NotNull(message = "must not be null")
    @Column("email_employee")
    private String emailEmployee;

    @Column("phone_employee")
    private String phoneEmployee;

    @Column("hire_date_employee")
    private LocalDate hireDateEmployee;

    @NotNull(message = "must not be null")
    @Column("gender_employee")
    private GenderEnum genderEmployee;

    @Column("desc_employee")
    private String descEmployee;

    @Transient
    private User user;

    @Transient
    @JsonIgnoreProperties(
        value = { "user", "superiorEmployee", "jobTitle", "level", "knowledgeDomains", "perimeters", "site", "subordinateEmployees" },
        allowSetters = true
    )
    private Employee superiorEmployee;

    @Transient
    private JobTitle jobTitle;

    @Transient
    private Level level;

    @Transient
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Set<KnowledgeDomain> knowledgeDomains = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "activity", "employees" }, allowSetters = true)
    private Set<Perimeter> perimeters = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Site site;

    @Transient
    @JsonIgnoreProperties(
        value = { "user", "superiorEmployee", "jobTitle", "level", "knowledgeDomains", "perimeters", "site", "subordinateEmployees" },
        allowSetters = true
    )
    private Set<Employee> subordinateEmployees = new HashSet<>();

    @Column("user_id")
    private Long userId;

    @Column("superior_employee_id")
    private Long superiorEmployeeId;

    @Column("job_title_id")
    private Long jobTitleId;

    @Column("level_id")
    private Long levelId;

    @Column("site_id")
    private Long siteId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstnameEmployee() {
        return this.firstnameEmployee;
    }

    public Employee firstnameEmployee(String firstnameEmployee) {
        this.setFirstnameEmployee(firstnameEmployee);
        return this;
    }

    public void setFirstnameEmployee(String firstnameEmployee) {
        this.firstnameEmployee = firstnameEmployee;
    }

    public String getLastnameEmployee() {
        return this.lastnameEmployee;
    }

    public Employee lastnameEmployee(String lastnameEmployee) {
        this.setLastnameEmployee(lastnameEmployee);
        return this;
    }

    public void setLastnameEmployee(String lastnameEmployee) {
        this.lastnameEmployee = lastnameEmployee;
    }

    public String getMatriculationNumberEmployee() {
        return this.matriculationNumberEmployee;
    }

    public Employee matriculationNumberEmployee(String matriculationNumberEmployee) {
        this.setMatriculationNumberEmployee(matriculationNumberEmployee);
        return this;
    }

    public void setMatriculationNumberEmployee(String matriculationNumberEmployee) {
        this.matriculationNumberEmployee = matriculationNumberEmployee;
    }

    public LocalDate getDateOfBirthEmployee() {
        return this.dateOfBirthEmployee;
    }

    public Employee dateOfBirthEmployee(LocalDate dateOfBirthEmployee) {
        this.setDateOfBirthEmployee(dateOfBirthEmployee);
        return this;
    }

    public void setDateOfBirthEmployee(LocalDate dateOfBirthEmployee) {
        this.dateOfBirthEmployee = dateOfBirthEmployee;
    }

    public String getEmailEmployee() {
        return this.emailEmployee;
    }

    public Employee emailEmployee(String emailEmployee) {
        this.setEmailEmployee(emailEmployee);
        return this;
    }

    public void setEmailEmployee(String emailEmployee) {
        this.emailEmployee = emailEmployee;
    }

    public String getPhoneEmployee() {
        return this.phoneEmployee;
    }

    public Employee phoneEmployee(String phoneEmployee) {
        this.setPhoneEmployee(phoneEmployee);
        return this;
    }

    public void setPhoneEmployee(String phoneEmployee) {
        this.phoneEmployee = phoneEmployee;
    }

    public LocalDate getHireDateEmployee() {
        return this.hireDateEmployee;
    }

    public Employee hireDateEmployee(LocalDate hireDateEmployee) {
        this.setHireDateEmployee(hireDateEmployee);
        return this;
    }

    public void setHireDateEmployee(LocalDate hireDateEmployee) {
        this.hireDateEmployee = hireDateEmployee;
    }

    public GenderEnum getGenderEmployee() {
        return this.genderEmployee;
    }

    public Employee genderEmployee(GenderEnum genderEmployee) {
        this.setGenderEmployee(genderEmployee);
        return this;
    }

    public void setGenderEmployee(GenderEnum genderEmployee) {
        this.genderEmployee = genderEmployee;
    }

    public String getDescEmployee() {
        return this.descEmployee;
    }

    public Employee descEmployee(String descEmployee) {
        this.setDescEmployee(descEmployee);
        return this;
    }

    public void setDescEmployee(String descEmployee) {
        this.descEmployee = descEmployee;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public Employee user(User user) {
        this.setUser(user);
        return this;
    }

    public Employee getSuperiorEmployee() {
        return this.superiorEmployee;
    }

    public void setSuperiorEmployee(Employee employee) {
        this.superiorEmployee = employee;
        this.superiorEmployeeId = employee != null ? employee.getId() : null;
    }

    public Employee superiorEmployee(Employee employee) {
        this.setSuperiorEmployee(employee);
        return this;
    }

    public JobTitle getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
        this.jobTitleId = jobTitle != null ? jobTitle.getId() : null;
    }

    public Employee jobTitle(JobTitle jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
        this.levelId = level != null ? level.getId() : null;
    }

    public Employee level(Level level) {
        this.setLevel(level);
        return this;
    }

    public Set<KnowledgeDomain> getKnowledgeDomains() {
        return this.knowledgeDomains;
    }

    public void setKnowledgeDomains(Set<KnowledgeDomain> knowledgeDomains) {
        this.knowledgeDomains = knowledgeDomains;
    }

    public Employee knowledgeDomains(Set<KnowledgeDomain> knowledgeDomains) {
        this.setKnowledgeDomains(knowledgeDomains);
        return this;
    }

    public Employee addKnowledgeDomain(KnowledgeDomain knowledgeDomain) {
        this.knowledgeDomains.add(knowledgeDomain);
        knowledgeDomain.getEmployees().add(this);
        return this;
    }

    public Employee removeKnowledgeDomain(KnowledgeDomain knowledgeDomain) {
        this.knowledgeDomains.remove(knowledgeDomain);
        knowledgeDomain.getEmployees().remove(this);
        return this;
    }

    public Set<Perimeter> getPerimeters() {
        return this.perimeters;
    }

    public void setPerimeters(Set<Perimeter> perimeters) {
        this.perimeters = perimeters;
    }

    public Employee perimeters(Set<Perimeter> perimeters) {
        this.setPerimeters(perimeters);
        return this;
    }

    public Employee addPerimeter(Perimeter perimeter) {
        this.perimeters.add(perimeter);
        perimeter.getEmployees().add(this);
        return this;
    }

    public Employee removePerimeter(Perimeter perimeter) {
        this.perimeters.remove(perimeter);
        perimeter.getEmployees().remove(this);
        return this;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
        this.siteId = site != null ? site.getId() : null;
    }

    public Employee site(Site site) {
        this.setSite(site);
        return this;
    }

    public Set<Employee> getSubordinateEmployees() {
        return this.subordinateEmployees;
    }

    public void setSubordinateEmployees(Set<Employee> employees) {
        if (this.subordinateEmployees != null) {
            this.subordinateEmployees.forEach(i -> i.setSuperiorEmployee(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setSuperiorEmployee(this));
        }
        this.subordinateEmployees = employees;
    }

    public Employee subordinateEmployees(Set<Employee> employees) {
        this.setSubordinateEmployees(employees);
        return this;
    }

    public Employee addSubordinateEmployee(Employee employee) {
        this.subordinateEmployees.add(employee);
        employee.setSuperiorEmployee(this);
        return this;
    }

    public Employee removeSubordinateEmployee(Employee employee) {
        this.subordinateEmployees.remove(employee);
        employee.setSuperiorEmployee(null);
        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long user) {
        this.userId = user;
    }

    public Long getSuperiorEmployeeId() {
        return this.superiorEmployeeId;
    }

    public void setSuperiorEmployeeId(Long employee) {
        this.superiorEmployeeId = employee;
    }

    public Long getJobTitleId() {
        return this.jobTitleId;
    }

    public void setJobTitleId(Long jobTitle) {
        this.jobTitleId = jobTitle;
    }

    public Long getLevelId() {
        return this.levelId;
    }

    public void setLevelId(Long level) {
        this.levelId = level;
    }

    public Long getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Long site) {
        this.siteId = site;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstnameEmployee='" + getFirstnameEmployee() + "'" +
            ", lastnameEmployee='" + getLastnameEmployee() + "'" +
            ", matriculationNumberEmployee='" + getMatriculationNumberEmployee() + "'" +
            ", dateOfBirthEmployee='" + getDateOfBirthEmployee() + "'" +
            ", emailEmployee='" + getEmailEmployee() + "'" +
            ", phoneEmployee='" + getPhoneEmployee() + "'" +
            ", hireDateEmployee='" + getHireDateEmployee() + "'" +
            ", genderEmployee='" + getGenderEmployee() + "'" +
            ", descEmployee='" + getDescEmployee() + "'" +
            "}";
    }
}
