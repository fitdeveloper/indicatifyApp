package ma.indicatify.app.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A JobTitle.
 */
@Table("job_title")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobTitle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name_job_title")
    private String nameJobTitle;

    @Column("desc_job_title")
    private String descJobTitle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JobTitle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameJobTitle() {
        return this.nameJobTitle;
    }

    public JobTitle nameJobTitle(String nameJobTitle) {
        this.setNameJobTitle(nameJobTitle);
        return this;
    }

    public void setNameJobTitle(String nameJobTitle) {
        this.nameJobTitle = nameJobTitle;
    }

    public String getDescJobTitle() {
        return this.descJobTitle;
    }

    public JobTitle descJobTitle(String descJobTitle) {
        this.setDescJobTitle(descJobTitle);
        return this;
    }

    public void setDescJobTitle(String descJobTitle) {
        this.descJobTitle = descJobTitle;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobTitle)) {
            return false;
        }
        return id != null && id.equals(((JobTitle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobTitle{" +
            "id=" + getId() +
            ", nameJobTitle='" + getNameJobTitle() + "'" +
            ", descJobTitle='" + getDescJobTitle() + "'" +
            "}";
    }
}
