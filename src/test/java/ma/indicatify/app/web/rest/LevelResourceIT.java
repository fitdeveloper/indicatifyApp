package ma.indicatify.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import ma.indicatify.app.IntegrationTest;
import ma.indicatify.app.domain.Level;
import ma.indicatify.app.domain.enumeration.LevelEnum;
import ma.indicatify.app.repository.EntityManager;
import ma.indicatify.app.repository.LevelRepository;
import ma.indicatify.app.service.dto.LevelDTO;
import ma.indicatify.app.service.mapper.LevelMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link LevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LevelResourceIT {

    private static final String DEFAULT_NAME_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_NAME_LEVEL = "BBBBBBBBBB";

    private static final LevelEnum DEFAULT_VALUE_LEVEL = LevelEnum.N0;
    private static final LevelEnum UPDATED_VALUE_LEVEL = LevelEnum.N1;

    private static final String DEFAULT_DESC_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_DESC_LEVEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private LevelMapper levelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Level level;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Level createEntity(EntityManager em) {
        Level level = new Level().nameLevel(DEFAULT_NAME_LEVEL).valueLevel(DEFAULT_VALUE_LEVEL).descLevel(DEFAULT_DESC_LEVEL);
        return level;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Level createUpdatedEntity(EntityManager em) {
        Level level = new Level().nameLevel(UPDATED_NAME_LEVEL).valueLevel(UPDATED_VALUE_LEVEL).descLevel(UPDATED_DESC_LEVEL);
        return level;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Level.class).block();
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
        level = createEntity(em);
    }

    @Test
    void createLevel() throws Exception {
        int databaseSizeBeforeCreate = levelRepository.findAll().collectList().block().size();
        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeCreate + 1);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getNameLevel()).isEqualTo(DEFAULT_NAME_LEVEL);
        assertThat(testLevel.getValueLevel()).isEqualTo(DEFAULT_VALUE_LEVEL);
        assertThat(testLevel.getDescLevel()).isEqualTo(DEFAULT_DESC_LEVEL);
    }

    @Test
    void createLevelWithExistingId() throws Exception {
        // Create the Level with an existing ID
        level.setId(1L);
        LevelDTO levelDTO = levelMapper.toDto(level);

        int databaseSizeBeforeCreate = levelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = levelRepository.findAll().collectList().block().size();
        // set the field null
        level.setNameLevel(null);

        // Create the Level, which fails.
        LevelDTO levelDTO = levelMapper.toDto(level);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkValueLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = levelRepository.findAll().collectList().block().size();
        // set the field null
        level.setValueLevel(null);

        // Create the Level, which fails.
        LevelDTO levelDTO = levelMapper.toDto(level);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllLevels() {
        // Initialize the database
        levelRepository.save(level).block();

        // Get all the levelList
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
            .value(hasItem(level.getId().intValue()))
            .jsonPath("$.[*].nameLevel")
            .value(hasItem(DEFAULT_NAME_LEVEL))
            .jsonPath("$.[*].valueLevel")
            .value(hasItem(DEFAULT_VALUE_LEVEL.toString()))
            .jsonPath("$.[*].descLevel")
            .value(hasItem(DEFAULT_DESC_LEVEL));
    }

    @Test
    void getLevel() {
        // Initialize the database
        levelRepository.save(level).block();

        // Get the level
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, level.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(level.getId().intValue()))
            .jsonPath("$.nameLevel")
            .value(is(DEFAULT_NAME_LEVEL))
            .jsonPath("$.valueLevel")
            .value(is(DEFAULT_VALUE_LEVEL.toString()))
            .jsonPath("$.descLevel")
            .value(is(DEFAULT_DESC_LEVEL));
    }

    @Test
    void getNonExistingLevel() {
        // Get the level
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLevel() throws Exception {
        // Initialize the database
        levelRepository.save(level).block();

        int databaseSizeBeforeUpdate = levelRepository.findAll().collectList().block().size();

        // Update the level
        Level updatedLevel = levelRepository.findById(level.getId()).block();
        updatedLevel.nameLevel(UPDATED_NAME_LEVEL).valueLevel(UPDATED_VALUE_LEVEL).descLevel(UPDATED_DESC_LEVEL);
        LevelDTO levelDTO = levelMapper.toDto(updatedLevel);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, levelDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getNameLevel()).isEqualTo(UPDATED_NAME_LEVEL);
        assertThat(testLevel.getValueLevel()).isEqualTo(UPDATED_VALUE_LEVEL);
        assertThat(testLevel.getDescLevel()).isEqualTo(UPDATED_DESC_LEVEL);
    }

    @Test
    void putNonExistingLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().collectList().block().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, levelDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().collectList().block().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().collectList().block().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLevelWithPatch() throws Exception {
        // Initialize the database
        levelRepository.save(level).block();

        int databaseSizeBeforeUpdate = levelRepository.findAll().collectList().block().size();

        // Update the level using partial update
        Level partialUpdatedLevel = new Level();
        partialUpdatedLevel.setId(level.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLevel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLevel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getNameLevel()).isEqualTo(DEFAULT_NAME_LEVEL);
        assertThat(testLevel.getValueLevel()).isEqualTo(DEFAULT_VALUE_LEVEL);
        assertThat(testLevel.getDescLevel()).isEqualTo(DEFAULT_DESC_LEVEL);
    }

    @Test
    void fullUpdateLevelWithPatch() throws Exception {
        // Initialize the database
        levelRepository.save(level).block();

        int databaseSizeBeforeUpdate = levelRepository.findAll().collectList().block().size();

        // Update the level using partial update
        Level partialUpdatedLevel = new Level();
        partialUpdatedLevel.setId(level.getId());

        partialUpdatedLevel.nameLevel(UPDATED_NAME_LEVEL).valueLevel(UPDATED_VALUE_LEVEL).descLevel(UPDATED_DESC_LEVEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLevel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLevel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getNameLevel()).isEqualTo(UPDATED_NAME_LEVEL);
        assertThat(testLevel.getValueLevel()).isEqualTo(UPDATED_VALUE_LEVEL);
        assertThat(testLevel.getDescLevel()).isEqualTo(UPDATED_DESC_LEVEL);
    }

    @Test
    void patchNonExistingLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().collectList().block().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, levelDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().collectList().block().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().collectList().block().size();
        level.setId(count.incrementAndGet());

        // Create the Level
        LevelDTO levelDTO = levelMapper.toDto(level);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(levelDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLevel() {
        // Initialize the database
        levelRepository.save(level).block();

        int databaseSizeBeforeDelete = levelRepository.findAll().collectList().block().size();

        // Delete the level
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, level.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Level> levelList = levelRepository.findAll().collectList().block();
        assertThat(levelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
