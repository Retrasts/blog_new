package com.retrast.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.retrast.blog.IntegrationTest;
import com.retrast.blog.domain.IRoleMenu;
import com.retrast.blog.repository.IRoleMenuRepository;
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
 * Integration tests for the {@link IRoleMenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IRoleMenuResourceIT {

    private static final String ENTITY_API_URL = "/api/i-role-menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IRoleMenuRepository iRoleMenuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IRoleMenu iRoleMenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IRoleMenu createEntity(EntityManager em) {
        IRoleMenu iRoleMenu = new IRoleMenu();
        return iRoleMenu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IRoleMenu createUpdatedEntity(EntityManager em) {
        IRoleMenu iRoleMenu = new IRoleMenu();
        return iRoleMenu;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IRoleMenu.class).block();
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
        iRoleMenu = createEntity(em);
    }

    @Test
    void createIRoleMenu() throws Exception {
        int databaseSizeBeforeCreate = iRoleMenuRepository.findAll().collectList().block().size();
        // Create the IRoleMenu
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRoleMenu))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeCreate + 1);
        IRoleMenu testIRoleMenu = iRoleMenuList.get(iRoleMenuList.size() - 1);
    }

    @Test
    void createIRoleMenuWithExistingId() throws Exception {
        // Create the IRoleMenu with an existing ID
        iRoleMenu.setId(1L);

        int databaseSizeBeforeCreate = iRoleMenuRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRoleMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIRoleMenusAsStream() {
        // Initialize the database
        iRoleMenuRepository.save(iRoleMenu).block();

        List<IRoleMenu> iRoleMenuList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(IRoleMenu.class)
            .getResponseBody()
            .filter(iRoleMenu::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(iRoleMenuList).isNotNull();
        assertThat(iRoleMenuList).hasSize(1);
        IRoleMenu testIRoleMenu = iRoleMenuList.get(0);
    }

    @Test
    void getAllIRoleMenus() {
        // Initialize the database
        iRoleMenuRepository.save(iRoleMenu).block();

        // Get all the iRoleMenuList
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
            .value(hasItem(iRoleMenu.getId().intValue()));
    }

    @Test
    void getIRoleMenu() {
        // Initialize the database
        iRoleMenuRepository.save(iRoleMenu).block();

        // Get the iRoleMenu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, iRoleMenu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(iRoleMenu.getId().intValue()));
    }

    @Test
    void getNonExistingIRoleMenu() {
        // Get the iRoleMenu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIRoleMenu() throws Exception {
        // Initialize the database
        iRoleMenuRepository.save(iRoleMenu).block();

        int databaseSizeBeforeUpdate = iRoleMenuRepository.findAll().collectList().block().size();

        // Update the iRoleMenu
        IRoleMenu updatedIRoleMenu = iRoleMenuRepository.findById(iRoleMenu.getId()).block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIRoleMenu.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIRoleMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeUpdate);
        IRoleMenu testIRoleMenu = iRoleMenuList.get(iRoleMenuList.size() - 1);
    }

    @Test
    void putNonExistingIRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = iRoleMenuRepository.findAll().collectList().block().size();
        iRoleMenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, iRoleMenu.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRoleMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = iRoleMenuRepository.findAll().collectList().block().size();
        iRoleMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRoleMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = iRoleMenuRepository.findAll().collectList().block().size();
        iRoleMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRoleMenu))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIRoleMenuWithPatch() throws Exception {
        // Initialize the database
        iRoleMenuRepository.save(iRoleMenu).block();

        int databaseSizeBeforeUpdate = iRoleMenuRepository.findAll().collectList().block().size();

        // Update the iRoleMenu using partial update
        IRoleMenu partialUpdatedIRoleMenu = new IRoleMenu();
        partialUpdatedIRoleMenu.setId(iRoleMenu.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIRoleMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIRoleMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeUpdate);
        IRoleMenu testIRoleMenu = iRoleMenuList.get(iRoleMenuList.size() - 1);
    }

    @Test
    void fullUpdateIRoleMenuWithPatch() throws Exception {
        // Initialize the database
        iRoleMenuRepository.save(iRoleMenu).block();

        int databaseSizeBeforeUpdate = iRoleMenuRepository.findAll().collectList().block().size();

        // Update the iRoleMenu using partial update
        IRoleMenu partialUpdatedIRoleMenu = new IRoleMenu();
        partialUpdatedIRoleMenu.setId(iRoleMenu.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIRoleMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIRoleMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeUpdate);
        IRoleMenu testIRoleMenu = iRoleMenuList.get(iRoleMenuList.size() - 1);
    }

    @Test
    void patchNonExistingIRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = iRoleMenuRepository.findAll().collectList().block().size();
        iRoleMenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, iRoleMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRoleMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = iRoleMenuRepository.findAll().collectList().block().size();
        iRoleMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRoleMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = iRoleMenuRepository.findAll().collectList().block().size();
        iRoleMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iRoleMenu))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IRoleMenu in the database
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIRoleMenu() {
        // Initialize the database
        iRoleMenuRepository.save(iRoleMenu).block();

        int databaseSizeBeforeDelete = iRoleMenuRepository.findAll().collectList().block().size();

        // Delete the iRoleMenu
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, iRoleMenu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IRoleMenu> iRoleMenuList = iRoleMenuRepository.findAll().collectList().block();
        assertThat(iRoleMenuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
