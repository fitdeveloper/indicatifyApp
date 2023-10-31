package ma.indicatify.app.repository;

import ma.indicatify.app.domain.Perimeter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Perimeter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerimeterRepository extends ReactiveCrudRepository<Perimeter, Long>, PerimeterRepositoryInternal {
    Flux<Perimeter> findAllBy(Pageable pageable);

    @Query("SELECT * FROM perimeter entity WHERE entity.activity_id = :id")
    Flux<Perimeter> findByActivity(Long id);

    @Query("SELECT * FROM perimeter entity WHERE entity.activity_id IS NULL")
    Flux<Perimeter> findAllWhereActivityIsNull();

    @Override
    <S extends Perimeter> Mono<S> save(S entity);

    @Override
    Flux<Perimeter> findAll();

    @Override
    Mono<Perimeter> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PerimeterRepositoryInternal {
    <S extends Perimeter> Mono<S> save(S entity);

    Flux<Perimeter> findAllBy(Pageable pageable);

    Flux<Perimeter> findAll();

    Mono<Perimeter> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Perimeter> findAllBy(Pageable pageable, Criteria criteria);

}
