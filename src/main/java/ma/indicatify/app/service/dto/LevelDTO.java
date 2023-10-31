package ma.indicatify.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import ma.indicatify.app.domain.enumeration.LevelEnum;

/**
 * A DTO for the {@link ma.indicatify.app.domain.Level} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LevelDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nameLevel;

    @NotNull(message = "must not be null")
    private LevelEnum valueLevel;

    private String descLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameLevel() {
        return nameLevel;
    }

    public void setNameLevel(String nameLevel) {
        this.nameLevel = nameLevel;
    }

    public LevelEnum getValueLevel() {
        return valueLevel;
    }

    public void setValueLevel(LevelEnum valueLevel) {
        this.valueLevel = valueLevel;
    }

    public String getDescLevel() {
        return descLevel;
    }

    public void setDescLevel(String descLevel) {
        this.descLevel = descLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LevelDTO)) {
            return false;
        }

        LevelDTO levelDTO = (LevelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, levelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LevelDTO{" +
            "id=" + getId() +
            ", nameLevel='" + getNameLevel() + "'" +
            ", valueLevel='" + getValueLevel() + "'" +
            ", descLevel='" + getDescLevel() + "'" +
            "}";
    }
}
