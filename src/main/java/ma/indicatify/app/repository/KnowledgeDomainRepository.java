package ma.indicatify.app.repository;

import ma.indicatify.app.domain.KnowledgeDomain;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the KnowledgeDomain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KnowledgeDomainRepository extends ReactiveCrudRepository<KnowledgeDomain, Long>, KnowledgeDomainRepositoryInternal {
    Flux<KnowledgeDomain> findAllBy(Pageable pageable);

    @Override
    <S extends KnowledgeDomain> Mono<S> save(S entity);

    @Override
    Flux<KnowledgeDomain> findAll();

    @Override
    Mono<KnowledgeDomain> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface KnowledgeDomainRepositoryInternal {
    <S extends KnowledgeDomain> Mono<S> save(S entity);

    Flux<KnowledgeDomain> findAllBy(Pageable pageable);

    Flux<KnowledgeDomain> findAll();

    Mono<KnowledgeDomain> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<KnowledgeDomain> findAllBy(Pageable pageable, Criteria criteria);

}
