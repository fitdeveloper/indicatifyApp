package ma.indicatify.app.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import ma.indicatify.app.domain.Perimeter;
import ma.indicatify.app.repository.rowmapper.ActivityRowMapper;
import ma.indicatify.app.repository.rowmapper.PerimeterRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Perimeter entity.
 */
@SuppressWarnings("unused")
class PerimeterRepositoryInternalImpl extends SimpleR2dbcRepository<Perimeter, Long> implements PerimeterRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ActivityRowMapper activityMapper;
    private final PerimeterRowMapper perimeterMapper;

    private static final Table entityTable = Table.aliased("perimeter", EntityManager.ENTITY_ALIAS);
    private static final Table activityTable = Table.aliased("activity", "activity");

    public PerimeterRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ActivityRowMapper activityMapper,
        PerimeterRowMapper perimeterMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Perimeter.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.activityMapper = activityMapper;
        this.perimeterMapper = perimeterMapper;
    }

    @Override
    public Flux<Perimeter> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Perimeter> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PerimeterSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ActivitySqlHelper.getColumns(activityTable, "activity"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(activityTable)
            .on(Column.create("activity_id", entityTable))
            .equals(Column.create("id", activityTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Perimeter.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Perimeter> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Perimeter> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Perimeter process(Row row, RowMetadata metadata) {
        Perimeter entity = perimeterMapper.apply(row, "e");
        entity.setActivity(activityMapper.apply(row, "activity"));
        return entity;
    }

    @Override
    public <S extends Perimeter> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
