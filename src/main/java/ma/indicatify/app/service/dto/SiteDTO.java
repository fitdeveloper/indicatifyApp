package ma.indicatify.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ma.indicatify.app.domain.Site} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nameSite;

    @NotNull(message = "must not be null")
    private String addressSite;

    private String descSite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameSite() {
        return nameSite;
    }

    public void setNameSite(String nameSite) {
        this.nameSite = nameSite;
    }

    public String getAddressSite() {
        return addressSite;
    }

    public void setAddressSite(String addressSite) {
        this.addressSite = addressSite;
    }

    public String getDescSite() {
        return descSite;
    }

    public void setDescSite(String descSite) {
        this.descSite = descSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SiteDTO)) {
            return false;
        }

        SiteDTO siteDTO = (SiteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, siteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteDTO{" +
            "id=" + getId() +
            ", nameSite='" + getNameSite() + "'" +
            ", addressSite='" + getAddressSite() + "'" +
            ", descSite='" + getDescSite() + "'" +
            "}";
    }
}
