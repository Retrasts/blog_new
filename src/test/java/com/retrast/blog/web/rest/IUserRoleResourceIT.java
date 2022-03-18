package com.retrast.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.retrast.blog.IntegrationTest;
import com.retrast.blog.domain.IUserRole;
import com.retrast.blog.repository.IUserRoleRepository;
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
 * Integration tests for the {@link IUserRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IUserRoleResourceIT {

    private static final String ENTITY_API_URL = "/api/i-user-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IUserRoleRepository iUserRoleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IUserRole iUserRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IUserRole createEntity(EntityManager em) {
        IUserRole iUserRole = new IUserRole();
        return iUserRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IUserRole createUpdatedEntity(EntityManager em) {
        IUserRole iUserRole = new IUserRole();
        return iUserRole;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IUserRole.class).block();
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
        iUserRole = createEntity(em);
    }

    @Test
    void createIUserRole() throws Exception {
        int databaseSizeBeforeCreate = iUserRoleRepository.findAll().collectList().block().size();
        // Create the IUserRole
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUserRole))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeCreate + 1);
        IUserRole testIUserRole = iUserRoleList.get(iUserRoleList.size() - 1);
    }

    @Test
    void createIUserRoleWithExistingId() throws Exception {
        // Create the IUserRole with an existing ID
        iUserRole.setId(1L);

        int databaseSizeBeforeCreate = iUserRoleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUserRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIUserRolesAsStream() {
        // Initialize the database
        iUserRoleRepository.save(iUserRole).block();

        List<IUserRole> iUserRoleList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(IUserRole.class)
            .getResponseBody()
            .filter(iUserRole::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(iUserRoleList).isNotNull();
        assertThat(iUserRoleList).hasSize(1);
        IUserRole testIUserRole = iUserRoleList.get(0);
    }

    @Test
    void getAllIUserRoles() {
        // Initialize the database
        iUserRoleRepository.save(iUserRole).block();

        // Get all the iUserRoleList
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
            .value(hasItem(iUserRole.getId().intValue()));
    }

    @Test
    void getIUserRole() {
        // Initialize the database
        iUserRoleRepository.save(iUserRole).block();

        // Get the iUserRole
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, iUserRole.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(iUserRole.getId().intValue()));
    }

    @Test
    void getNonExistingIUserRole() {
        // Get the iUserRole
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIUserRole() throws Exception {
        // Initialize the database
        iUserRoleRepository.save(iUserRole).block();

        int databaseSizeBeforeUpdate = iUserRoleRepository.findAll().collectList().block().size();

        // Update the iUserRole
        IUserRole updatedIUserRole = iUserRoleRepository.findById(iUserRole.getId()).block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIUserRole.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIUserRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeUpdate);
        IUserRole testIUserRole = iUserRoleList.get(iUserRoleList.size() - 1);
    }

    @Test
    void putNonExistingIUserRole() throws Exception {
        int databaseSizeBeforeUpdate = iUserRoleRepository.findAll().collectList().block().size();
        iUserRole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, iUserRole.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUserRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIUserRole() throws Exception {
        int databaseSizeBeforeUpdate = iUserRoleRepository.findAll().collectList().block().size();
        iUserRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUserRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIUserRole() throws Exception {
        int databaseSizeBeforeUpdate = iUserRoleRepository.findAll().collectList().block().size();
        iUserRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUserRole))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIUserRoleWithPatch() throws Exception {
        // Initialize the database
        iUserRoleRepository.save(iUserRole).block();

        int databaseSizeBeforeUpdate = iUserRoleRepository.findAll().collectList().block().size();

        // Update the iUserRole using partial update
        IUserRole partialUpdatedIUserRole = new IUserRole();
        partialUpdatedIUserRole.setId(iUserRole.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIUserRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIUserRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeUpdate);
        IUserRole testIUserRole = iUserRoleList.get(iUserRoleList.size() - 1);
    }

    @Test
    void fullUpdateIUserRoleWithPatch() throws Exception {
        // Initialize the database
        iUserRoleRepository.save(iUserRole).block();

        int databaseSizeBeforeUpdate = iUserRoleRepository.findAll().collectList().block().size();

        // Update the iUserRole using partial update
        IUserRole partialUpdatedIUserRole = new IUserRole();
        partialUpdatedIUserRole.setId(iUserRole.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIUserRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIUserRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeUpdate);
        IUserRole testIUserRole = iUserRoleList.get(iUserRoleList.size() - 1);
    }

    @Test
    void patchNonExistingIUserRole() throws Exception {
        int databaseSizeBeforeUpdate = iUserRoleRepository.findAll().collectList().block().size();
        iUserRole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, iUserRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUserRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIUserRole() throws Exception {
        int databaseSizeBeforeUpdate = iUserRoleRepository.findAll().collectList().block().size();
        iUserRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUserRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIUserRole() throws Exception {
        int databaseSizeBeforeUpdate = iUserRoleRepository.findAll().collectList().block().size();
        iUserRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUserRole))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IUserRole in the database
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIUserRole() {
        // Initialize the database
        iUserRoleRepository.save(iUserRole).block();

        int databaseSizeBeforeDelete = iUserRoleRepository.findAll().collectList().block().size();

        // Delete the iUserRole
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, iUserRole.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IUserRole> iUserRoleList = iUserRoleRepository.findAll().collectList().block();
        assertThat(iUserRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
