package ma.indicatify.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import ma.indicatify.app.IntegrationTest;
import ma.indicatify.app.domain.KnowledgeDomain;
import ma.indicatify.app.repository.EntityManager;
import ma.indicatify.app.repository.KnowledgeDomainRepository;
import ma.indicatify.app.service.dto.KnowledgeDomainDTO;
import ma.indicatify.app.service.mapper.KnowledgeDomainMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link KnowledgeDomainResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class KnowledgeDomainResourceIT {

    private static final String DEFAULT_NAME_KNOWLEDGE_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_KNOWLEDGE_DOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_KNOWLEDGE_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DESC_KNOWLEDGE_DOMAIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/knowledge-domains";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KnowledgeDomainRepository knowledgeDomainRepository;

    @Autowired
    private KnowledgeDomainMapper knowledgeDomainMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private KnowledgeDomain knowledgeDomain;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KnowledgeDomain createEntity(EntityManager em) {
        KnowledgeDomain knowledgeDomain = new KnowledgeDomain()
            .nameKnowledgeDomain(DEFAULT_NAME_KNOWLEDGE_DOMAIN)
            .descKnowledgeDomain(DEFAULT_DESC_KNOWLEDGE_DOMAIN);
        return knowledgeDomain;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KnowledgeDomain createUpdatedEntity(EntityManager em) {
        KnowledgeDomain knowledgeDomain = new KnowledgeDomain()
            .nameKnowledgeDomain(UPDATED_NAME_KNOWLEDGE_DOMAIN)
            .descKnowledgeDomain(UPDATED_DESC_KNOWLEDGE_DOMAIN);
        return knowledgeDomain;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(KnowledgeDomain.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        knowledgeDomain = createEntity(em);
    }

    @Test
    void createKnowledgeDomain() throws Exception {
        int databaseSizeBeforeCreate = knowledgeDomainRepository.findAll().collectList().block().size();
        // Create the KnowledgeDomain
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(knowledgeDomain);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeCreate + 1);
        KnowledgeDomain testKnowledgeDomain = knowledgeDomainList.get(knowledgeDomainList.size() - 1);
        assertThat(testKnowledgeDomain.getNameKnowledgeDomain()).isEqualTo(DEFAULT_NAME_KNOWLEDGE_DOMAIN);
        assertThat(testKnowledgeDomain.getDescKnowledgeDomain()).isEqualTo(DEFAULT_DESC_KNOWLEDGE_DOMAIN);
    }

    @Test
    void createKnowledgeDomainWithExistingId() throws Exception {
        // Create the KnowledgeDomain with an existing ID
        knowledgeDomain.setId(1L);
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(knowledgeDomain);

        int databaseSizeBeforeCreate = knowledgeDomainRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameKnowledgeDomainIsRequired() throws Exception {
        int databaseSizeBeforeTest = knowledgeDomainRepository.findAll().collectList().block().size();
        // set the field null
        knowledgeDomain.setNameKnowledgeDomain(null);

        // Create the KnowledgeDomain, which fails.
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(knowledgeDomain);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllKnowledgeDomains() {
        // Initialize the database
        knowledgeDomainRepository.save(knowledgeDomain).block();

        // Get all the knowledgeDomainList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(knowledgeDomain.getId().intValue()))
            .jsonPath("$.[*].nameKnowledgeDomain")
            .value(hasItem(DEFAULT_NAME_KNOWLEDGE_DOMAIN))
            .jsonPath("$.[*].descKnowledgeDomain")
            .value(hasItem(DEFAULT_DESC_KNOWLEDGE_DOMAIN));
    }

    @Test
    void getKnowledgeDomain() {
        // Initialize the database
        knowledgeDomainRepository.save(knowledgeDomain).block();

        // Get the knowledgeDomain
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, knowledgeDomain.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(knowledgeDomain.getId().intValue()))
            .jsonPath("$.nameKnowledgeDomain")
            .value(is(DEFAULT_NAME_KNOWLEDGE_DOMAIN))
            .jsonPath("$.descKnowledgeDomain")
            .value(is(DEFAULT_DESC_KNOWLEDGE_DOMAIN));
    }

    @Test
    void getNonExistingKnowledgeDomain() {
        // Get the knowledgeDomain
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingKnowledgeDomain() throws Exception {
        // Initialize the database
        knowledgeDomainRepository.save(knowledgeDomain).block();

        int databaseSizeBeforeUpdate = knowledgeDomainRepository.findAll().collectList().block().size();

        // Update the knowledgeDomain
        KnowledgeDomain updatedKnowledgeDomain = knowledgeDomainRepository.findById(knowledgeDomain.getId()).block();
        updatedKnowledgeDomain.nameKnowledgeDomain(UPDATED_NAME_KNOWLEDGE_DOMAIN).descKnowledgeDomain(UPDATED_DESC_KNOWLEDGE_DOMAIN);
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(updatedKnowledgeDomain);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, knowledgeDomainDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeUpdate);
        KnowledgeDomain testKnowledgeDomain = knowledgeDomainList.get(knowledgeDomainList.size() - 1);
        assertThat(testKnowledgeDomain.getNameKnowledgeDomain()).isEqualTo(UPDATED_NAME_KNOWLEDGE_DOMAIN);
        assertThat(testKnowledgeDomain.getDescKnowledgeDomain()).isEqualTo(UPDATED_DESC_KNOWLEDGE_DOMAIN);
    }

    @Test
    void putNonExistingKnowledgeDomain() throws Exception {
        int databaseSizeBeforeUpdate = knowledgeDomainRepository.findAll().collectList().block().size();
        knowledgeDomain.setId(count.incrementAndGet());

        // Create the KnowledgeDomain
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(knowledgeDomain);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, knowledgeDomainDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchKnowledgeDomain() throws Exception {
        int databaseSizeBeforeUpdate = knowledgeDomainRepository.findAll().collectList().block().size();
        knowledgeDomain.setId(count.incrementAndGet());

        // Create the KnowledgeDomain
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(knowledgeDomain);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamKnowledgeDomain() throws Exception {
        int databaseSizeBeforeUpdate = knowledgeDomainRepository.findAll().collectList().block().size();
        knowledgeDomain.setId(count.incrementAndGet());

        // Create the KnowledgeDomain
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(knowledgeDomain);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateKnowledgeDomainWithPatch() throws Exception {
        // Initialize the database
        knowledgeDomainRepository.save(knowledgeDomain).block();

        int databaseSizeBeforeUpdate = knowledgeDomainRepository.findAll().collectList().block().size();

        // Update the knowledgeDomain using partial update
        KnowledgeDomain partialUpdatedKnowledgeDomain = new KnowledgeDomain();
        partialUpdatedKnowledgeDomain.setId(knowledgeDomain.getId());

        partialUpdatedKnowledgeDomain.nameKnowledgeDomain(UPDATED_NAME_KNOWLEDGE_DOMAIN).descKnowledgeDomain(UPDATED_DESC_KNOWLEDGE_DOMAIN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedKnowledgeDomain.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedKnowledgeDomain))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeUpdate);
        KnowledgeDomain testKnowledgeDomain = knowledgeDomainList.get(knowledgeDomainList.size() - 1);
        assertThat(testKnowledgeDomain.getNameKnowledgeDomain()).isEqualTo(UPDATED_NAME_KNOWLEDGE_DOMAIN);
        assertThat(testKnowledgeDomain.getDescKnowledgeDomain()).isEqualTo(UPDATED_DESC_KNOWLEDGE_DOMAIN);
    }

    @Test
    void fullUpdateKnowledgeDomainWithPatch() throws Exception {
        // Initialize the database
        knowledgeDomainRepository.save(knowledgeDomain).block();

        int databaseSizeBeforeUpdate = knowledgeDomainRepository.findAll().collectList().block().size();

        // Update the knowledgeDomain using partial update
        KnowledgeDomain partialUpdatedKnowledgeDomain = new KnowledgeDomain();
        partialUpdatedKnowledgeDomain.setId(knowledgeDomain.getId());

        partialUpdatedKnowledgeDomain.nameKnowledgeDomain(UPDATED_NAME_KNOWLEDGE_DOMAIN).descKnowledgeDomain(UPDATED_DESC_KNOWLEDGE_DOMAIN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedKnowledgeDomain.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedKnowledgeDomain))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeUpdate);
        KnowledgeDomain testKnowledgeDomain = knowledgeDomainList.get(knowledgeDomainList.size() - 1);
        assertThat(testKnowledgeDomain.getNameKnowledgeDomain()).isEqualTo(UPDATED_NAME_KNOWLEDGE_DOMAIN);
        assertThat(testKnowledgeDomain.getDescKnowledgeDomain()).isEqualTo(UPDATED_DESC_KNOWLEDGE_DOMAIN);
    }

    @Test
    void patchNonExistingKnowledgeDomain() throws Exception {
        int databaseSizeBeforeUpdate = knowledgeDomainRepository.findAll().collectList().block().size();
        knowledgeDomain.setId(count.incrementAndGet());

        // Create the KnowledgeDomain
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(knowledgeDomain);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, knowledgeDomainDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchKnowledgeDomain() throws Exception {
        int databaseSizeBeforeUpdate = knowledgeDomainRepository.findAll().collectList().block().size();
        knowledgeDomain.setId(count.incrementAndGet());

        // Create the KnowledgeDomain
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(knowledgeDomain);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamKnowledgeDomain() throws Exception {
        int databaseSizeBeforeUpdate = knowledgeDomainRepository.findAll().collectList().block().size();
        knowledgeDomain.setId(count.incrementAndGet());

        // Create the KnowledgeDomain
        KnowledgeDomainDTO knowledgeDomainDTO = knowledgeDomainMapper.toDto(knowledgeDomain);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(knowledgeDomainDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the KnowledgeDomain in the database
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteKnowledgeDomain() {
        // Initialize the database
        knowledgeDomainRepository.save(knowledgeDomain).block();

        int databaseSizeBeforeDelete = knowledgeDomainRepository.findAll().collectList().block().size();

        // Delete the knowledgeDomain
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, knowledgeDomain.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<KnowledgeDomain> knowledgeDomainList = knowledgeDomainRepository.findAll().collectList().block();
        assertThat(knowledgeDomainList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
