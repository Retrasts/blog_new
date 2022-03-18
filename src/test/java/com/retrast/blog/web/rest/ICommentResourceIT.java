package com.retrast.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.retrast.blog.IntegrationTest;
import com.retrast.blog.domain.IComment;
import com.retrast.blog.repository.ICommentRepository;
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
 * Integration tests for the {@link ICommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ICommentResourceIT {

    private static final LocalDate DEFAULT_CREATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_CREATE_USER_ID = 1L;
    private static final Long UPDATED_CREATE_USER_ID = 2L;

    private static final Long DEFAULT_BLOG_ID = 1L;
    private static final Long UPDATED_BLOG_ID = 2L;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_LIKES = 1L;
    private static final Long UPDATED_LIKES = 2L;

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/i-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ICommentRepository iCommentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IComment iComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IComment createEntity(EntityManager em) {
        IComment iComment = new IComment()
            .createTime(DEFAULT_CREATE_TIME)
            .createUserId(DEFAULT_CREATE_USER_ID)
            .blogId(DEFAULT_BLOG_ID)
            .content(DEFAULT_CONTENT)
            .likes(DEFAULT_LIKES)
            .parentId(DEFAULT_PARENT_ID);
        return iComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IComment createUpdatedEntity(EntityManager em) {
        IComment iComment = new IComment()
            .createTime(UPDATED_CREATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .blogId(UPDATED_BLOG_ID)
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .parentId(UPDATED_PARENT_ID);
        return iComment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IComment.class).block();
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
        iComment = createEntity(em);
    }

    @Test
    void createIComment() throws Exception {
        int databaseSizeBeforeCreate = iCommentRepository.findAll().collectList().block().size();
        // Create the IComment
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iComment))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeCreate + 1);
        IComment testIComment = iCommentList.get(iCommentList.size() - 1);
        assertThat(testIComment.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIComment.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testIComment.getBlogId()).isEqualTo(DEFAULT_BLOG_ID);
        assertThat(testIComment.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testIComment.getLikes()).isEqualTo(DEFAULT_LIKES);
        assertThat(testIComment.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
    }

    @Test
    void createICommentWithExistingId() throws Exception {
        // Create the IComment with an existing ID
        iComment.setId(1L);

        int databaseSizeBeforeCreate = iCommentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iComment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIComments() {
        // Initialize the database
        iCommentRepository.save(iComment).block();

        // Get all the iCommentList
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
            .value(hasItem(iComment.getId().intValue()))
            .jsonPath("$.[*].createTime")
            .value(hasItem(DEFAULT_CREATE_TIME.toString()))
            .jsonPath("$.[*].createUserId")
            .value(hasItem(DEFAULT_CREATE_USER_ID.intValue()))
            .jsonPath("$.[*].blogId")
            .value(hasItem(DEFAULT_BLOG_ID.intValue()))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].likes")
            .value(hasItem(DEFAULT_LIKES.intValue()))
            .jsonPath("$.[*].parentId")
            .value(hasItem(DEFAULT_PARENT_ID.intValue()));
    }

    @Test
    void getIComment() {
        // Initialize the database
        iCommentRepository.save(iComment).block();

        // Get the iComment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, iComment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(iComment.getId().intValue()))
            .jsonPath("$.createTime")
            .value(is(DEFAULT_CREATE_TIME.toString()))
            .jsonPath("$.createUserId")
            .value(is(DEFAULT_CREATE_USER_ID.intValue()))
            .jsonPath("$.blogId")
            .value(is(DEFAULT_BLOG_ID.intValue()))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT))
            .jsonPath("$.likes")
            .value(is(DEFAULT_LIKES.intValue()))
            .jsonPath("$.parentId")
            .value(is(DEFAULT_PARENT_ID.intValue()));
    }

    @Test
    void getNonExistingIComment() {
        // Get the iComment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewIComment() throws Exception {
        // Initialize the database
        iCommentRepository.save(iComment).block();

        int databaseSizeBeforeUpdate = iCommentRepository.findAll().collectList().block().size();

        // Update the iComment
        IComment updatedIComment = iCommentRepository.findById(iComment.getId()).block();
        updatedIComment
            .createTime(UPDATED_CREATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .blogId(UPDATED_BLOG_ID)
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .parentId(UPDATED_PARENT_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedIComment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedIComment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeUpdate);
        IComment testIComment = iCommentList.get(iCommentList.size() - 1);
        assertThat(testIComment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIComment.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIComment.getBlogId()).isEqualTo(UPDATED_BLOG_ID);
        assertThat(testIComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testIComment.getLikes()).isEqualTo(UPDATED_LIKES);
        assertThat(testIComment.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    void putNonExistingIComment() throws Exception {
        int databaseSizeBeforeUpdate = iCommentRepository.findAll().collectList().block().size();
        iComment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, iComment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iComment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIComment() throws Exception {
        int databaseSizeBeforeUpdate = iCommentRepository.findAll().collectList().block().size();
        iComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iComment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIComment() throws Exception {
        int databaseSizeBeforeUpdate = iCommentRepository.findAll().collectList().block().size();
        iComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(iComment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateICommentWithPatch() throws Exception {
        // Initialize the database
        iCommentRepository.save(iComment).block();

        int databaseSizeBeforeUpdate = iCommentRepository.findAll().collectList().block().size();

        // Update the iComment using partial update
        IComment partialUpdatedIComment = new IComment();
        partialUpdatedIComment.setId(iComment.getId());

        partialUpdatedIComment.createUserId(UPDATED_CREATE_USER_ID).blogId(UPDATED_BLOG_ID).content(UPDATED_CONTENT).likes(UPDATED_LIKES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIComment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIComment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeUpdate);
        IComment testIComment = iCommentList.get(iCommentList.size() - 1);
        assertThat(testIComment.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIComment.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIComment.getBlogId()).isEqualTo(UPDATED_BLOG_ID);
        assertThat(testIComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testIComment.getLikes()).isEqualTo(UPDATED_LIKES);
        assertThat(testIComment.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
    }

    @Test
    void fullUpdateICommentWithPatch() throws Exception {
        // Initialize the database
        iCommentRepository.save(iComment).block();

        int databaseSizeBeforeUpdate = iCommentRepository.findAll().collectList().block().size();

        // Update the iComment using partial update
        IComment partialUpdatedIComment = new IComment();
        partialUpdatedIComment.setId(iComment.getId());

        partialUpdatedIComment
            .createTime(UPDATED_CREATE_TIME)
            .createUserId(UPDATED_CREATE_USER_ID)
            .blogId(UPDATED_BLOG_ID)
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .parentId(UPDATED_PARENT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIComment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIComment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeUpdate);
        IComment testIComment = iCommentList.get(iCommentList.size() - 1);
        assertThat(testIComment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIComment.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testIComment.getBlogId()).isEqualTo(UPDATED_BLOG_ID);
        assertThat(testIComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testIComment.getLikes()).isEqualTo(UPDATED_LIKES);
        assertThat(testIComment.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    void patchNonExistingIComment() throws Exception {
        int databaseSizeBeforeUpdate = iCommentRepository.findAll().collectList().block().size();
        iComment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, iComment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iComment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIComment() throws Exception {
        int databaseSizeBeforeUpdate = iCommentRepository.findAll().collectList().block().size();
        iComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iComment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIComment() throws Exception {
        int databaseSizeBeforeUpdate = iCommentRepository.findAll().collectList().block().size();
        iComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(iComment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IComment in the database
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIComment() {
        // Initialize the database
        iCommentRepository.save(iComment).block();

        int databaseSizeBeforeDelete = iCommentRepository.findAll().collectList().block().size();

        // Delete the iComment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, iComment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IComment> iCommentList = iCommentRepository.findAll().collectList().block();
        assertThat(iCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
