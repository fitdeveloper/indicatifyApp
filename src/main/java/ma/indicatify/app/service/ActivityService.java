package ma.indicatify.app.service;

import ma.indicatify.app.service.dto.ActivityDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ma.indicatify.app.domain.Activity}.
 */
public interface ActivityService {
    /**
     * Save a activity.
     *
     * @param activityDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ActivityDTO> save(ActivityDTO activityDTO);

    /**
     * Updates a activity.
     *
     * @param activityDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ActivityDTO> update(ActivityDTO activityDTO);

    /**
     * Partially updates a activity.
     *
     * @param activityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ActivityDTO> partialUpdate(ActivityDTO activityDTO);

    /**
     * Get all the activities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ActivityDTO> findAll(Pageable pageable);

    /**
     * Returns the number of activities available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" activity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ActivityDTO> findOne(Long id);

    /**
     * Delete the "id" activity.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
