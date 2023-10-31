package ma.indicatify.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import ma.indicatify.app.IntegrationTest;
import ma.indicatify.app.domain.Activity;
import ma.indicatify.app.domain.Perimeter;
import ma.indicatify.app.repository.EntityManager;
import ma.indicatify.app.repository.PerimeterRepository;
import ma.indicatify.app.service.dto.PerimeterDTO;
import ma.indicatify.app.service.mapper.PerimeterMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PerimeterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PerimeterResourceIT {

    private static final String DEFAULT_NAME_PERIMETER = "AAAAAAAAAA";
    private static final String UPDATED_NAME_PERIMETER = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_PERIMETER = "AAAAAAAAAA";
    private static final String UPDATED_DESC_PERIMETER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/perimeters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PerimeterRepository perimeterRepository;

    @Autowired
    private PerimeterMapper perimeterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Perimeter perimeter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Perimeter createEntity(EntityManager em) {
        Perimeter perimeter = new Perimeter().namePerimeter(DEFAULT_NAME_PERIMETER).descPerimeter(DEFAULT_DESC_PERIMETER);
        // Add required entity
        Activity activity;
        activity = em.insert(ActivityResourceIT.createEntity(em)).block();
        perimeter.setActivity(activity);
        return perimeter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Perimeter createUpdatedEntity(EntityManager em) {
        Perimeter perimeter = new Perimeter().namePerimeter(UPDATED_NAME_PERIMETER).descPerimeter(UPDATED_DESC_PERIMETER);
        // Add required entity
        Activity activity;
        activity = em.insert(ActivityResourceIT.createUpdatedEntity(em)).block();
        perimeter.setActivity(activity);
        return perimeter;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Perimeter.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ActivityResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        perimeter = createEntity(em);
    }

    @Test
    void createPerimeter() throws Exception {
        int databaseSizeBeforeCreate = perimeterRepository.findAll().collectList().block().size();
        // Create the Perimeter
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(perimeter);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeCreate + 1);
        Perimeter testPerimeter = perimeterList.get(perimeterList.size() - 1);
        assertThat(testPerimeter.getNamePerimeter()).isEqualTo(DEFAULT_NAME_PERIMETER);
        assertThat(testPerimeter.getDescPerimeter()).isEqualTo(DEFAULT_DESC_PERIMETER);
    }

    @Test
    void createPerimeterWithExistingId() throws Exception {
        // Create the Perimeter with an existing ID
        perimeter.setId(1L);
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(perimeter);

        int databaseSizeBeforeCreate = perimeterRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNamePerimeterIsRequired() throws Exception {
        int databaseSizeBeforeTest = perimeterRepository.findAll().collectList().block().size();
        // set the field null
        perimeter.setNamePerimeter(null);

        // Create the Perimeter, which fails.
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(perimeter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPerimeters() {
        // Initialize the database
        perimeterRepository.save(perimeter).block();

        // Get all the perimeterList
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
            .value(hasItem(perimeter.getId().intValue()))
            .jsonPath("$.[*].namePerimeter")
            .value(hasItem(DEFAULT_NAME_PERIMETER))
            .jsonPath("$.[*].descPerimeter")
            .value(hasItem(DEFAULT_DESC_PERIMETER));
    }

    @Test
    void getPerimeter() {
        // Initialize the database
        perimeterRepository.save(perimeter).block();

        // Get the perimeter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, perimeter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(perimeter.getId().intValue()))
            .jsonPath("$.namePerimeter")
            .value(is(DEFAULT_NAME_PERIMETER))
            .jsonPath("$.descPerimeter")
            .value(is(DEFAULT_DESC_PERIMETER));
    }

    @Test
    void getNonExistingPerimeter() {
        // Get the perimeter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPerimeter() throws Exception {
        // Initialize the database
        perimeterRepository.save(perimeter).block();

        int databaseSizeBeforeUpdate = perimeterRepository.findAll().collectList().block().size();

        // Update the perimeter
        Perimeter updatedPerimeter = perimeterRepository.findById(perimeter.getId()).block();
        updatedPerimeter.namePerimeter(UPDATED_NAME_PERIMETER).descPerimeter(UPDATED_DESC_PERIMETER);
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(updatedPerimeter);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, perimeterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeUpdate);
        Perimeter testPerimeter = perimeterList.get(perimeterList.size() - 1);
        assertThat(testPerimeter.getNamePerimeter()).isEqualTo(UPDATED_NAME_PERIMETER);
        assertThat(testPerimeter.getDescPerimeter()).isEqualTo(UPDATED_DESC_PERIMETER);
    }

    @Test
    void putNonExistingPerimeter() throws Exception {
        int databaseSizeBeforeUpdate = perimeterRepository.findAll().collectList().block().size();
        perimeter.setId(count.incrementAndGet());

        // Create the Perimeter
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(perimeter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, perimeterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPerimeter() throws Exception {
        int databaseSizeBeforeUpdate = perimeterRepository.findAll().collectList().block().size();
        perimeter.setId(count.incrementAndGet());

        // Create the Perimeter
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(perimeter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPerimeter() throws Exception {
        int databaseSizeBeforeUpdate = perimeterRepository.findAll().collectList().block().size();
        perimeter.setId(count.incrementAndGet());

        // Create the Perimeter
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(perimeter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePerimeterWithPatch() throws Exception {
        // Initialize the database
        perimeterRepository.save(perimeter).block();

        int databaseSizeBeforeUpdate = perimeterRepository.findAll().collectList().block().size();

        // Update the perimeter using partial update
        Perimeter partialUpdatedPerimeter = new Perimeter();
        partialUpdatedPerimeter.setId(perimeter.getId());

        partialUpdatedPerimeter.namePerimeter(UPDATED_NAME_PERIMETER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPerimeter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPerimeter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeUpdate);
        Perimeter testPerimeter = perimeterList.get(perimeterList.size() - 1);
        assertThat(testPerimeter.getNamePerimeter()).isEqualTo(UPDATED_NAME_PERIMETER);
        assertThat(testPerimeter.getDescPerimeter()).isEqualTo(DEFAULT_DESC_PERIMETER);
    }

    @Test
    void fullUpdatePerimeterWithPatch() throws Exception {
        // Initialize the database
        perimeterRepository.save(perimeter).block();

        int databaseSizeBeforeUpdate = perimeterRepository.findAll().collectList().block().size();

        // Update the perimeter using partial update
        Perimeter partialUpdatedPerimeter = new Perimeter();
        partialUpdatedPerimeter.setId(perimeter.getId());

        partialUpdatedPerimeter.namePerimeter(UPDATED_NAME_PERIMETER).descPerimeter(UPDATED_DESC_PERIMETER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPerimeter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPerimeter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeUpdate);
        Perimeter testPerimeter = perimeterList.get(perimeterList.size() - 1);
        assertThat(testPerimeter.getNamePerimeter()).isEqualTo(UPDATED_NAME_PERIMETER);
        assertThat(testPerimeter.getDescPerimeter()).isEqualTo(UPDATED_DESC_PERIMETER);
    }

    @Test
    void patchNonExistingPerimeter() throws Exception {
        int databaseSizeBeforeUpdate = perimeterRepository.findAll().collectList().block().size();
        perimeter.setId(count.incrementAndGet());

        // Create the Perimeter
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(perimeter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, perimeterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPerimeter() throws Exception {
        int databaseSizeBeforeUpdate = perimeterRepository.findAll().collectList().block().size();
        perimeter.setId(count.incrementAndGet());

        // Create the Perimeter
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(perimeter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPerimeter() throws Exception {
        int databaseSizeBeforeUpdate = perimeterRepository.findAll().collectList().block().size();
        perimeter.setId(count.incrementAndGet());

        // Create the Perimeter
        PerimeterDTO perimeterDTO = perimeterMapper.toDto(perimeter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(perimeterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Perimeter in the database
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePerimeter() {
        // Initialize the database
        perimeterRepository.save(perimeter).block();

        int databaseSizeBeforeDelete = perimeterRepository.findAll().collectList().block().size();

        // Delete the perimeter
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, perimeter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Perimeter> perimeterList = perimeterRepository.findAll().collectList().block();
        assertThat(perimeterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
