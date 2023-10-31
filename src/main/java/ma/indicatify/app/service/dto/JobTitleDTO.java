package ma.indicatify.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ma.indicatify.app.domain.JobTitle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobTitleDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nameJobTitle;

    private String descJobTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameJobTitle() {
        return nameJobTitle;
    }

    public void setNameJobTitle(String nameJobTitle) {
        this.nameJobTitle = nameJobTitle;
    }

    public String getDescJobTitle() {
        return descJobTitle;
    }

    public void setDescJobTitle(String descJobTitle) {
        this.descJobTitle = descJobTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobTitleDTO)) {
            return false;
        }

        JobTitleDTO jobTitleDTO = (JobTitleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jobTitleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobTitleDTO{" +
            "id=" + getId() +
            ", nameJobTitle='" + getNameJobTitle() + "'" +
            ", descJobTitle='" + getDescJobTitle() + "'" +
            "}";
    }
}
