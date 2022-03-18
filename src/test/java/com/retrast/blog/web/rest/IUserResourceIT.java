package com.retrast.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.retrast.blog.IntegrationTest;
import com.retrast.blog.domain.IUser;
import com.retrast.blog.repository.IUserRepository;
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
 * Integration tests for the {@link IUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IUserResourceIT {

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_NIKENAME = "AAAAAAAAAA";
    private static final String UPDATED_NIKENAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEX = 1;
    private static final Integer UPDATED_SEX = 2;

    private static final String DEFAULT_EMAILE = "AAAAAAAAAA";
    private static final String UPDATED_EMAILE = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_CREATE_USER_ID = 1L;
    private static final Long UPDATED_CREATE_USER_ID = 2L;

    private static final Long DEFAULT_UPDATE_USER_ID = 1L;
    private static final Long UPDATED_UPDATE_USER_ID = 2L;

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE = 1;
    private static final Integer UPDATED_PHONE = 2;

    private static final String ENTITY_API_URL = "/api/i-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IUser iUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IUser createEntity(EntityManager em) {
        IUser iUser = new IUser()
            .ip(DEFAULT_IP)
            .username(DEFAULT_USERNAME)
            .nikename(DEFAULT_NIKENAME)
            .password(DEFAULT_PASSWORD)
            .sex(DEFAULT_SEX)
            .emaile(DEFAULT_EMAILE)
            .avatar(DEFAULT_AVATAR)
            .createTime(DEFAULT_CREATE_TIME)
            .updateTime(DEFAULT_UPDATE_TIME)
            .createUserId(DEFAULT_CREATE_USER_ID)
            .updateUserId(DEFAULT_UPDATE_USER_ID)
            .birthday(DEFAULT_BIRTHDAY)
            .company(DEFAULT_COMPANY)
            .phone(DEFAULT_PHONE);
        return iUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IUser createUpdatedEntity(EntityManager em) {
        IUser iUser = new IUser()
            .ip(UPDATED_IP)
            .username(UPDATED_USERNAME)
            .nikename(UPDATED_NIKENAME)
            .password(UPDATED_PASSWORD)
            .sex(UPDATED_SEX)
            .emaile(UPDATED_EMAILE)
            .avatar(UPDATED_AVATAR)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .updateUserId(UPDATED_UPDATE_USER_ID)
            .birthday(UPDATED_BIRTHDAY)
            .company(UPDATED_COMPANY)
            .phone(UPDATED_PHONE);
        return iUser;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IUser.class).block();
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
        iUser = createEntity(em);
    }

    @Test
    void createIUser() throws Exception {
        int databaseSizeBeforeCreate = iUserRepository.findAll().collectList().block().size();
        // Create the IUser
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUser))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeCreate + 1);
        IUser testIUser = iUserList.get(iUserList.size() - 1);
        assertThat(testIUser.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testIUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testIUser.getNikename()).isEqualTo(DEFAULT_NIKENAME);
        assertThat(testIUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testIUser.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testIUser.getEmaile()).isEqualTo(DEFAULT_EMAILE);
        assertThat(testIUser.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testIUser.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIUser.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testIUser.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testIUser.getUpdateUserId()).isEqualTo(DEFAULT_UPDATE_USER_ID);
        assertThat(testIUser.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testIUser.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testIUser.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    void createIUserWithExistingId() throws Exception {
        // Create the IUser with an existing ID
        iUser.setId(1L);

        int databaseSizeBeforeCreate = iUserRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUser))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIUsers() {
        // Initialize the database
        iUserRepository.save(iUser).block();

        // Get all the iUserList
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
            .value(hasItem(iUser.getId().intValue()))
            .jsonPath("$.[*].ip")
            .value(hasItem(DEFAULT_IP))
            .jsonPath("$.[*].username")
            .value(hasItem(DEFAULT_USERNAME))
            .jsonPath("$.[*].nikename")
            .value(hasItem(DEFAULT_NIKENAME))
            .jsonPath("$.[*].password")
            .value(hasItem(DEFAULT_PASSWORD))
            .jsonPath("$.[*].sex")
            .value(hasItem(DEFAULT_SEX))
            .jsonPath("$.[*].emaile")
            .value(hasItem(DEFAULT_EMAILE))
            .jsonPath("$.[*].avatar")
            .value(hasItem(DEFAULT_AVATAR))
            .jsonPath("$.[*].createTime")
            .value(hasItem(DEFAULT_CREATE_TIME.toString()))
            .jsonPath("$.[*].updateTime")
            .value(hasItem(DEFAULT_UPDATE_TIME.toString()))
            .jsonPath("$.[*].createUserId")
            .value(hasItem(DEFAULT_CREATE_USER_ID.intValue()))
            .jsonPath("$.[*].updateUserId")
            .value(hasItem(DEFAULT_UPDATE_USER_ID.intValue()))
            .jsonPath("$.[*].birthday")
            .value(hasItem(DEFAULT_BIRTHDAY.toString()))
            .jsonPath("$.[*].company")
            .value(hasItem(DEFAULT_COMPANY))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE));
    }

    @Test
    void getIUser() {
        // Initialize the database
        iUserRepository.save(iUser).block();

        // Get the iUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, iUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(iUser.getId().intValue()))
            .jsonPath("$.ip")
            .value(is(DEFAULT_IP))
            .jsonPath("$.username")
            .value(is(DEFAULT_USERNAME))
            .jsonPath("$.nikename")
            .value(is(DEFAULT_NIKENAME))
            .jsonPath("$.password")
            .value(is(DEFAULT_PASSWORD))
            .jsonPath("$.sex")
            .value(is(DEFAULT_SEX))
            .jsonPath("$.emaile")
            .value(is(DEFAULT_EMAILE))
            .jsonPath("$.avatar")
            .value(is(DEFAULT_AVATAR))
            .jsonPath("$.createTime")
            .value(is(DEFAULT_CREATE_TIME.toString()))
            .jsonPath("$.updateTime")
            .value(is(DEFAULT_UPDATE_TIME.toString()))
            .jsonPath("$.createUserId")
            .value(is(DEFAULT_CREATE_USER_ID.intValue()))
            .jsonPath("$.updateUserId")
            .value(is(DEFAULT_UPDATE_USER_ID.intValue()))
            .jsonPath("$.birthday")
            .value(is(DEFAULT_BIRTHDAY.toString()))
            .jsonPath("$.company")
            .value(is(DEFAULT_COMPANY))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE));
    }

    @Test
    void getNonExistingIUser() {
        // Get the iUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIUser() throws Exception {
        // Initialize the database
        iUserRepository.save(iUser).block();

        int databaseSizeBeforeUpdate = iUserRepository.findAll().collectList().block().size();

        // Update the iUser
        IUser updatedIUser = iUserRepository.findById(iUser.getId()).block();
        updatedIUser
            .ip(UPDATED_IP)
            .username(UPDATED_USERNAME)
            .nikename(UPDATED_NIKENAME)
            .password(UPDATED_PASSWORD)
            .sex(UPDATED_SEX)
            .emaile(UPDATED_EMAILE)
            .avatar(UPDATED_AVATAR)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .updateUserId(UPDATED_UPDATE_USER_ID)
            .birthday(UPDATED_BIRTHDAY)
            .company(UPDATED_COMPANY)
            .phone(UPDATED_PHONE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIUser.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeUpdate);
        IUser testIUser = iUserList.get(iUserList.size() - 1);
        assertThat(testIUser.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testIUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIUser.getNikename()).isEqualTo(UPDATED_NIKENAME);
        assertThat(testIUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testIUser.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testIUser.getEmaile()).isEqualTo(UPDATED_EMAILE);
        assertThat(testIUser.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testIUser.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIUser.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testIUser.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIUser.getUpdateUserId()).isEqualTo(UPDATED_UPDATE_USER_ID);
        assertThat(testIUser.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testIUser.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testIUser.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    void putNonExistingIUser() throws Exception {
        int databaseSizeBeforeUpdate = iUserRepository.findAll().collectList().block().size();
        iUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, iUser.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUser))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIUser() throws Exception {
        int databaseSizeBeforeUpdate = iUserRepository.findAll().collectList().block().size();
        iUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUser))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIUser() throws Exception {
        int databaseSizeBeforeUpdate = iUserRepository.findAll().collectList().block().size();
        iUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUser))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIUserWithPatch() throws Exception {
        // Initialize the database
        iUserRepository.save(iUser).block();

        int databaseSizeBeforeUpdate = iUserRepository.findAll().collectList().block().size();

        // Update the iUser using partial update
        IUser partialUpdatedIUser = new IUser();
        partialUpdatedIUser.setId(iUser.getId());

        partialUpdatedIUser
            .username(UPDATED_USERNAME)
            .nikename(UPDATED_NIKENAME)
            .password(UPDATED_PASSWORD)
            .sex(UPDATED_SEX)
            .emaile(UPDATED_EMAILE)
            .avatar(UPDATED_AVATAR)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUserId(UPDATED_UPDATE_USER_ID)
            .birthday(UPDATED_BIRTHDAY)
            .company(UPDATED_COMPANY)
            .phone(UPDATED_PHONE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeUpdate);
        IUser testIUser = iUserList.get(iUserList.size() - 1);
        assertThat(testIUser.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testIUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIUser.getNikename()).isEqualTo(UPDATED_NIKENAME);
        assertThat(testIUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testIUser.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testIUser.getEmaile()).isEqualTo(UPDATED_EMAILE);
        assertThat(testIUser.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testIUser.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIUser.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testIUser.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testIUser.getUpdateUserId()).isEqualTo(UPDATED_UPDATE_USER_ID);
        assertThat(testIUser.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testIUser.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testIUser.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    void fullUpdateIUserWithPatch() throws Exception {
        // Initialize the database
        iUserRepository.save(iUser).block();

        int databaseSizeBeforeUpdate = iUserRepository.findAll().collectList().block().size();

        // Update the iUser using partial update
        IUser partialUpdatedIUser = new IUser();
        partialUpdatedIUser.setId(iUser.getId());

        partialUpdatedIUser
            .ip(UPDATED_IP)
            .username(UPDATED_USERNAME)
            .nikename(UPDATED_NIKENAME)
            .password(UPDATED_PASSWORD)
            .sex(UPDATED_SEX)
            .emaile(UPDATED_EMAILE)
            .avatar(UPDATED_AVATAR)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .updateUserId(UPDATED_UPDATE_USER_ID)
            .birthday(UPDATED_BIRTHDAY)
            .company(UPDATED_COMPANY)
            .phone(UPDATED_PHONE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeUpdate);
        IUser testIUser = iUserList.get(iUserList.size() - 1);
        assertThat(testIUser.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testIUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIUser.getNikename()).isEqualTo(UPDATED_NIKENAME);
        assertThat(testIUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testIUser.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testIUser.getEmaile()).isEqualTo(UPDATED_EMAILE);
        assertThat(testIUser.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testIUser.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIUser.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testIUser.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIUser.getUpdateUserId()).isEqualTo(UPDATED_UPDATE_USER_ID);
        assertThat(testIUser.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testIUser.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testIUser.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    void patchNonExistingIUser() throws Exception {
        int databaseSizeBeforeUpdate = iUserRepository.findAll().collectList().block().size();
        iUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, iUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUser))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIUser() throws Exception {
        int databaseSizeBeforeUpdate = iUserRepository.findAll().collectList().block().size();
        iUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUser))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIUser() throws Exception {
        int databaseSizeBeforeUpdate = iUserRepository.findAll().collectList().block().size();
        iUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iUser))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IUser in the database
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIUser() {
        // Initialize the database
        iUserRepository.save(iUser).block();

        int databaseSizeBeforeDelete = iUserRepository.findAll().collectList().block().size();

        // Delete the iUser
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, iUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IUser> iUserList = iUserRepository.findAll().collectList().block();
        assertThat(iUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
