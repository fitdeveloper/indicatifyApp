package ma.indicatify.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ma.indicatify.app.domain.Activity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActivityDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nameActivity;

    private String descActivity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameActivity() {
        return nameActivity;
    }

    public void setNameActivity(String nameActivity) {
        this.nameActivity = nameActivity;
    }

    public String getDescActivity() {
        return descActivity;
    }

    public void setDescActivity(String descActivity) {
        this.descActivity = descActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityDTO)) {
            return false;
        }

        ActivityDTO activityDTO = (ActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, activityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityDTO{" +
            "id=" + getId() +
            ", nameActivity='" + getNameActivity() + "'" +
            ", descActivity='" + getDescActivity() + "'" +
            "}";
    }
}
