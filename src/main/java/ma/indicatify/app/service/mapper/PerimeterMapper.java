package ma.indicatify.app.service.mapper;

import ma.indicatify.app.domain.Activity;
import ma.indicatify.app.domain.Perimeter;
import ma.indicatify.app.service.dto.ActivityDTO;
import ma.indicatify.app.service.dto.PerimeterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Perimeter} and its DTO {@link PerimeterDTO}.
 */
@Mapper(componentModel = "spring")
public interface PerimeterMapper extends EntityMapper<PerimeterDTO, Perimeter> {
    @Mapping(target = "activity", source = "activity", qualifiedByName = "activityId")
    PerimeterDTO toDto(Perimeter s);

    @Named("activityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActivityDTO toDtoActivityId(Activity activity);
}
