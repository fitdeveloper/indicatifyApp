package ma.indicatify.app.service.mapper;

import ma.indicatify.app.domain.JobTitle;
import ma.indicatify.app.service.dto.JobTitleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobTitle} and its DTO {@link JobTitleDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobTitleMapper extends EntityMapper<JobTitleDTO, JobTitle> {}
