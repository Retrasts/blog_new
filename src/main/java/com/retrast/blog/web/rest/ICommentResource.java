package com.retrast.blog.web.rest;

import com.retrast.blog.domain.IComment;
import com.retrast.blog.repository.ICommentRepository;
import com.retrast.blog.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.retrast.blog.domain.IComment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ICommentResource {

    private final Logger log = LoggerFactory.getLogger(ICommentResource.class);

    private static final String ENTITY_NAME = "iComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ICommentRepository iCommentRepository;

    public ICommentResource(ICommentRepository iCommentRepository) {
        this.iCommentRepository = iCommentRepository;
    }

    /**
     * {@code POST  /i-comments} : Create a new iComment.
     *
     * @param iComment the iComment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iComment, or with status {@code 400 (Bad Request)} if the iComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-comments")
    public Mono<ResponseEntity<IComment>> createIComment(@RequestBody IComment iComment) throws URISyntaxException {
        log.debug("REST request to save IComment : {}", iComment);
        if (iComment.getId() != null) {
            throw new BadRequestAlertException("A new iComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return iCommentRepository
            .save(iComment)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/i-comments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /i-comments/:id} : Updates an existing iComment.
     *
     * @param id the id of the iComment to save.
     * @param iComment the iComment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iComment,
     * or with status {@code 400 (Bad Request)} if the iComment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iComment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-comments/{id}")
    public Mono<ResponseEntity<IComment>> updateIComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IComment iComment
    ) throws URISyntaxException {
        log.debug("REST request to update IComment : {}, {}", id, iComment);
        if (iComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iComment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iCommentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return iCommentRepository
                    .save(iComment)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /i-comments/:id} : Partial updates given fields of an existing iComment, field will ignore if it is null
     *
     * @param id the id of the iComment to save.
     * @param iComment the iComment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iComment,
     * or with status {@code 400 (Bad Request)} if the iComment is not valid,
     * or with status {@code 404 (Not Found)} if the iComment is not found,
     * or with status {@code 500 (Internal Server Error)} if the iComment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IComment>> partialUpdateIComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IComment iComment
    ) throws URISyntaxException {
        log.debug("REST request to partial update IComment partially : {}, {}", id, iComment);
        if (iComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iComment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iCommentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IComment> result = iCommentRepository
                    .findById(iComment.getId())
                    .map(existingIComment -> {
                        if (iComment.getCreateTime() != null) {
                            existingIComment.setCreateTime(iComment.getCreateTime());
                        }
                        if (iComment.getCreateUserId() != null) {
                            existingIComment.setCreateUserId(iComment.getCreateUserId());
                        }
                        if (iComment.getBlogId() != null) {
                            existingIComment.setBlogId(iComment.getBlogId());
                        }
                        if (iComment.getContent() != null) {
                            existingIComment.setContent(iComment.getContent());
                        }
                        if (iComment.getLikes() != null) {
                            existingIComment.setLikes(iComment.getLikes());
                        }
                        if (iComment.getParentId() != null) {
                            existingIComment.setParentId(iComment.getParentId());
                        }

                        return existingIComment;
                    })
                    .flatMap(iCommentRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /i-comments} : get all the iComments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iComments in body.
     */
    @GetMapping("/i-comments")
    public Mono<ResponseEntity<List<IComment>>> getAllIComments(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of IComments");
        return iCommentRepository
            .count()
            .zipWith(iCommentRepository.findAllBy(pageable).collectList())
            .map(countWithEntities -> {
                return ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2());
            });
    }

    /**
     * {@code GET  /i-comments/:id} : get the "id" iComment.
     *
     * @param id the id of the iComment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iComment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-comments/{id}")
    public Mono<ResponseEntity<IComment>> getIComment(@PathVariable Long id) {
        log.debug("REST request to get IComment : {}", id);
        Mono<IComment> iComment = iCommentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iComment);
    }

    /**
     * {@code DELETE  /i-comments/:id} : delete the "id" iComment.
     *
     * @param id the id of the iComment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-comments/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIComment(@PathVariable Long id) {
        log.debug("REST request to delete IComment : {}", id);
        return iCommentRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
