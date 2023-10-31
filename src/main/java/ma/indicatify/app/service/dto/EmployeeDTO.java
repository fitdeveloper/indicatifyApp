package ma.indicatify.app.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import ma.indicatify.app.domain.enumeration.GenderEnum;

/**
 * A DTO for the {@link ma.indicatify.app.domain.Employee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String firstnameEmployee;

    @NotNull(message = "must not be null")
    private String lastnameEmployee;

    @NotNull(message = "must not be null")
    private String matriculationNumberEmployee;

    @NotNull(message = "must not be null")
    private LocalDate dateOfBirthEmployee;

    @NotNull(message = "must not be null")
    private String emailEmployee;

    private String phoneEmployee;

    private LocalDate hireDateEmployee;

    @NotNull(message = "must not be null")
    private GenderEnum genderEmployee;

    private String descEmployee;

    private UserDTO user;

    private EmployeeDTO superiorEmployee;

    private JobTitleDTO jobTitle;

    private LevelDTO level;

    private Set<KnowledgeDomainDTO> knowledgeDomains = new HashSet<>();

    private Set<PerimeterDTO> perimeters = new HashSet<>();

    private SiteDTO site;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstnameEmployee() {
        return firstnameEmployee;
    }

    public void setFirstnameEmployee(String firstnameEmployee) {
        this.firstnameEmployee = firstnameEmployee;
    }

    public String getLastnameEmployee() {
        return lastnameEmployee;
    }

    public void setLastnameEmployee(String lastnameEmployee) {
        this.lastnameEmployee = lastnameEmployee;
    }

    public String getMatriculationNumberEmployee() {
        return matriculationNumberEmployee;
    }

    public void setMatriculationNumberEmployee(String matriculationNumberEmployee) {
        this.matriculationNumberEmployee = matriculationNumberEmployee;
    }

    public LocalDate getDateOfBirthEmployee() {
        return dateOfBirthEmployee;
    }

    public void setDateOfBirthEmployee(LocalDate dateOfBirthEmployee) {
        this.dateOfBirthEmployee = dateOfBirthEmployee;
    }

    public String getEmailEmployee() {
        return emailEmployee;
    }

    public void setEmailEmployee(String emailEmployee) {
        this.emailEmployee = emailEmployee;
    }

    public String getPhoneEmployee() {
        return phoneEmployee;
    }

    public void setPhoneEmployee(String phoneEmployee) {
        this.phoneEmployee = phoneEmployee;
    }

    public LocalDate getHireDateEmployee() {
        return hireDateEmployee;
    }

    public void setHireDateEmployee(LocalDate hireDateEmployee) {
        this.hireDateEmployee = hireDateEmployee;
    }

    public GenderEnum getGenderEmployee() {
        return genderEmployee;
    }

    public void setGenderEmployee(GenderEnum genderEmployee) {
        this.genderEmployee = genderEmployee;
    }

    public String getDescEmployee() {
        return descEmployee;
    }

    public void setDescEmployee(String descEmployee) {
        this.descEmployee = descEmployee;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public EmployeeDTO getSuperiorEmployee() {
        return superiorEmployee;
    }

    public void setSuperiorEmployee(EmployeeDTO superiorEmployee) {
        this.superiorEmployee = superiorEmployee;
    }

    public JobTitleDTO getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitleDTO jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LevelDTO getLevel() {
        return level;
    }

    public void setLevel(LevelDTO level) {
        this.level = level;
    }

    public Set<KnowledgeDomainDTO> getKnowledgeDomains() {
        return knowledgeDomains;
    }

    public void setKnowledgeDomains(Set<KnowledgeDomainDTO> knowledgeDomains) {
        this.knowledgeDomains = knowledgeDomains;
    }

    public Set<PerimeterDTO> getPerimeters() {
        return perimeters;
    }

    public void setPerimeters(Set<PerimeterDTO> perimeters) {
        this.perimeters = perimeters;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
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
            ", user=" + getUser() +
            ", superiorEmployee=" + getSuperiorEmployee() +
            ", jobTitle=" + getJobTitle() +
            ", level=" + getLevel() +
            ", knowledgeDomains=" + getKnowledgeDomains() +
            ", perimeters=" + getPerimeters() +
            ", site=" + getSite() +
            "}";
    }
}
