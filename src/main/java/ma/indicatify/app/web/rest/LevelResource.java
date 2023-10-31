package ma.indicatify.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.indicatify.app.repository.LevelRepository;
import ma.indicatify.app.service.LevelService;
import ma.indicatify.app.service.dto.LevelDTO;
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
 * REST controller for managing {@link ma.indicatify.app.domain.Level}.
 */
@RestController
@RequestMapping("/api")
public class LevelResource {

    private final Logger log = LoggerFactory.getLogger(LevelResource.class);

    private static final String ENTITY_NAME = "level";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LevelService levelService;

    private final LevelRepository levelRepository;

    public LevelResource(LevelService levelService, LevelRepository levelRepository) {
        this.levelService = levelService;
        this.levelRepository = levelRepository;
    }

    /**
     * {@code POST  /levels} : Create a new level.
     *
     * @param levelDTO the levelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new levelDTO, or with status {@code 400 (Bad Request)} if the level has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/levels")
    public Mono<ResponseEntity<LevelDTO>> createLevel(@Valid @RequestBody LevelDTO levelDTO) throws URISyntaxException {
        log.debug("REST request to save Level : {}", levelDTO);
        if (levelDTO.getId() != null) {
            throw new BadRequestAlertException("A new level cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return levelService
            .save(levelDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/levels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /levels/:id} : Updates an existing level.
     *
     * @param id the id of the levelDTO to save.
     * @param levelDTO the levelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated levelDTO,
     * or with status {@code 400 (Bad Request)} if the levelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the levelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/levels/{id}")
    public Mono<ResponseEntity<LevelDTO>> updateLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LevelDTO levelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Level : {}, {}", id, levelDTO);
        if (levelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, levelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return levelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return levelService
                    .update(levelDTO)
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
     * {@code PATCH  /levels/:id} : Partial updates given fields of an existing level, field will ignore if it is null
     *
     * @param id the id of the levelDTO to save.
     * @param levelDTO the levelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated levelDTO,
     * or with status {@code 400 (Bad Request)} if the levelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the levelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the levelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/levels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LevelDTO>> partialUpdateLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LevelDTO levelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Level partially : {}, {}", id, levelDTO);
        if (levelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, levelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return levelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LevelDTO> result = levelService.partialUpdate(levelDTO);

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
     * {@code GET  /levels} : get all the levels.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of levels in body.
     */
    @GetMapping("/levels")
    public Mono<ResponseEntity<List<LevelDTO>>> getAllLevels(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Levels");
        return levelService
            .countAll()
            .zipWith(levelService.findAll(pageable).collectList())
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
     * {@code GET  /levels/:id} : get the "id" level.
     *
     * @param id the id of the levelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the levelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/levels/{id}")
    public Mono<ResponseEntity<LevelDTO>> getLevel(@PathVariable Long id) {
        log.debug("REST request to get Level : {}", id);
        Mono<LevelDTO> levelDTO = levelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(levelDTO);
    }

    /**
     * {@code DELETE  /levels/:id} : delete the "id" level.
     *
     * @param id the id of the levelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/levels/{id}")
    public Mono<ResponseEntity<Void>> deleteLevel(@PathVariable Long id) {
        log.debug("REST request to delete Level : {}", id);
        return levelService
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
