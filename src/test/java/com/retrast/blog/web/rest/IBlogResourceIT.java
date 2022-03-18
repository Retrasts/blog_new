package com.retrast.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.retrast.blog.IntegrationTest;
import com.retrast.blog.domain.IBlog;
import com.retrast.blog.repository.IBlogRepository;
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
 * Integration tests for the {@link IBlogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class IBlogResourceIT {

    private static final Long DEFAULT_CREATE_USER_ID = 1L;
    private static final Long UPDATED_CREATE_USER_ID = 2L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_LABEL = 1L;
    private static final Long UPDATED_LABEL = 2L;

    private static final Long DEFAULT_CLASSIFY = 1L;
    private static final Long UPDATED_CLASSIFY = 2L;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_LIKES = 1L;
    private static final Long UPDATED_LIKES = 2L;

    private static final Long DEFAULT_REPLYNUMBER = 1L;
    private static final Long UPDATED_REPLYNUMBER = 2L;

    private static final LocalDate DEFAULT_CREATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/i-blogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IBlogRepository iBlogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IBlog iBlog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IBlog createEntity(EntityManager em) {
        IBlog iBlog = new IBlog()
            .createUserId(DEFAULT_CREATE_USER_ID)
            .title(DEFAULT_TITLE)
            .label(DEFAULT_LABEL)
            .classify(DEFAULT_CLASSIFY)
            .content(DEFAULT_CONTENT)
            .likes(DEFAULT_LIKES)
            .replynumber(DEFAULT_REPLYNUMBER)
            .createTime(DEFAULT_CREATE_TIME)
            .updateTime(DEFAULT_UPDATE_TIME);
        return iBlog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IBlog createUpdatedEntity(EntityManager em) {
        IBlog iBlog = new IBlog()
            .createUserId(UPDATED_CREATE_USER_ID)
            .title(UPDATED_TITLE)
            .label(UPDATED_LABEL)
            .classify(UPDATED_CLASSIFY)
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .replynumber(UPDATED_REPLYNUMBER)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME);
        return iBlog;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IBlog.class).block();
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
        iBlog = createEntity(em);
    }

    @Test
    void createIBlog() throws Exception {
        int databaseSizeBeforeCreate = iBlogRepository.findAll().collectList().block().size();
        // Create the IBlog
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iBlog))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeCreate + 1);
        IBlog testIBlog = iBlogList.get(iBlogList.size() - 1);
        assertThat(testIBlog.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testIBlog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testIBlog.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testIBlog.getClassify()).isEqualTo(DEFAULT_CLASSIFY);
        assertThat(testIBlog.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testIBlog.getLikes()).isEqualTo(DEFAULT_LIKES);
        assertThat(testIBlog.getReplynumber()).isEqualTo(DEFAULT_REPLYNUMBER);
        assertThat(testIBlog.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIBlog.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    void createIBlogWithExistingId() throws Exception {
        // Create the IBlog with an existing ID
        iBlog.setId(1L);

        int databaseSizeBeforeCreate = iBlogRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iBlog))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIBlogs() {
        // Initialize the database
        iBlogRepository.save(iBlog).block();

        // Get all the iBlogList
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
            .value(hasItem(iBlog.getId().intValue()))
            .jsonPath("$.[*].createUserId")
            .value(hasItem(DEFAULT_CREATE_USER_ID.intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].label")
            .value(hasItem(DEFAULT_LABEL.intValue()))
            .jsonPath("$.[*].classify")
            .value(hasItem(DEFAULT_CLASSIFY.intValue()))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].likes")
            .value(hasItem(DEFAULT_LIKES.intValue()))
            .jsonPath("$.[*].replynumber")
            .value(hasItem(DEFAULT_REPLYNUMBER.intValue()))
            .jsonPath("$.[*].createTime")
            .value(hasItem(DEFAULT_CREATE_TIME.toString()))
            .jsonPath("$.[*].updateTime")
            .value(hasItem(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    void getIBlog() {
        // Initialize the database
        iBlogRepository.save(iBlog).block();

        // Get the iBlog
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, iBlog.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(iBlog.getId().intValue()))
            .jsonPath("$.createUserId")
            .value(is(DEFAULT_CREATE_USER_ID.intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.label")
            .value(is(DEFAULT_LABEL.intValue()))
            .jsonPath("$.classify")
            .value(is(DEFAULT_CLASSIFY.intValue()))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT))
            .jsonPath("$.likes")
            .value(is(DEFAULT_LIKES.intValue()))
            .jsonPath("$.replynumber")
            .value(is(DEFAULT_REPLYNUMBER.intValue()))
            .jsonPath("$.createTime")
            .value(is(DEFAULT_CREATE_TIME.toString()))
            .jsonPath("$.updateTime")
            .value(is(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    void getNonExistingIBlog() {
        // Get the iBlog
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIBlog() throws Exception {
        // Initialize the database
        iBlogRepository.save(iBlog).block();

        int databaseSizeBeforeUpdate = iBlogRepository.findAll().collectList().block().size();

        // Update the iBlog
        IBlog updatedIBlog = iBlogRepository.findById(iBlog.getId()).block();
        updatedIBlog
            .createUserId(UPDATED_CREATE_USER_ID)
            .title(UPDATED_TITLE)
            .label(UPDATED_LABEL)
            .classify(UPDATED_CLASSIFY)
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .replynumber(UPDATED_REPLYNUMBER)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIBlog.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIBlog))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeUpdate);
        IBlog testIBlog = iBlogList.get(iBlogList.size() - 1);
        assertThat(testIBlog.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIBlog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIBlog.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testIBlog.getClassify()).isEqualTo(UPDATED_CLASSIFY);
        assertThat(testIBlog.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testIBlog.getLikes()).isEqualTo(UPDATED_LIKES);
        assertThat(testIBlog.getReplynumber()).isEqualTo(UPDATED_REPLYNUMBER);
        assertThat(testIBlog.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIBlog.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    void putNonExistingIBlog() throws Exception {
        int databaseSizeBeforeUpdate = iBlogRepository.findAll().collectList().block().size();
        iBlog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, iBlog.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iBlog))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIBlog() throws Exception {
        int databaseSizeBeforeUpdate = iBlogRepository.findAll().collectList().block().size();
        iBlog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iBlog))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIBlog() throws Exception {
        int databaseSizeBeforeUpdate = iBlogRepository.findAll().collectList().block().size();
        iBlog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iBlog))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIBlogWithPatch() throws Exception {
        // Initialize the database
        iBlogRepository.save(iBlog).block();

        int databaseSizeBeforeUpdate = iBlogRepository.findAll().collectList().block().size();

        // Update the iBlog using partial update
        IBlog partialUpdatedIBlog = new IBlog();
        partialUpdatedIBlog.setId(iBlog.getId());

        partialUpdatedIBlog.createUserId(UPDATED_CREATE_USER_ID).label(UPDATED_LABEL).content(UPDATED_CONTENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIBlog.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIBlog))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeUpdate);
        IBlog testIBlog = iBlogList.get(iBlogList.size() - 1);
        assertThat(testIBlog.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIBlog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testIBlog.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testIBlog.getClassify()).isEqualTo(DEFAULT_CLASSIFY);
        assertThat(testIBlog.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testIBlog.getLikes()).isEqualTo(DEFAULT_LIKES);
        assertThat(testIBlog.getReplynumber()).isEqualTo(DEFAULT_REPLYNUMBER);
        assertThat(testIBlog.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIBlog.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    void fullUpdateIBlogWithPatch() throws Exception {
        // Initialize the database
        iBlogRepository.save(iBlog).block();

        int databaseSizeBeforeUpdate = iBlogRepository.findAll().collectList().block().size();

        // Update the iBlog using partial update
        IBlog partialUpdatedIBlog = new IBlog();
        partialUpdatedIBlog.setId(iBlog.getId());

        partialUpdatedIBlog
            .createUserId(UPDATED_CREATE_USER_ID)
            .title(UPDATED_TITLE)
            .label(UPDATED_LABEL)
            .classify(UPDATED_CLASSIFY)
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .replynumber(UPDATED_REPLYNUMBER)
            .createTime(UPDATED_CREATE_TIME)
            .updateTime(UPDATED_UPDATE_TIME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIBlog.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIBlog))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeUpdate);
        IBlog testIBlog = iBlogList.get(iBlogList.size() - 1);
        assertThat(testIBlog.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIBlog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIBlog.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testIBlog.getClassify()).isEqualTo(UPDATED_CLASSIFY);
        assertThat(testIBlog.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testIBlog.getLikes()).isEqualTo(UPDATED_LIKES);
        assertThat(testIBlog.getReplynumber()).isEqualTo(UPDATED_REPLYNUMBER);
        assertThat(testIBlog.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIBlog.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    void patchNonExistingIBlog() throws Exception {
        int databaseSizeBeforeUpdate = iBlogRepository.findAll().collectList().block().size();
        iBlog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, iBlog.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iBlog))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIBlog() throws Exception {
        int databaseSizeBeforeUpdate = iBlogRepository.findAll().collectList().block().size();
        iBlog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iBlog))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIBlog() throws Exception {
        int databaseSizeBeforeUpdate = iBlogRepository.findAll().collectList().block().size();
        iBlog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iBlog))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IBlog in the database
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIBlog() {
        // Initialize the database
        iBlogRepository.save(iBlog).block();

        int databaseSizeBeforeDelete = iBlogRepository.findAll().collectList().block().size();

        // Delete the iBlog
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, iBlog.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IBlog> iBlogList = iBlogRepository.findAll().collectList().block();
        assertThat(iBlogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
