package ma.indicatify.app.service.mapper;

import ma.indicatify.app.domain.Activity;
import ma.indicatify.app.service.dto.ActivityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activity} and its DTO {@link ActivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {}
