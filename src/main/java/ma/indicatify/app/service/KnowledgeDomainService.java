package ma.indicatify.app.service;

import ma.indicatify.app.service.dto.KnowledgeDomainDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ma.indicatify.app.domain.KnowledgeDomain}.
 */
public interface KnowledgeDomainService {
    /**
     * Save a knowledgeDomain.
     *
     * @param knowledgeDomainDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<KnowledgeDomainDTO> save(KnowledgeDomainDTO knowledgeDomainDTO);

    /**
     * Updates a knowledgeDomain.
     *
     * @param knowledgeDomainDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<KnowledgeDomainDTO> update(KnowledgeDomainDTO knowledgeDomainDTO);

    /**
     * Partially updates a knowledgeDomain.
     *
     * @param knowledgeDomainDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<KnowledgeDomainDTO> partialUpdate(KnowledgeDomainDTO knowledgeDomainDTO);

    /**
     * Get all the knowledgeDomains.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<KnowledgeDomainDTO> findAll(Pageable pageable);

    /**
     * Returns the number of knowledgeDomains available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" knowledgeDomain.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<KnowledgeDomainDTO> findOne(Long id);

    /**
     * Delete the "id" knowledgeDomain.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
