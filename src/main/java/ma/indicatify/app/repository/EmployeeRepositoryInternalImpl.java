package ma.indicatify.app.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import ma.indicatify.app.domain.Employee;
import ma.indicatify.app.domain.KnowledgeDomain;
import ma.indicatify.app.domain.Perimeter;
import ma.indicatify.app.domain.enumeration.GenderEnum;
import ma.indicatify.app.repository.rowmapper.EmployeeRowMapper;
import ma.indicatify.app.repository.rowmapper.EmployeeRowMapper;
import ma.indicatify.app.repository.rowmapper.JobTitleRowMapper;
import ma.indicatify.app.repository.rowmapper.LevelRowMapper;
import ma.indicatify.app.repository.rowmapper.SiteRowMapper;
import ma.indicatify.app.repository.rowmapper.UserRowMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Employee entity.
 */
@SuppressWarnings("unused")
class EmployeeRepositoryInternalImpl extends SimpleR2dbcRepository<Employee, Long> implements EmployeeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final EmployeeRowMapper employeeMapper;
    private final JobTitleRowMapper jobtitleMapper;
    private final LevelRowMapper levelMapper;
    private final SiteRowMapper siteMapper;

    private static final Table entityTable = Table.aliased("employee", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("jhi_user", "e_user");
    private static final Table superiorEmployeeTable = Table.aliased("employee", "superiorEmployee");
    private static final Table jobTitleTable = Table.aliased("job_title", "jobTitle");
    private static final Table levelTable = Table.aliased("level", "e_level");
    private static final Table siteTable = Table.aliased("site", "site");

    private static final EntityManager.LinkTable knowledgeDomainLink = new EntityManager.LinkTable(
        "rel_employee__knowledge_domain",
        "employee_id",
        "knowledge_domain_id"
    );
    private static final EntityManager.LinkTable perimeterLink = new EntityManager.LinkTable(
        "rel_employee__perimeter",
        "employee_id",
        "perimeter_id"
    );

    public EmployeeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        EmployeeRowMapper employeeMapper,
        JobTitleRowMapper jobtitleMapper,
        LevelRowMapper levelMapper,
        SiteRowMapper siteMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Employee.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.employeeMapper = employeeMapper;
        this.jobtitleMapper = jobtitleMapper;
        this.levelMapper = levelMapper;
        this.siteMapper = siteMapper;
    }

    @Override
    public Flux<Employee> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Employee> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EmployeeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        columns.addAll(EmployeeSqlHelper.getColumns(superiorEmployeeTable, "superiorEmployee"));
        columns.addAll(JobTitleSqlHelper.getColumns(jobTitleTable, "jobTitle"));
        columns.addAll(LevelSqlHelper.getColumns(levelTable, "level"));
        columns.addAll(SiteSqlHelper.getColumns(siteTable, "site"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable))
            .leftOuterJoin(superiorEmployeeTable)
            .on(Column.create("superior_employee_id", entityTable))
            .equals(Column.create("id", superiorEmployeeTable))
            .leftOuterJoin(jobTitleTable)
            .on(Column.create("job_title_id", entityTable))
            .equals(Column.create("id", jobTitleTable))
            .leftOuterJoin(levelTable)
            .on(Column.create("level_id", entityTable))
            .equals(Column.create("id", levelTable))
            .leftOuterJoin(siteTable)
            .on(Column.create("site_id", entityTable))
            .equals(Column.create("id", siteTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Employee.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Employee> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Employee> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Employee> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Employee> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Employee> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Employee process(Row row, RowMetadata metadata) {
        Employee entity = employeeMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        entity.setSuperiorEmployee(employeeMapper.apply(row, "superiorEmployee"));
        entity.setJobTitle(jobtitleMapper.apply(row, "jobTitle"));
        entity.setLevel(levelMapper.apply(row, "level"));
        entity.setSite(siteMapper.apply(row, "site"));
        return entity;
    }

    @Override
    public <S extends Employee> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Employee> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(knowledgeDomainLink, entity.getId(), entity.getKnowledgeDomains().stream().map(KnowledgeDomain::getId))
            .then();
        result =
            result.and(entityManager.updateLinkTable(perimeterLink, entity.getId(), entity.getPerimeters().stream().map(Perimeter::getId)));
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager
            .deleteFromLinkTable(knowledgeDomainLink, entityId)
            .and(entityManager.deleteFromLinkTable(perimeterLink, entityId));
    }
}
