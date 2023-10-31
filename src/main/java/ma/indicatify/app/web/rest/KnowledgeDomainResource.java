package ma.indicatify.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.indicatify.app.repository.KnowledgeDomainRepository;
import ma.indicatify.app.service.KnowledgeDomainService;
import ma.indicatify.app.service.dto.KnowledgeDomainDTO;
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
 * REST controller for managing {@link ma.indicatify.app.domain.KnowledgeDomain}.
 */
@RestController
@RequestMapping("/api")
public class KnowledgeDomainResource {

    private final Logger log = LoggerFactory.getLogger(KnowledgeDomainResource.class);

    private static final String ENTITY_NAME = "knowledgeDomain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KnowledgeDomainService knowledgeDomainService;

    private final KnowledgeDomainRepository knowledgeDomainRepository;

    public KnowledgeDomainResource(KnowledgeDomainService knowledgeDomainService, KnowledgeDomainRepository knowledgeDomainRepository) {
        this.knowledgeDomainService = knowledgeDomainService;
        this.knowledgeDomainRepository = knowledgeDomainRepository;
    }

    /**
     * {@code POST  /knowledge-domains} : Create a new knowledgeDomain.
     *
     * @param knowledgeDomainDTO the knowledgeDomainDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new knowledgeDomainDTO, or with status {@code 400 (Bad Request)} if the knowledgeDomain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/knowledge-domains")
    public Mono<ResponseEntity<KnowledgeDomainDTO>> createKnowledgeDomain(@Valid @RequestBody KnowledgeDomainDTO knowledgeDomainDTO)
        throws URISyntaxException {
        log.debug("REST request to save KnowledgeDomain : {}", knowledgeDomainDTO);
        if (knowledgeDomainDTO.getId() != null) {
            throw new BadRequestAlertException("A new knowledgeDomain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return knowledgeDomainService
            .save(knowledgeDomainDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/knowledge-domains/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /knowledge-domains/:id} : Updates an existing knowledgeDomain.
     *
     * @param id the id of the knowledgeDomainDTO to save.
     * @param knowledgeDomainDTO the knowledgeDomainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated knowledgeDomainDTO,
     * or with status {@code 400 (Bad Request)} if the knowledgeDomainDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the knowledgeDomainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/knowledge-domains/{id}")
    public Mono<ResponseEntity<KnowledgeDomainDTO>> updateKnowledgeDomain(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KnowledgeDomainDTO knowledgeDomainDTO
    ) throws URISyntaxException {
        log.debug("REST request to update KnowledgeDomain : {}, {}", id, knowledgeDomainDTO);
        if (knowledgeDomainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, knowledgeDomainDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return knowledgeDomainRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return knowledgeDomainService
                    .update(knowledgeDomainDTO)
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
     * {@code PATCH  /knowledge-domains/:id} : Partial updates given fields of an existing knowledgeDomain, field will ignore if it is null
     *
     * @param id the id of the knowledgeDomainDTO to save.
     * @param knowledgeDomainDTO the knowledgeDomainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated knowledgeDomainDTO,
     * or with status {@code 400 (Bad Request)} if the knowledgeDomainDTO is not valid,
     * or with status {@code 404 (Not Found)} if the knowledgeDomainDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the knowledgeDomainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/knowledge-domains/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<KnowledgeDomainDTO>> partialUpdateKnowledgeDomain(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KnowledgeDomainDTO knowledgeDomainDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update KnowledgeDomain partially : {}, {}", id, knowledgeDomainDTO);
        if (knowledgeDomainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, knowledgeDomainDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return knowledgeDomainRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<KnowledgeDomainDTO> result = knowledgeDomainService.partialUpdate(knowledgeDomainDTO);

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
     * {@code GET  /knowledge-domains} : get all the knowledgeDomains.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of knowledgeDomains in body.
     */
    @GetMapping("/knowledge-domains")
    public Mono<ResponseEntity<List<KnowledgeDomainDTO>>> getAllKnowledgeDomains(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of KnowledgeDomains");
        return knowledgeDomainService
            .countAll()
            .zipWith(knowledgeDomainService.findAll(pageable).collectList())
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
     * {@code GET  /knowledge-domains/:id} : get the "id" knowledgeDomain.
     *
     * @param id the id of the knowledgeDomainDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the knowledgeDomainDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/knowledge-domains/{id}")
    public Mono<ResponseEntity<KnowledgeDomainDTO>> getKnowledgeDomain(@PathVariable Long id) {
        log.debug("REST request to get KnowledgeDomain : {}", id);
        Mono<KnowledgeDomainDTO> knowledgeDomainDTO = knowledgeDomainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(knowledgeDomainDTO);
    }

    /**
     * {@code DELETE  /knowledge-domains/:id} : delete the "id" knowledgeDomain.
     *
     * @param id the id of the knowledgeDomainDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/knowledge-domains/{id}")
    public Mono<ResponseEntity<Void>> deleteKnowledgeDomain(@PathVariable Long id) {
        log.debug("REST request to delete KnowledgeDomain : {}", id);
        return knowledgeDomainService
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
