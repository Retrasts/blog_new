package com.retrast.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.retrast.blog.IntegrationTest;
import com.retrast.blog.domain.IRole;
import com.retrast.blog.repository.IRoleRepository;
import com.retrast.blog.service.EntityManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link IRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IRoleResourceIT {

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_CREATE_USER_ID = 1L;
    private static final Long UPDATED_CREATE_USER_ID = 2L;

    private static final Long DEFAULT_UPDATE_USER_ID = 1L;
    private static final Long UPDATED_UPDATE_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/i-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IRole iRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IRole createEntity(EntityManager em) {
        IRole iRole = new IRole()
            .roleName(DEFAULT_ROLE_NAME)
            .remark(DEFAULT_REMARK)
            .createTime(DEFAULT_CREATE_TIME)
            .updateTime(DEFAULT_UPDATE_TIME)
            .createUserId(DEFAULT_CREATE_USER_ID)
            .updateUserId(DEFAULT_UPDATE_USER_ID);
        return iRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IRole createUpdatedEntity(EntityManager em) {
        IRole iRole = new IRole()
            .roleName(UPDATED_ROLE_NAME)
            .remark(UPDATED_REMARK)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .updateUserId(UPDATED_UPDATE_USER_ID);
        return iRole;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IRole.class).block();
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
        iRole = createEntity(em);
    }

    @Test
    void createIRole() throws Exception {
        int databaseSizeBeforeCreate = iRoleRepository.findAll().collectList().block().size();
        // Create the IRole
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRole))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeCreate + 1);
        IRole testIRole = iRoleList.get(iRoleList.size() - 1);
        assertThat(testIRole.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);
        assertThat(testIRole.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testIRole.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIRole.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testIRole.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testIRole.getUpdateUserId()).isEqualTo(DEFAULT_UPDATE_USER_ID);
    }

    @Test
    void createIRoleWithExistingId() throws Exception {
        // Create the IRole with an existing ID
        iRole.setId(1L);

        int databaseSizeBeforeCreate = iRoleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIRoles() {
        // Initialize the database
        iRoleRepository.save(iRole).block();

        // Get all the iRoleList
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
            .value(hasItem(iRole.getId().intValue()))
            .jsonPath("$.[*].roleName")
            .value(hasItem(DEFAULT_ROLE_NAME))
            .jsonPath("$.[*].remark")
            .value(hasItem(DEFAULT_REMARK))
            .jsonPath("$.[*].createTime")
            .value(hasItem(DEFAULT_CREATE_TIME.toString()))
            .jsonPath("$.[*].updateTime")
            .value(hasItem(DEFAULT_UPDATE_TIME.toString()))
            .jsonPath("$.[*].createUserId")
            .value(hasItem(DEFAULT_CREATE_USER_ID.intValue()))
            .jsonPath("$.[*].updateUserId")
            .value(hasItem(DEFAULT_UPDATE_USER_ID.intValue()));
    }

    @Test
    void getIRole() {
        // Initialize the database
        iRoleRepository.save(iRole).block();

        // Get the iRole
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, iRole.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(iRole.getId().intValue()))
            .jsonPath("$.roleName")
            .value(is(DEFAULT_ROLE_NAME))
            .jsonPath("$.remark")
            .value(is(DEFAULT_REMARK))
            .jsonPath("$.createTime")
            .value(is(DEFAULT_CREATE_TIME.toString()))
            .jsonPath("$.updateTime")
            .value(is(DEFAULT_UPDATE_TIME.toString()))
            .jsonPath("$.createUserId")
            .value(is(DEFAULT_CREATE_USER_ID.intValue()))
            .jsonPath("$.updateUserId")
            .value(is(DEFAULT_UPDATE_USER_ID.intValue()));
    }

    @Test
    void getNonExistingIRole() {
        // Get the iRole
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIRole() throws Exception {
        // Initialize the database
        iRoleRepository.save(iRole).block();

        int databaseSizeBeforeUpdate = iRoleRepository.findAll().collectList().block().size();

        // Update the iRole
        IRole updatedIRole = iRoleRepository.findById(iRole.getId()).block();
        updatedIRole
            .roleName(UPDATED_ROLE_NAME)
            .remark(UPDATED_REMARK)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .updateUserId(UPDATED_UPDATE_USER_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIRole.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeUpdate);
        IRole testIRole = iRoleList.get(iRoleList.size() - 1);
        assertThat(testIRole.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
        assertThat(testIRole.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testIRole.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIRole.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testIRole.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIRole.getUpdateUserId()).isEqualTo(UPDATED_UPDATE_USER_ID);
    }

    @Test
    void putNonExistingIRole() throws Exception {
        int databaseSizeBeforeUpdate = iRoleRepository.findAll().collectList().block().size();
        iRole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, iRole.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIRole() throws Exception {
        int databaseSizeBeforeUpdate = iRoleRepository.findAll().collectList().block().size();
        iRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIRole() throws Exception {
        int databaseSizeBeforeUpdate = iRoleRepository.findAll().collectList().block().size();
        iRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRole))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIRoleWithPatch() throws Exception {
        // Initialize the database
        iRoleRepository.save(iRole).block();

        int databaseSizeBeforeUpdate = iRoleRepository.findAll().collectList().block().size();

        // Update the iRole using partial update
        IRole partialUpdatedIRole = new IRole();
        partialUpdatedIRole.setId(iRole.getId());

        partialUpdatedIRole.createTime(UPDATED_CREATE_TIME).updateTime(UPDATED_UPDATE_TIME).updateUserId(UPDATED_UPDATE_USER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeUpdate);
        IRole testIRole = iRoleList.get(iRoleList.size() - 1);
        assertThat(testIRole.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);
        assertThat(testIRole.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testIRole.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIRole.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testIRole.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testIRole.getUpdateUserId()).isEqualTo(UPDATED_UPDATE_USER_ID);
    }

    @Test
    void fullUpdateIRoleWithPatch() throws Exception {
        // Initialize the database
        iRoleRepository.save(iRole).block();

        int databaseSizeBeforeUpdate = iRoleRepository.findAll().collectList().block().size();

        // Update the iRole using partial update
        IRole partialUpdatedIRole = new IRole();
        partialUpdatedIRole.setId(iRole.getId());

        partialUpdatedIRole
            .roleName(UPDATED_ROLE_NAME)
            .remark(UPDATED_REMARK)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .updateUserId(UPDATED_UPDATE_USER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeUpdate);
        IRole testIRole = iRoleList.get(iRoleList.size() - 1);
        assertThat(testIRole.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);
        assertThat(testIRole.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testIRole.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIRole.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testIRole.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIRole.getUpdateUserId()).isEqualTo(UPDATED_UPDATE_USER_ID);
    }

    @Test
    void patchNonExistingIRole() throws Exception {
        int databaseSizeBeforeUpdate = iRoleRepository.findAll().collectList().block().size();
        iRole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, iRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIRole() throws Exception {
        int databaseSizeBeforeUpdate = iRoleRepository.findAll().collectList().block().size();
        iRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRole))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIRole() throws Exception {
        int databaseSizeBeforeUpdate = iRoleRepository.findAll().collectList().block().size();
        iRole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRole))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IRole in the database
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIRole() {
        // Initialize the database
        iRoleRepository.save(iRole).block();

        int databaseSizeBeforeDelete = iRoleRepository.findAll().collectList().block().size();

        // Delete the iRole
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, iRole.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IRole> iRoleList = iRoleRepository.findAll().collectList().block();
        assertThat(iRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
