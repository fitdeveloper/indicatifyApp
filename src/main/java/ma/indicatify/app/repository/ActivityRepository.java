package ma.indicatify.app.repository;

import ma.indicatify.app.domain.Activity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Activity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityRepository extends ReactiveCrudRepository<Activity, Long>, ActivityRepositoryInternal {
    Flux<Activity> findAllBy(Pageable pageable);

    @Override
    <S extends Activity> Mono<S> save(S entity);

    @Override
    Flux<Activity> findAll();

    @Override
    Mono<Activity> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ActivityRepositoryInternal {
    <S extends Activity> Mono<S> save(S entity);

    Flux<Activity> findAllBy(Pageable pageable);

    Flux<Activity> findAll();

    Mono<Activity> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Activity> findAllBy(Pageable pageable, Criteria criteria);

}
