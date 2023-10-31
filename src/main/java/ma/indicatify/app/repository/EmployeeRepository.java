package ma.indicatify.app.repository;

import ma.indicatify.app.domain.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long>, EmployeeRepositoryInternal {
    Flux<Employee> findAllBy(Pageable pageable);

    @Override
    Mono<Employee> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Employee> findAllWithEagerRelationships();

    @Override
    Flux<Employee> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM employee entity WHERE entity.user_id = :id")
    Flux<Employee> findByUser(Long id);

    @Query("SELECT * FROM employee entity WHERE entity.user_id IS NULL")
    Flux<Employee> findAllWhereUserIsNull();

    @Query("SELECT * FROM employee entity WHERE entity.superior_employee_id = :id")
    Flux<Employee> findBySuperiorEmployee(Long id);

    @Query("SELECT * FROM employee entity WHERE entity.superior_employee_id IS NULL")
    Flux<Employee> findAllWhereSuperiorEmployeeIsNull();

    @Query("SELECT * FROM employee entity WHERE entity.job_title_id = :id")
    Flux<Employee> findByJobTitle(Long id);

    @Query("SELECT * FROM employee entity WHERE entity.job_title_id IS NULL")
    Flux<Employee> findAllWhereJobTitleIsNull();

    @Query("SELECT * FROM employee entity WHERE entity.level_id = :id")
    Flux<Employee> findByLevel(Long id);

    @Query("SELECT * FROM employee entity WHERE entity.level_id IS NULL")
    Flux<Employee> findAllWhereLevelIsNull();

    @Query(
        "SELECT entity.* FROM employee entity JOIN rel_employee__knowledge_domain joinTable ON entity.id = joinTable.knowledge_domain_id WHERE joinTable.knowledge_domain_id = :id"
    )
    Flux<Employee> findByKnowledgeDomain(Long id);

    @Query(
        "SELECT entity.* FROM employee entity JOIN rel_employee__perimeter joinTable ON entity.id = joinTable.perimeter_id WHERE joinTable.perimeter_id = :id"
    )
    Flux<Employee> findByPerimeter(Long id);

    @Query("SELECT * FROM employee entity WHERE entity.site_id = :id")
    Flux<Employee> findBySite(Long id);

    @Query("SELECT * FROM employee entity WHERE entity.site_id IS NULL")
    Flux<Employee> findAllWhereSiteIsNull();

    @Override
    <S extends Employee> Mono<S> save(S entity);

    @Override
    Flux<Employee> findAll();

    @Override
    Mono<Employee> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EmployeeRepositoryInternal {
    <S extends Employee> Mono<S> save(S entity);

    Flux<Employee> findAllBy(Pageable pageable);

    Flux<Employee> findAll();

    Mono<Employee> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Employee> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Employee> findOneWithEagerRelationships(Long id);

    Flux<Employee> findAllWithEagerRelationships();

    Flux<Employee> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
