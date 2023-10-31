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
 * A Activity.
 */
@Table("activity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name_activity")
    private String nameActivity;

    @Column("desc_activity")
    private String descActivity;

    @Transient
    @JsonIgnoreProperties(value = { "activity", "employees" }, allowSetters = true)
    private Set<Perimeter> perimeters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameActivity() {
        return this.nameActivity;
    }

    public Activity nameActivity(String nameActivity) {
        this.setNameActivity(nameActivity);
        return this;
    }

    public void setNameActivity(String nameActivity) {
        this.nameActivity = nameActivity;
    }

    public String getDescActivity() {
        return this.descActivity;
    }

    public Activity descActivity(String descActivity) {
        this.setDescActivity(descActivity);
        return this;
    }

    public void setDescActivity(String descActivity) {
        this.descActivity = descActivity;
    }

    public Set<Perimeter> getPerimeters() {
        return this.perimeters;
    }

    public void setPerimeters(Set<Perimeter> perimeters) {
        if (this.perimeters != null) {
            this.perimeters.forEach(i -> i.setActivity(null));
        }
        if (perimeters != null) {
            perimeters.forEach(i -> i.setActivity(this));
        }
        this.perimeters = perimeters;
    }

    public Activity perimeters(Set<Perimeter> perimeters) {
        this.setPerimeters(perimeters);
        return this;
    }

    public Activity addPerimeter(Perimeter perimeter) {
        this.perimeters.add(perimeter);
        perimeter.setActivity(this);
        return this;
    }

    public Activity removePerimeter(Perimeter perimeter) {
        this.perimeters.remove(perimeter);
        perimeter.setActivity(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        return id != null && id.equals(((Activity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activity{" +
            "id=" + getId() +
            ", nameActivity='" + getNameActivity() + "'" +
            ", descActivity='" + getDescActivity() + "'" +
            "}";
    }
}
