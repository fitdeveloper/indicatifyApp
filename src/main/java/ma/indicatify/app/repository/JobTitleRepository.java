package ma.indicatify.app.repository;

import ma.indicatify.app.domain.JobTitle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the JobTitle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobTitleRepository extends ReactiveCrudRepository<JobTitle, Long>, JobTitleRepositoryInternal {
    Flux<JobTitle> findAllBy(Pageable pageable);

    @Override
    <S extends JobTitle> Mono<S> save(S entity);

    @Override
    Flux<JobTitle> findAll();

    @Override
    Mono<JobTitle> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface JobTitleRepositoryInternal {
    <S extends JobTitle> Mono<S> save(S entity);

    Flux<JobTitle> findAllBy(Pageable pageable);

    Flux<JobTitle> findAll();

    Mono<JobTitle> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<JobTitle> findAllBy(Pageable pageable, Criteria criteria);

}
