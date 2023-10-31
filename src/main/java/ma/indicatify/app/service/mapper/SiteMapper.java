package ma.indicatify.app.service.mapper;

import ma.indicatify.app.domain.Site;
import ma.indicatify.app.service.dto.SiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Site} and its DTO {@link SiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface SiteMapper extends EntityMapper<SiteDTO, Site> {}
