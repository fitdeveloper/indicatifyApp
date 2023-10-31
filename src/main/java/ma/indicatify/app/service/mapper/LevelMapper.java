package ma.indicatify.app.service.mapper;

import ma.indicatify.app.domain.Level;
import ma.indicatify.app.service.dto.LevelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Level} and its DTO {@link LevelDTO}.
 */
@Mapper(componentModel = "spring")
public interface LevelMapper extends EntityMapper<LevelDTO, Level> {}
