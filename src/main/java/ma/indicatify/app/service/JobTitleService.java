package ma.indicatify.app.service;

import ma.indicatify.app.service.dto.JobTitleDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ma.indicatify.app.domain.JobTitle}.
 */
public interface JobTitleService {
    /**
     * Save a jobTitle.
     *
     * @param jobTitleDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<JobTitleDTO> save(JobTitleDTO jobTitleDTO);

    /**
     * Updates a jobTitle.
     *
     * @param jobTitleDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<JobTitleDTO> update(JobTitleDTO jobTitleDTO);

    /**
     * Partially updates a jobTitle.
     *
     * @param jobTitleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<JobTitleDTO> partialUpdate(JobTitleDTO jobTitleDTO);

    /**
     * Get all the jobTitles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<JobTitleDTO> findAll(Pageable pageable);

    /**
     * Returns the number of jobTitles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" jobTitle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<JobTitleDTO> findOne(Long id);

    /**
     * Delete the "id" jobTitle.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
