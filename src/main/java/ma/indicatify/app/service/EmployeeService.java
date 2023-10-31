package ma.indicatify.app.service;

import ma.indicatify.app.service.dto.EmployeeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ma.indicatify.app.domain.Employee}.
 */
public interface EmployeeService {
    /**
     * Save a employee.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EmployeeDTO> save(EmployeeDTO employeeDTO);

    /**
     * Updates a employee.
     *
     * @param employeeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EmployeeDTO> update(EmployeeDTO employeeDTO);

    /**
     * Partially updates a employee.
     *
     * @param employeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EmployeeDTO> partialUpdate(EmployeeDTO employeeDTO);

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EmployeeDTO> findAll(Pageable pageable);

    /**
     * Get all the employees with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EmployeeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of employees available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" employee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EmployeeDTO> findOne(Long id);

    /**
     * Delete the "id" employee.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
