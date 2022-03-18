package com.retrast.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.retrast.blog.IntegrationTest;
import com.retrast.blog.domain.IMenu;
import com.retrast.blog.repository.IMenuRepository;
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
 * Integration tests for the {@link IMenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IMenuResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MENU_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final LocalDate DEFAULT_CREATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_CREATE_USER_ID = 1L;
    private static final Long UPDATED_CREATE_USER_ID = 2L;

    private static final Long DEFAULT_UPDATE_USER_ID = 1L;
    private static final Long UPDATED_UPDATE_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/i-menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IMenuRepository iMenuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IMenu iMenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IMenu createEntity(EntityManager em) {
        IMenu iMenu = new IMenu()
            .url(DEFAULT_URL)
            .menuName(DEFAULT_MENU_NAME)
            .parentId(DEFAULT_PARENT_ID)
            .createTime(DEFAULT_CREATE_TIME)
            .updateTime(DEFAULT_UPDATE_TIME)
            .createUserId(DEFAULT_CREATE_USER_ID)
            .updateUserId(DEFAULT_UPDATE_USER_ID);
        return iMenu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IMenu createUpdatedEntity(EntityManager em) {
        IMenu iMenu = new IMenu()
            .url(UPDATED_URL)
            .menuName(UPDATED_MENU_NAME)
            .parentId(UPDATED_PARENT_ID)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .updateUserId(UPDATED_UPDATE_USER_ID);
        return iMenu;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IMenu.class).block();
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
        iMenu = createEntity(em);
    }

    @Test
    void createIMenu() throws Exception {
        int databaseSizeBeforeCreate = iMenuRepository.findAll().collectList().block().size();
        // Create the IMenu
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iMenu))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeCreate + 1);
        IMenu testIMenu = iMenuList.get(iMenuList.size() - 1);
        assertThat(testIMenu.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testIMenu.getMenuName()).isEqualTo(DEFAULT_MENU_NAME);
        assertThat(testIMenu.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testIMenu.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIMenu.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testIMenu.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testIMenu.getUpdateUserId()).isEqualTo(DEFAULT_UPDATE_USER_ID);
    }

    @Test
    void createIMenuWithExistingId() throws Exception {
        // Create the IMenu with an existing ID
        iMenu.setId(1L);

        int databaseSizeBeforeCreate = iMenuRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIMenus() {
        // Initialize the database
        iMenuRepository.save(iMenu).block();

        // Get all the iMenuList
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
            .value(hasItem(iMenu.getId().intValue()))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL))
            .jsonPath("$.[*].menuName")
            .value(hasItem(DEFAULT_MENU_NAME))
            .jsonPath("$.[*].parentId")
            .value(hasItem(DEFAULT_PARENT_ID.intValue()))
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
    void getIMenu() {
        // Initialize the database
        iMenuRepository.save(iMenu).block();

        // Get the iMenu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, iMenu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(iMenu.getId().intValue()))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL))
            .jsonPath("$.menuName")
            .value(is(DEFAULT_MENU_NAME))
            .jsonPath("$.parentId")
            .value(is(DEFAULT_PARENT_ID.intValue()))
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
    void getNonExistingIMenu() {
        // Get the iMenu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIMenu() throws Exception {
        // Initialize the database
        iMenuRepository.save(iMenu).block();

        int databaseSizeBeforeUpdate = iMenuRepository.findAll().collectList().block().size();

        // Update the iMenu
        IMenu updatedIMenu = iMenuRepository.findById(iMenu.getId()).block();
        updatedIMenu
            .url(UPDATED_URL)
            .menuName(UPDATED_MENU_NAME)
            .parentId(UPDATED_PARENT_ID)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .updateUserId(UPDATED_UPDATE_USER_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIMenu.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeUpdate);
        IMenu testIMenu = iMenuList.get(iMenuList.size() - 1);
        assertThat(testIMenu.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testIMenu.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testIMenu.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testIMenu.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIMenu.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testIMenu.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIMenu.getUpdateUserId()).isEqualTo(UPDATED_UPDATE_USER_ID);
    }

    @Test
    void putNonExistingIMenu() throws Exception {
        int databaseSizeBeforeUpdate = iMenuRepository.findAll().collectList().block().size();
        iMenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, iMenu.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIMenu() throws Exception {
        int databaseSizeBeforeUpdate = iMenuRepository.findAll().collectList().block().size();
        iMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIMenu() throws Exception {
        int databaseSizeBeforeUpdate = iMenuRepository.findAll().collectList().block().size();
        iMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iMenu))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIMenuWithPatch() throws Exception {
        // Initialize the database
        iMenuRepository.save(iMenu).block();

        int databaseSizeBeforeUpdate = iMenuRepository.findAll().collectList().block().size();

        // Update the iMenu using partial update
        IMenu partialUpdatedIMenu = new IMenu();
        partialUpdatedIMenu.setId(iMenu.getId());

        partialUpdatedIMenu.menuName(UPDATED_MENU_NAME).parentId(UPDATED_PARENT_ID).createUserId(UPDATED_CREATE_USER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeUpdate);
        IMenu testIMenu = iMenuList.get(iMenuList.size() - 1);
        assertThat(testIMenu.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testIMenu.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testIMenu.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testIMenu.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIMenu.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testIMenu.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIMenu.getUpdateUserId()).isEqualTo(DEFAULT_UPDATE_USER_ID);
    }

    @Test
    void fullUpdateIMenuWithPatch() throws Exception {
        // Initialize the database
        iMenuRepository.save(iMenu).block();

        int databaseSizeBeforeUpdate = iMenuRepository.findAll().collectList().block().size();

        // Update the iMenu using partial update
        IMenu partialUpdatedIMenu = new IMenu();
        partialUpdatedIMenu.setId(iMenu.getId());

        partialUpdatedIMenu
            .url(UPDATED_URL)
            .menuName(UPDATED_MENU_NAME)
            .parentId(UPDATED_PARENT_ID)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .updateUserId(UPDATED_UPDATE_USER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeUpdate);
        IMenu testIMenu = iMenuList.get(iMenuList.size() - 1);
        assertThat(testIMenu.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testIMenu.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testIMenu.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testIMenu.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIMenu.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testIMenu.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIMenu.getUpdateUserId()).isEqualTo(UPDATED_UPDATE_USER_ID);
    }

    @Test
    void patchNonExistingIMenu() throws Exception {
        int databaseSizeBeforeUpdate = iMenuRepository.findAll().collectList().block().size();
        iMenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, iMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIMenu() throws Exception {
        int databaseSizeBeforeUpdate = iMenuRepository.findAll().collectList().block().size();
        iMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iMenu))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIMenu() throws Exception {
        int databaseSizeBeforeUpdate = iMenuRepository.findAll().collectList().block().size();
        iMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iMenu))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IMenu in the database
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIMenu() {
        // Initialize the database
        iMenuRepository.save(iMenu).block();

        int databaseSizeBeforeDelete = iMenuRepository.findAll().collectList().block().size();

        // Delete the iMenu
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, iMenu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IMenu> iMenuList = iMenuRepository.findAll().collectList().block();
        assertThat(iMenuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
