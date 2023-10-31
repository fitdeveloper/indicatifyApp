package ma.indicatify.app.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import ma.indicatify.app.domain.enumeration.LevelEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Level.
 */
@Table("level")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name_level")
    private String nameLevel;

    @NotNull(message = "must not be null")
    @Column("value_level")
    private LevelEnum valueLevel;

    @Column("desc_level")
    private String descLevel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Level id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameLevel() {
        return this.nameLevel;
    }

    public Level nameLevel(String nameLevel) {
        this.setNameLevel(nameLevel);
        return this;
    }

    public void setNameLevel(String nameLevel) {
        this.nameLevel = nameLevel;
    }

    public LevelEnum getValueLevel() {
        return this.valueLevel;
    }

    public Level valueLevel(LevelEnum valueLevel) {
        this.setValueLevel(valueLevel);
        return this;
    }

    public void setValueLevel(LevelEnum valueLevel) {
        this.valueLevel = valueLevel;
    }

    public String getDescLevel() {
        return this.descLevel;
    }

    public Level descLevel(String descLevel) {
        this.setDescLevel(descLevel);
        return this;
    }

    public void setDescLevel(String descLevel) {
        this.descLevel = descLevel;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Level)) {
            return false;
        }
        return id != null && id.equals(((Level) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Level{" +
            "id=" + getId() +
            ", nameLevel='" + getNameLevel() + "'" +
            ", valueLevel='" + getValueLevel() + "'" +
            ", descLevel='" + getDescLevel() + "'" +
            "}";
    }
}
