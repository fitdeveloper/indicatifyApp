package ma.indicatify.app.service;

import ma.indicatify.app.service.dto.LevelDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ma.indicatify.app.domain.Level}.
 */
public interface LevelService {
    /**
     * Save a level.
     *
     * @param levelDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<LevelDTO> save(LevelDTO levelDTO);

    /**
     * Updates a level.
     *
     * @param levelDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<LevelDTO> update(LevelDTO levelDTO);

    /**
     * Partially updates a level.
     *
     * @param levelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<LevelDTO> partialUpdate(LevelDTO levelDTO);

    /**
     * Get all the levels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<LevelDTO> findAll(Pageable pageable);

    /**
     * Returns the number of levels available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" level.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<LevelDTO> findOne(Long id);

    /**
     * Delete the "id" level.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
