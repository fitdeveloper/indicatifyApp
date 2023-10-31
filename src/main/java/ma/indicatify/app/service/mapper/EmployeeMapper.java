package ma.indicatify.app.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import ma.indicatify.app.domain.Employee;
import ma.indicatify.app.domain.JobTitle;
import ma.indicatify.app.domain.KnowledgeDomain;
import ma.indicatify.app.domain.Level;
import ma.indicatify.app.domain.Perimeter;
import ma.indicatify.app.domain.Site;
import ma.indicatify.app.domain.User;
import ma.indicatify.app.service.dto.EmployeeDTO;
import ma.indicatify.app.service.dto.JobTitleDTO;
import ma.indicatify.app.service.dto.KnowledgeDomainDTO;
import ma.indicatify.app.service.dto.LevelDTO;
import ma.indicatify.app.service.dto.PerimeterDTO;
import ma.indicatify.app.service.dto.SiteDTO;
import ma.indicatify.app.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "superiorEmployee", source = "superiorEmployee", qualifiedByName = "employeeId")
    @Mapping(target = "jobTitle", source = "jobTitle", qualifiedByName = "jobTitleId")
    @Mapping(target = "level", source = "level", qualifiedByName = "levelId")
    @Mapping(target = "knowledgeDomains", source = "knowledgeDomains", qualifiedByName = "knowledgeDomainIdSet")
    @Mapping(target = "perimeters", source = "perimeters", qualifiedByName = "perimeterIdSet")
    @Mapping(target = "site", source = "site", qualifiedByName = "siteId")
    EmployeeDTO toDto(Employee s);

    @Mapping(target = "removeKnowledgeDomain", ignore = true)
    @Mapping(target = "removePerimeter", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    @Named("jobTitleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    JobTitleDTO toDtoJobTitleId(JobTitle jobTitle);

    @Named("levelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LevelDTO toDtoLevelId(Level level);

    @Named("knowledgeDomainId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    KnowledgeDomainDTO toDtoKnowledgeDomainId(KnowledgeDomain knowledgeDomain);

    @Named("knowledgeDomainIdSet")
    default Set<KnowledgeDomainDTO> toDtoKnowledgeDomainIdSet(Set<KnowledgeDomain> knowledgeDomain) {
        return knowledgeDomain.stream().map(this::toDtoKnowledgeDomainId).collect(Collectors.toSet());
    }

    @Named("perimeterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PerimeterDTO toDtoPerimeterId(Perimeter perimeter);

    @Named("perimeterIdSet")
    default Set<PerimeterDTO> toDtoPerimeterIdSet(Set<Perimeter> perimeter) {
        return perimeter.stream().map(this::toDtoPerimeterId).collect(Collectors.toSet());
    }

    @Named("siteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SiteDTO toDtoSiteId(Site site);
}
