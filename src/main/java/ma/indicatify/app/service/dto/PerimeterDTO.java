package ma.indicatify.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ma.indicatify.app.domain.Perimeter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PerimeterDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String namePerimeter;

    private String descPerimeter;

    private ActivityDTO activity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamePerimeter() {
        return namePerimeter;
    }

    public void setNamePerimeter(String namePerimeter) {
        this.namePerimeter = namePerimeter;
    }

    public String getDescPerimeter() {
        return descPerimeter;
    }

    public void setDescPerimeter(String descPerimeter) {
        this.descPerimeter = descPerimeter;
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PerimeterDTO)) {
            return false;
        }

        PerimeterDTO perimeterDTO = (PerimeterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, perimeterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerimeterDTO{" +
            "id=" + getId() +
            ", namePerimeter='" + getNamePerimeter() + "'" +
            ", descPerimeter='" + getDescPerimeter() + "'" +
            ", activity=" + getActivity() +
            "}";
    }
}
