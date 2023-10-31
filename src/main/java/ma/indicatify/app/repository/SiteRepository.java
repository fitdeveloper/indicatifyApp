package ma.indicatify.app.repository;

import ma.indicatify.app.domain.Site;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Site entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteRepository extends ReactiveCrudRepository<Site, Long>, SiteRepositoryInternal {
    Flux<Site> findAllBy(Pageable pageable);

    @Override
    <S extends Site> Mono<S> save(S entity);

    @Override
    Flux<Site> findAll();

    @Override
    Mono<Site> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SiteRepositoryInternal {
    <S extends Site> Mono<S> save(S entity);

    Flux<Site> findAllBy(Pageable pageable);

    Flux<Site> findAll();

    Mono<Site> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Site> findAllBy(Pageable pageable, Criteria criteria);

}
