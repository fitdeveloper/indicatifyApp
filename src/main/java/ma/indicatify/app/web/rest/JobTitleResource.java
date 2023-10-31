package ma.indicatify.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.indicatify.app.repository.JobTitleRepository;
import ma.indicatify.app.service.JobTitleService;
import ma.indicatify.app.service.dto.JobTitleDTO;
import ma.indicatify.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link ma.indicatify.app.domain.JobTitle}.
 */
@RestController
@RequestMapping("/api")
public class JobTitleResource {

    private final Logger log = LoggerFactory.getLogger(JobTitleResource.class);

    private static final String ENTITY_NAME = "jobTitle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobTitleService jobTitleService;

    private final JobTitleRepository jobTitleRepository;

    public JobTitleResource(JobTitleService jobTitleService, JobTitleRepository jobTitleRepository) {
        this.jobTitleService = jobTitleService;
        this.jobTitleRepository = jobTitleRepository;
    }

    /**
     * {@code POST  /job-titles} : Create a new jobTitle.
     *
     * @param jobTitleDTO the jobTitleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobTitleDTO, or with status {@code 400 (Bad Request)} if the jobTitle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-titles")
    public Mono<ResponseEntity<JobTitleDTO>> createJobTitle(@Valid @RequestBody JobTitleDTO jobTitleDTO) throws URISyntaxException {
        log.debug("REST request to save JobTitle : {}", jobTitleDTO);
        if (jobTitleDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobTitle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return jobTitleService
            .save(jobTitleDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/job-titles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /job-titles/:id} : Updates an existing jobTitle.
     *
     * @param id the id of the jobTitleDTO to save.
     * @param jobTitleDTO the jobTitleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobTitleDTO,
     * or with status {@code 400 (Bad Request)} if the jobTitleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobTitleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-titles/{id}")
    public Mono<ResponseEntity<JobTitleDTO>> updateJobTitle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobTitleDTO jobTitleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update JobTitle : {}, {}", id, jobTitleDTO);
        if (jobTitleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobTitleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return jobTitleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return jobTitleService
                    .update(jobTitleDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /job-titles/:id} : Partial updates given fields of an existing jobTitle, field will ignore if it is null
     *
     * @param id the id of the jobTitleDTO to save.
     * @param jobTitleDTO the jobTitleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobTitleDTO,
     * or with status {@code 400 (Bad Request)} if the jobTitleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jobTitleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobTitleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-titles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<JobTitleDTO>> partialUpdateJobTitle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobTitleDTO jobTitleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobTitle partially : {}, {}", id, jobTitleDTO);
        if (jobTitleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobTitleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return jobTitleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<JobTitleDTO> result = jobTitleService.partialUpdate(jobTitleDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /job-titles} : get all the jobTitles.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobTitles in body.
     */
    @GetMapping("/job-titles")
    public Mono<ResponseEntity<List<JobTitleDTO>>> getAllJobTitles(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of JobTitles");
        return jobTitleService
            .countAll()
            .zipWith(jobTitleService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /job-titles/:id} : get the "id" jobTitle.
     *
     * @param id the id of the jobTitleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobTitleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-titles/{id}")
    public Mono<ResponseEntity<JobTitleDTO>> getJobTitle(@PathVariable Long id) {
        log.debug("REST request to get JobTitle : {}", id);
        Mono<JobTitleDTO> jobTitleDTO = jobTitleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobTitleDTO);
    }

    /**
     * {@code DELETE  /job-titles/:id} : delete the "id" jobTitle.
     *
     * @param id the id of the jobTitleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-titles/{id}")
    public Mono<ResponseEntity<Void>> deleteJobTitle(@PathVariable Long id) {
        log.debug("REST request to delete JobTitle : {}", id);
        return jobTitleService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
