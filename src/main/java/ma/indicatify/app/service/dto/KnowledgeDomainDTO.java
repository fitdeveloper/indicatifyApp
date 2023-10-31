package ma.indicatify.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ma.indicatify.app.domain.KnowledgeDomain} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KnowledgeDomainDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String nameKnowledgeDomain;

    private String descKnowledgeDomain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameKnowledgeDomain() {
        return nameKnowledgeDomain;
    }

    public void setNameKnowledgeDomain(String nameKnowledgeDomain) {
        this.nameKnowledgeDomain = nameKnowledgeDomain;
    }

    public String getDescKnowledgeDomain() {
        return descKnowledgeDomain;
    }

    public void setDescKnowledgeDomain(String descKnowledgeDomain) {
        this.descKnowledgeDomain = descKnowledgeDomain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KnowledgeDomainDTO)) {
            return false;
        }

        KnowledgeDomainDTO knowledgeDomainDTO = (KnowledgeDomainDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, knowledgeDomainDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KnowledgeDomainDTO{" +
            "id=" + getId() +
            ", nameKnowledgeDomain='" + getNameKnowledgeDomain() + "'" +
            ", descKnowledgeDomain='" + getDescKnowledgeDomain() + "'" +
            "}";
    }
}
