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
import ma.indicatify.app.repository.ActivityRepository;
import ma.indicatify.app.repository.EntityManager;
import ma.indicatify.app.service.dto.ActivityDTO;
import ma.indicatify.app.service.mapper.ActivityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ActivityResourceIT {

    private static final String DEFAULT_NAME_ACTIVITY = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ACTIVITY = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_ACTIVITY = "AAAAAAAAAA";
    private static final String UPDATED_DESC_ACTIVITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Activity activity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createEntity(EntityManager em) {
        Activity activity = new Activity().nameActivity(DEFAULT_NAME_ACTIVITY).descActivity(DEFAULT_DESC_ACTIVITY);
        return activity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createUpdatedEntity(EntityManager em) {
        Activity activity = new Activity().nameActivity(UPDATED_NAME_ACTIVITY).descActivity(UPDATED_DESC_ACTIVITY);
        return activity;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Activity.class).block();
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
        activity = createEntity(em);
    }

    @Test
    void createActivity() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().collectList().block().size();
        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate + 1);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getNameActivity()).isEqualTo(DEFAULT_NAME_ACTIVITY);
        assertThat(testActivity.getDescActivity()).isEqualTo(DEFAULT_DESC_ACTIVITY);
    }

    @Test
    void createActivityWithExistingId() throws Exception {
        // Create the Activity with an existing ID
        activity.setId(1L);
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        int databaseSizeBeforeCreate = activityRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameActivityIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().collectList().block().size();
        // set the field null
        activity.setNameActivity(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllActivities() {
        // Initialize the database
        activityRepository.save(activity).block();

        // Get all the activityList
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
            .value(hasItem(activity.getId().intValue()))
            .jsonPath("$.[*].nameActivity")
            .value(hasItem(DEFAULT_NAME_ACTIVITY))
            .jsonPath("$.[*].descActivity")
            .value(hasItem(DEFAULT_DESC_ACTIVITY));
    }

    @Test
    void getActivity() {
        // Initialize the database
        activityRepository.save(activity).block();

        // Get the activity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, activity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(activity.getId().intValue()))
            .jsonPath("$.nameActivity")
            .value(is(DEFAULT_NAME_ACTIVITY))
            .jsonPath("$.descActivity")
            .value(is(DEFAULT_DESC_ACTIVITY));
    }

    @Test
    void getNonExistingActivity() {
        // Get the activity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingActivity() throws Exception {
        // Initialize the database
        activityRepository.save(activity).block();

        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();

        // Update the activity
        Activity updatedActivity = activityRepository.findById(activity.getId()).block();
        updatedActivity.nameActivity(UPDATED_NAME_ACTIVITY).descActivity(UPDATED_DESC_ACTIVITY);
        ActivityDTO activityDTO = activityMapper.toDto(updatedActivity);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, activityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getNameActivity()).isEqualTo(UPDATED_NAME_ACTIVITY);
        assertThat(testActivity.getDescActivity()).isEqualTo(UPDATED_DESC_ACTIVITY);
    }

    @Test
    void putNonExistingActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(count.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, activityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(count.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(count.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        activityRepository.save(activity).block();

        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        partialUpdatedActivity.nameActivity(UPDATED_NAME_ACTIVITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedActivity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getNameActivity()).isEqualTo(UPDATED_NAME_ACTIVITY);
        assertThat(testActivity.getDescActivity()).isEqualTo(DEFAULT_DESC_ACTIVITY);
    }

    @Test
    void fullUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        activityRepository.save(activity).block();

        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        partialUpdatedActivity.nameActivity(UPDATED_NAME_ACTIVITY).descActivity(UPDATED_DESC_ACTIVITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedActivity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getNameActivity()).isEqualTo(UPDATED_NAME_ACTIVITY);
        assertThat(testActivity.getDescActivity()).isEqualTo(UPDATED_DESC_ACTIVITY);
    }

    @Test
    void patchNonExistingActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(count.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, activityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(count.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(count.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteActivity() {
        // Initialize the database
        activityRepository.save(activity).block();

        int databaseSizeBeforeDelete = activityRepository.findAll().collectList().block().size();

        // Delete the activity
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, activity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
