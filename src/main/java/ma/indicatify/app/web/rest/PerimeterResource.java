package ma.indicatify.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.indicatify.app.repository.PerimeterRepository;
import ma.indicatify.app.service.PerimeterService;
import ma.indicatify.app.service.dto.PerimeterDTO;
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
 * REST controller for managing {@link ma.indicatify.app.domain.Perimeter}.
 */
@RestController
@RequestMapping("/api")
public class PerimeterResource {

    private final Logger log = LoggerFactory.getLogger(PerimeterResource.class);

    private static final String ENTITY_NAME = "perimeter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerimeterService perimeterService;

    private final PerimeterRepository perimeterRepository;

    public PerimeterResource(PerimeterService perimeterService, PerimeterRepository perimeterRepository) {
        this.perimeterService = perimeterService;
        this.perimeterRepository = perimeterRepository;
    }

    /**
     * {@code POST  /perimeters} : Create a new perimeter.
     *
     * @param perimeterDTO the perimeterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new perimeterDTO, or with status {@code 400 (Bad Request)} if the perimeter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/perimeters")
    public Mono<ResponseEntity<PerimeterDTO>> createPerimeter(@Valid @RequestBody PerimeterDTO perimeterDTO) throws URISyntaxException {
        log.debug("REST request to save Perimeter : {}", perimeterDTO);
        if (perimeterDTO.getId() != null) {
            throw new BadRequestAlertException("A new perimeter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return perimeterService
            .save(perimeterDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/perimeters/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /perimeters/:id} : Updates an existing perimeter.
     *
     * @param id the id of the perimeterDTO to save.
     * @param perimeterDTO the perimeterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perimeterDTO,
     * or with status {@code 400 (Bad Request)} if the perimeterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the perimeterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/perimeters/{id}")
    public Mono<ResponseEntity<PerimeterDTO>> updatePerimeter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PerimeterDTO perimeterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Perimeter : {}, {}", id, perimeterDTO);
        if (perimeterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perimeterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return perimeterRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return perimeterService
                    .update(perimeterDTO)
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
     * {@code PATCH  /perimeters/:id} : Partial updates given fields of an existing perimeter, field will ignore if it is null
     *
     * @param id the id of the perimeterDTO to save.
     * @param perimeterDTO the perimeterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perimeterDTO,
     * or with status {@code 400 (Bad Request)} if the perimeterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the perimeterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the perimeterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/perimeters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PerimeterDTO>> partialUpdatePerimeter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PerimeterDTO perimeterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Perimeter partially : {}, {}", id, perimeterDTO);
        if (perimeterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perimeterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return perimeterRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PerimeterDTO> result = perimeterService.partialUpdate(perimeterDTO);

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
     * {@code GET  /perimeters} : get all the perimeters.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of perimeters in body.
     */
    @GetMapping("/perimeters")
    public Mono<ResponseEntity<List<PerimeterDTO>>> getAllPerimeters(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Perimeters");
        return perimeterService
            .countAll()
            .zipWith(perimeterService.findAll(pageable).collectList())
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
     * {@code GET  /perimeters/:id} : get the "id" perimeter.
     *
     * @param id the id of the perimeterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perimeterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/perimeters/{id}")
    public Mono<ResponseEntity<PerimeterDTO>> getPerimeter(@PathVariable Long id) {
        log.debug("REST request to get Perimeter : {}", id);
        Mono<PerimeterDTO> perimeterDTO = perimeterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(perimeterDTO);
    }

    /**
     * {@code DELETE  /perimeters/:id} : delete the "id" perimeter.
     *
     * @param id the id of the perimeterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/perimeters/{id}")
    public Mono<ResponseEntity<Void>> deletePerimeter(@PathVariable Long id) {
        log.debug("REST request to delete Perimeter : {}", id);
        return perimeterService
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
