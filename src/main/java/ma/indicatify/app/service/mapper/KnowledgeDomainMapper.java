package ma.indicatify.app.service.mapper;

import ma.indicatify.app.domain.KnowledgeDomain;
import ma.indicatify.app.service.dto.KnowledgeDomainDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link KnowledgeDomain} and its DTO {@link KnowledgeDomainDTO}.
 */
@Mapper(componentModel = "spring")
public interface KnowledgeDomainMapper extends EntityMapper<KnowledgeDomainDTO, KnowledgeDomain> {}
