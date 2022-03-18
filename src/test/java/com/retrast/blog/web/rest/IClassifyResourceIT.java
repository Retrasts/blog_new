package com.retrast.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.retrast.blog.IntegrationTest;
import com.retrast.blog.domain.IClassify;
import com.retrast.blog.repository.IClassifyRepository;
import com.retrast.blog.service.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link IClassifyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IClassifyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_ALIAS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/i-classifies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IClassifyRepository iClassifyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IClassify iClassify;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IClassify createEntity(EntityManager em) {
        IClassify iClassify = new IClassify()
            .name(DEFAULT_NAME)
            .alias(DEFAULT_ALIAS)
            .description(DEFAULT_DESCRIPTION)
            .parentId(DEFAULT_PARENT_ID);
        return iClassify;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IClassify createUpdatedEntity(EntityManager em) {
        IClassify iClassify = new IClassify()
            .name(UPDATED_NAME)
            .alias(UPDATED_ALIAS)
            .description(UPDATED_DESCRIPTION)
            .parentId(UPDATED_PARENT_ID);
        return iClassify;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IClassify.class).block();
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
        iClassify = createEntity(em);
    }

    @Test
    void createIClassify() throws Exception {
        int databaseSizeBeforeCreate = iClassifyRepository.findAll().collectList().block().size();
        // Create the IClassify
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iClassify))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeCreate + 1);
        IClassify testIClassify = iClassifyList.get(iClassifyList.size() - 1);
        assertThat(testIClassify.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIClassify.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testIClassify.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIClassify.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
    }

    @Test
    void createIClassifyWithExistingId() throws Exception {
        // Create the IClassify with an existing ID
        iClassify.setId(1L);

        int databaseSizeBeforeCreate = iClassifyRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iClassify))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIClassifiesAsStream() {
        // Initialize the database
        iClassifyRepository.save(iClassify).block();

        List<IClassify> iClassifyList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(IClassify.class)
            .getResponseBody()
            .filter(iClassify::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(iClassifyList).isNotNull();
        assertThat(iClassifyList).hasSize(1);
        IClassify testIClassify = iClassifyList.get(0);
        assertThat(testIClassify.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIClassify.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testIClassify.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIClassify.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
    }

    @Test
    void getAllIClassifies() {
        // Initialize the database
        iClassifyRepository.save(iClassify).block();

        // Get all the iClassifyList
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
            .value(hasItem(iClassify.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].alias")
            .value(hasItem(DEFAULT_ALIAS))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].parentId")
            .value(hasItem(DEFAULT_PARENT_ID.intValue()));
    }

    @Test
    void getIClassify() {
        // Initialize the database
        iClassifyRepository.save(iClassify).block();

        // Get the iClassify
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, iClassify.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(iClassify.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.alias")
            .value(is(DEFAULT_ALIAS))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.parentId")
            .value(is(DEFAULT_PARENT_ID.intValue()));
    }

    @Test
    void getNonExistingIClassify() {
        // Get the iClassify
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIClassify() throws Exception {
        // Initialize the database
        iClassifyRepository.save(iClassify).block();

        int databaseSizeBeforeUpdate = iClassifyRepository.findAll().collectList().block().size();

        // Update the iClassify
        IClassify updatedIClassify = iClassifyRepository.findById(iClassify.getId()).block();
        updatedIClassify.name(UPDATED_NAME).alias(UPDATED_ALIAS).description(UPDATED_DESCRIPTION).parentId(UPDATED_PARENT_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIClassify.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIClassify))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeUpdate);
        IClassify testIClassify = iClassifyList.get(iClassifyList.size() - 1);
        assertThat(testIClassify.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIClassify.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testIClassify.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIClassify.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    void putNonExistingIClassify() throws Exception {
        int databaseSizeBeforeUpdate = iClassifyRepository.findAll().collectList().block().size();
        iClassify.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, iClassify.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iClassify))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIClassify() throws Exception {
        int databaseSizeBeforeUpdate = iClassifyRepository.findAll().collectList().block().size();
        iClassify.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iClassify))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIClassify() throws Exception {
        int databaseSizeBeforeUpdate = iClassifyRepository.findAll().collectList().block().size();
        iClassify.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iClassify))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIClassifyWithPatch() throws Exception {
        // Initialize the database
        iClassifyRepository.save(iClassify).block();

        int databaseSizeBeforeUpdate = iClassifyRepository.findAll().collectList().block().size();

        // Update the iClassify using partial update
        IClassify partialUpdatedIClassify = new IClassify();
        partialUpdatedIClassify.setId(iClassify.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIClassify.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIClassify))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeUpdate);
        IClassify testIClassify = iClassifyList.get(iClassifyList.size() - 1);
        assertThat(testIClassify.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIClassify.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testIClassify.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIClassify.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
    }

    @Test
    void fullUpdateIClassifyWithPatch() throws Exception {
        // Initialize the database
        iClassifyRepository.save(iClassify).block();

        int databaseSizeBeforeUpdate = iClassifyRepository.findAll().collectList().block().size();

        // Update the iClassify using partial update
        IClassify partialUpdatedIClassify = new IClassify();
        partialUpdatedIClassify.setId(iClassify.getId());

        partialUpdatedIClassify.name(UPDATED_NAME).alias(UPDATED_ALIAS).description(UPDATED_DESCRIPTION).parentId(UPDATED_PARENT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIClassify.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIClassify))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeUpdate);
        IClassify testIClassify = iClassifyList.get(iClassifyList.size() - 1);
        assertThat(testIClassify.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIClassify.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testIClassify.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIClassify.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    void patchNonExistingIClassify() throws Exception {
        int databaseSizeBeforeUpdate = iClassifyRepository.findAll().collectList().block().size();
        iClassify.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, iClassify.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iClassify))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIClassify() throws Exception {
        int databaseSizeBeforeUpdate = iClassifyRepository.findAll().collectList().block().size();
        iClassify.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iClassify))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIClassify() throws Exception {
        int databaseSizeBeforeUpdate = iClassifyRepository.findAll().collectList().block().size();
        iClassify.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iClassify))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IClassify in the database
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIClassify() {
        // Initialize the database
        iClassifyRepository.save(iClassify).block();

        int databaseSizeBeforeDelete = iClassifyRepository.findAll().collectList().block().size();

        // Delete the iClassify
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, iClassify.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IClassify> iClassifyList = iClassifyRepository.findAll().collectList().block();
        assertThat(iClassifyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
