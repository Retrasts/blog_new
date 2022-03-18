package com.retrast.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.retrast.blog.IntegrationTest;
import com.retrast.blog.domain.ILabel;
import com.retrast.blog.repository.ILabelRepository;
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
 * Integration tests for the {@link ILabelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ILabelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_ALIAS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/i-labels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ILabelRepository iLabelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ILabel iLabel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ILabel createEntity(EntityManager em) {
        ILabel iLabel = new ILabel().name(DEFAULT_NAME).alias(DEFAULT_ALIAS).description(DEFAULT_DESCRIPTION).parentId(DEFAULT_PARENT_ID);
        return iLabel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ILabel createUpdatedEntity(EntityManager em) {
        ILabel iLabel = new ILabel().name(UPDATED_NAME).alias(UPDATED_ALIAS).description(UPDATED_DESCRIPTION).parentId(UPDATED_PARENT_ID);
        return iLabel;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ILabel.class).block();
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
        iLabel = createEntity(em);
    }

    @Test
    void createILabel() throws Exception {
        int databaseSizeBeforeCreate = iLabelRepository.findAll().collectList().block().size();
        // Create the ILabel
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iLabel))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeCreate + 1);
        ILabel testILabel = iLabelList.get(iLabelList.size() - 1);
        assertThat(testILabel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testILabel.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testILabel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testILabel.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
    }

    @Test
    void createILabelWithExistingId() throws Exception {
        // Create the ILabel with an existing ID
        iLabel.setId(1L);

        int databaseSizeBeforeCreate = iLabelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iLabel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllILabels() {
        // Initialize the database
        iLabelRepository.save(iLabel).block();

        // Get all the iLabelList
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
            .value(hasItem(iLabel.getId().intValue()))
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
    void getILabel() {
        // Initialize the database
        iLabelRepository.save(iLabel).block();

        // Get the iLabel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, iLabel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(iLabel.getId().intValue()))
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
    void getNonExistingILabel() {
        // Get the iLabel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewILabel() throws Exception {
        // Initialize the database
        iLabelRepository.save(iLabel).block();

        int databaseSizeBeforeUpdate = iLabelRepository.findAll().collectList().block().size();

        // Update the iLabel
        ILabel updatedILabel = iLabelRepository.findById(iLabel.getId()).block();
        updatedILabel.name(UPDATED_NAME).alias(UPDATED_ALIAS).description(UPDATED_DESCRIPTION).parentId(UPDATED_PARENT_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedILabel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedILabel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeUpdate);
        ILabel testILabel = iLabelList.get(iLabelList.size() - 1);
        assertThat(testILabel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testILabel.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testILabel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testILabel.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    void putNonExistingILabel() throws Exception {
        int databaseSizeBeforeUpdate = iLabelRepository.findAll().collectList().block().size();
        iLabel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, iLabel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iLabel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchILabel() throws Exception {
        int databaseSizeBeforeUpdate = iLabelRepository.findAll().collectList().block().size();
        iLabel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iLabel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamILabel() throws Exception {
        int databaseSizeBeforeUpdate = iLabelRepository.findAll().collectList().block().size();
        iLabel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iLabel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateILabelWithPatch() throws Exception {
        // Initialize the database
        iLabelRepository.save(iLabel).block();

        int databaseSizeBeforeUpdate = iLabelRepository.findAll().collectList().block().size();

        // Update the iLabel using partial update
        ILabel partialUpdatedILabel = new ILabel();
        partialUpdatedILabel.setId(iLabel.getId());

        partialUpdatedILabel.name(UPDATED_NAME).alias(UPDATED_ALIAS).parentId(UPDATED_PARENT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedILabel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedILabel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeUpdate);
        ILabel testILabel = iLabelList.get(iLabelList.size() - 1);
        assertThat(testILabel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testILabel.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testILabel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testILabel.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    void fullUpdateILabelWithPatch() throws Exception {
        // Initialize the database
        iLabelRepository.save(iLabel).block();

        int databaseSizeBeforeUpdate = iLabelRepository.findAll().collectList().block().size();

        // Update the iLabel using partial update
        ILabel partialUpdatedILabel = new ILabel();
        partialUpdatedILabel.setId(iLabel.getId());

        partialUpdatedILabel.name(UPDATED_NAME).alias(UPDATED_ALIAS).description(UPDATED_DESCRIPTION).parentId(UPDATED_PARENT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedILabel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedILabel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeUpdate);
        ILabel testILabel = iLabelList.get(iLabelList.size() - 1);
        assertThat(testILabel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testILabel.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testILabel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testILabel.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    void patchNonExistingILabel() throws Exception {
        int databaseSizeBeforeUpdate = iLabelRepository.findAll().collectList().block().size();
        iLabel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, iLabel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iLabel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchILabel() throws Exception {
        int databaseSizeBeforeUpdate = iLabelRepository.findAll().collectList().block().size();
        iLabel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iLabel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamILabel() throws Exception {
        int databaseSizeBeforeUpdate = iLabelRepository.findAll().collectList().block().size();
        iLabel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iLabel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ILabel in the database
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteILabel() {
        // Initialize the database
        iLabelRepository.save(iLabel).block();

        int databaseSizeBeforeDelete = iLabelRepository.findAll().collectList().block().size();

        // Delete the iLabel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, iLabel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ILabel> iLabelList = iLabelRepository.findAll().collectList().block();
        assertThat(iLabelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
