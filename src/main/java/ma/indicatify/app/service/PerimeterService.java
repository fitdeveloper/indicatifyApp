package ma.indicatify.app.service;

import ma.indicatify.app.service.dto.PerimeterDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ma.indicatify.app.domain.Perimeter}.
 */
public interface PerimeterService {
    /**
     * Save a perimeter.
     *
     * @param perimeterDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PerimeterDTO> save(PerimeterDTO perimeterDTO);

    /**
     * Updates a perimeter.
     *
     * @param perimeterDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PerimeterDTO> update(PerimeterDTO perimeterDTO);

    /**
     * Partially updates a perimeter.
     *
     * @param perimeterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PerimeterDTO> partialUpdate(PerimeterDTO perimeterDTO);

    /**
     * Get all the perimeters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PerimeterDTO> findAll(Pageable pageable);

    /**
     * Returns the number of perimeters available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" perimeter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PerimeterDTO> findOne(Long id);

    /**
     * Delete the "id" perimeter.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
