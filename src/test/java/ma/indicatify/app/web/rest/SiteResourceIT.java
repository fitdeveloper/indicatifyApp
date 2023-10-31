package ma.indicatify.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import ma.indicatify.app.IntegrationTest;
import ma.indicatify.app.domain.Site;
import ma.indicatify.app.repository.EntityManager;
import ma.indicatify.app.repository.SiteRepository;
import ma.indicatify.app.service.dto.SiteDTO;
import ma.indicatify.app.service.mapper.SiteMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link SiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SiteResourceIT {

    private static final String DEFAULT_NAME_SITE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SITE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_SITE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_SITE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_SITE = "AAAAAAAAAA";
    private static final String UPDATED_DESC_SITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Site site;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createEntity(EntityManager em) {
        Site site = new Site().nameSite(DEFAULT_NAME_SITE).addressSite(DEFAULT_ADDRESS_SITE).descSite(DEFAULT_DESC_SITE);
        return site;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createUpdatedEntity(EntityManager em) {
        Site site = new Site().nameSite(UPDATED_NAME_SITE).addressSite(UPDATED_ADDRESS_SITE).descSite(UPDATED_DESC_SITE);
        return site;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Site.class).block();
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
        site = createEntity(em);
    }

    @Test
    void createSite() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().collectList().block().size();
        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getNameSite()).isEqualTo(DEFAULT_NAME_SITE);
        assertThat(testSite.getAddressSite()).isEqualTo(DEFAULT_ADDRESS_SITE);
        assertThat(testSite.getDescSite()).isEqualTo(DEFAULT_DESC_SITE);
    }

    @Test
    void createSiteWithExistingId() throws Exception {
        // Create the Site with an existing ID
        site.setId(1L);
        SiteDTO siteDTO = siteMapper.toDto(site);

        int databaseSizeBeforeCreate = siteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameSiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().collectList().block().size();
        // set the field null
        site.setNameSite(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAddressSiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().collectList().block().size();
        // set the field null
        site.setAddressSite(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSites() {
        // Initialize the database
        siteRepository.save(site).block();

        // Get all the siteList
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
            .value(hasItem(site.getId().intValue()))
            .jsonPath("$.[*].nameSite")
            .value(hasItem(DEFAULT_NAME_SITE))
            .jsonPath("$.[*].addressSite")
            .value(hasItem(DEFAULT_ADDRESS_SITE))
            .jsonPath("$.[*].descSite")
            .value(hasItem(DEFAULT_DESC_SITE));
    }

    @Test
    void getSite() {
        // Initialize the database
        siteRepository.save(site).block();

        // Get the site
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, site.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(site.getId().intValue()))
            .jsonPath("$.nameSite")
            .value(is(DEFAULT_NAME_SITE))
            .jsonPath("$.addressSite")
            .value(is(DEFAULT_ADDRESS_SITE))
            .jsonPath("$.descSite")
            .value(is(DEFAULT_DESC_SITE));
    }

    @Test
    void getNonExistingSite() {
        // Get the site
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSite() throws Exception {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).block();
        updatedSite.nameSite(UPDATED_NAME_SITE).addressSite(UPDATED_ADDRESS_SITE).descSite(UPDATED_DESC_SITE);
        SiteDTO siteDTO = siteMapper.toDto(updatedSite);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, siteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getNameSite()).isEqualTo(UPDATED_NAME_SITE);
        assertThat(testSite.getAddressSite()).isEqualTo(UPDATED_ADDRESS_SITE);
        assertThat(testSite.getDescSite()).isEqualTo(UPDATED_DESC_SITE);
    }

    @Test
    void putNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, siteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite.addressSite(UPDATED_ADDRESS_SITE).descSite(UPDATED_DESC_SITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getNameSite()).isEqualTo(DEFAULT_NAME_SITE);
        assertThat(testSite.getAddressSite()).isEqualTo(UPDATED_ADDRESS_SITE);
        assertThat(testSite.getDescSite()).isEqualTo(UPDATED_DESC_SITE);
    }

    @Test
    void fullUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite.nameSite(UPDATED_NAME_SITE).addressSite(UPDATED_ADDRESS_SITE).descSite(UPDATED_DESC_SITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getNameSite()).isEqualTo(UPDATED_NAME_SITE);
        assertThat(testSite.getAddressSite()).isEqualTo(UPDATED_ADDRESS_SITE);
        assertThat(testSite.getDescSite()).isEqualTo(UPDATED_DESC_SITE);
    }

    @Test
    void patchNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, siteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSite() {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeDelete = siteRepository.findAll().collectList().block().size();

        // Delete the site
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, site.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
