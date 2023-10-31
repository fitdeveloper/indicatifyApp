package ma.indicatify.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import ma.indicatify.app.IntegrationTest;
import ma.indicatify.app.domain.JobTitle;
import ma.indicatify.app.repository.EntityManager;
import ma.indicatify.app.repository.JobTitleRepository;
import ma.indicatify.app.service.dto.JobTitleDTO;
import ma.indicatify.app.service.mapper.JobTitleMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link JobTitleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class JobTitleResourceIT {

    private static final String DEFAULT_NAME_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_DESC_JOB_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/job-titles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobTitleRepository jobTitleRepository;

    @Autowired
    private JobTitleMapper jobTitleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private JobTitle jobTitle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobTitle createEntity(EntityManager em) {
        JobTitle jobTitle = new JobTitle().nameJobTitle(DEFAULT_NAME_JOB_TITLE).descJobTitle(DEFAULT_DESC_JOB_TITLE);
        return jobTitle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobTitle createUpdatedEntity(EntityManager em) {
        JobTitle jobTitle = new JobTitle().nameJobTitle(UPDATED_NAME_JOB_TITLE).descJobTitle(UPDATED_DESC_JOB_TITLE);
        return jobTitle;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(JobTitle.class).block();
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
        jobTitle = createEntity(em);
    }

    @Test
    void createJobTitle() throws Exception {
        int databaseSizeBeforeCreate = jobTitleRepository.findAll().collectList().block().size();
        // Create the JobTitle
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(jobTitle);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeCreate + 1);
        JobTitle testJobTitle = jobTitleList.get(jobTitleList.size() - 1);
        assertThat(testJobTitle.getNameJobTitle()).isEqualTo(DEFAULT_NAME_JOB_TITLE);
        assertThat(testJobTitle.getDescJobTitle()).isEqualTo(DEFAULT_DESC_JOB_TITLE);
    }

    @Test
    void createJobTitleWithExistingId() throws Exception {
        // Create the JobTitle with an existing ID
        jobTitle.setId(1L);
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(jobTitle);

        int databaseSizeBeforeCreate = jobTitleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameJobTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobTitleRepository.findAll().collectList().block().size();
        // set the field null
        jobTitle.setNameJobTitle(null);

        // Create the JobTitle, which fails.
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(jobTitle);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllJobTitles() {
        // Initialize the database
        jobTitleRepository.save(jobTitle).block();

        // Get all the jobTitleList
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
            .value(hasItem(jobTitle.getId().intValue()))
            .jsonPath("$.[*].nameJobTitle")
            .value(hasItem(DEFAULT_NAME_JOB_TITLE))
            .jsonPath("$.[*].descJobTitle")
            .value(hasItem(DEFAULT_DESC_JOB_TITLE));
    }

    @Test
    void getJobTitle() {
        // Initialize the database
        jobTitleRepository.save(jobTitle).block();

        // Get the jobTitle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, jobTitle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(jobTitle.getId().intValue()))
            .jsonPath("$.nameJobTitle")
            .value(is(DEFAULT_NAME_JOB_TITLE))
            .jsonPath("$.descJobTitle")
            .value(is(DEFAULT_DESC_JOB_TITLE));
    }

    @Test
    void getNonExistingJobTitle() {
        // Get the jobTitle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingJobTitle() throws Exception {
        // Initialize the database
        jobTitleRepository.save(jobTitle).block();

        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().collectList().block().size();

        // Update the jobTitle
        JobTitle updatedJobTitle = jobTitleRepository.findById(jobTitle.getId()).block();
        updatedJobTitle.nameJobTitle(UPDATED_NAME_JOB_TITLE).descJobTitle(UPDATED_DESC_JOB_TITLE);
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(updatedJobTitle);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, jobTitleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeUpdate);
        JobTitle testJobTitle = jobTitleList.get(jobTitleList.size() - 1);
        assertThat(testJobTitle.getNameJobTitle()).isEqualTo(UPDATED_NAME_JOB_TITLE);
        assertThat(testJobTitle.getDescJobTitle()).isEqualTo(UPDATED_DESC_JOB_TITLE);
    }

    @Test
    void putNonExistingJobTitle() throws Exception {
        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().collectList().block().size();
        jobTitle.setId(count.incrementAndGet());

        // Create the JobTitle
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(jobTitle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, jobTitleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchJobTitle() throws Exception {
        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().collectList().block().size();
        jobTitle.setId(count.incrementAndGet());

        // Create the JobTitle
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(jobTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamJobTitle() throws Exception {
        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().collectList().block().size();
        jobTitle.setId(count.incrementAndGet());

        // Create the JobTitle
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(jobTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateJobTitleWithPatch() throws Exception {
        // Initialize the database
        jobTitleRepository.save(jobTitle).block();

        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().collectList().block().size();

        // Update the jobTitle using partial update
        JobTitle partialUpdatedJobTitle = new JobTitle();
        partialUpdatedJobTitle.setId(jobTitle.getId());

        partialUpdatedJobTitle.descJobTitle(UPDATED_DESC_JOB_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJobTitle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedJobTitle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeUpdate);
        JobTitle testJobTitle = jobTitleList.get(jobTitleList.size() - 1);
        assertThat(testJobTitle.getNameJobTitle()).isEqualTo(DEFAULT_NAME_JOB_TITLE);
        assertThat(testJobTitle.getDescJobTitle()).isEqualTo(UPDATED_DESC_JOB_TITLE);
    }

    @Test
    void fullUpdateJobTitleWithPatch() throws Exception {
        // Initialize the database
        jobTitleRepository.save(jobTitle).block();

        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().collectList().block().size();

        // Update the jobTitle using partial update
        JobTitle partialUpdatedJobTitle = new JobTitle();
        partialUpdatedJobTitle.setId(jobTitle.getId());

        partialUpdatedJobTitle.nameJobTitle(UPDATED_NAME_JOB_TITLE).descJobTitle(UPDATED_DESC_JOB_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJobTitle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedJobTitle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeUpdate);
        JobTitle testJobTitle = jobTitleList.get(jobTitleList.size() - 1);
        assertThat(testJobTitle.getNameJobTitle()).isEqualTo(UPDATED_NAME_JOB_TITLE);
        assertThat(testJobTitle.getDescJobTitle()).isEqualTo(UPDATED_DESC_JOB_TITLE);
    }

    @Test
    void patchNonExistingJobTitle() throws Exception {
        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().collectList().block().size();
        jobTitle.setId(count.incrementAndGet());

        // Create the JobTitle
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(jobTitle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, jobTitleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchJobTitle() throws Exception {
        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().collectList().block().size();
        jobTitle.setId(count.incrementAndGet());

        // Create the JobTitle
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(jobTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamJobTitle() throws Exception {
        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().collectList().block().size();
        jobTitle.setId(count.incrementAndGet());

        // Create the JobTitle
        JobTitleDTO jobTitleDTO = jobTitleMapper.toDto(jobTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobTitleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the JobTitle in the database
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteJobTitle() {
        // Initialize the database
        jobTitleRepository.save(jobTitle).block();

        int databaseSizeBeforeDelete = jobTitleRepository.findAll().collectList().block().size();

        // Delete the jobTitle
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, jobTitle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<JobTitle> jobTitleList = jobTitleRepository.findAll().collectList().block();
        assertThat(jobTitleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
