package com.retrast.blog.web.rest;

import com.retrast.blog.domain.IBlog;
import com.retrast.blog.repository.IBlogRepository;
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
 * REST controller for managing {@link com.retrast.blog.domain.IBlog}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IBlogResource {

    private final Logger log = LoggerFactory.getLogger(IBlogResource.class);

    private static final String ENTITY_NAME = "iBlog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IBlogRepository iBlogRepository;

    public IBlogResource(IBlogRepository iBlogRepository) {
        this.iBlogRepository = iBlogRepository;
    }

    /**
     * {@code POST  /i-blogs} : Create a new iBlog.
     *
     * @param iBlog the iBlog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iBlog, or with status {@code 400 (Bad Request)} if the iBlog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-blogs")
    public Mono<ResponseEntity<IBlog>> createIBlog(@RequestBody IBlog iBlog) throws URISyntaxException {
        log.debug("REST request to save IBlog : {}", iBlog);
        if (iBlog.getId() != null) {
            throw new BadRequestAlertException("A new iBlog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return iBlogRepository
            .save(iBlog)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/i-blogs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /i-blogs/:id} : Updates an existing iBlog.
     *
     * @param id the id of the iBlog to save.
     * @param iBlog the iBlog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iBlog,
     * or with status {@code 400 (Bad Request)} if the iBlog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iBlog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-blogs/{id}")
    public Mono<ResponseEntity<IBlog>> updateIBlog(@PathVariable(value = "id", required = false) final Long id, @RequestBody IBlog iBlog)
        throws URISyntaxException {
        log.debug("REST request to update IBlog : {}, {}", id, iBlog);
        if (iBlog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iBlog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iBlogRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return iBlogRepository
                    .save(iBlog)
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
     * {@code PATCH  /i-blogs/:id} : Partial updates given fields of an existing iBlog, field will ignore if it is null
     *
     * @param id the id of the iBlog to save.
     * @param iBlog the iBlog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iBlog,
     * or with status {@code 400 (Bad Request)} if the iBlog is not valid,
     * or with status {@code 404 (Not Found)} if the iBlog is not found,
     * or with status {@code 500 (Internal Server Error)} if the iBlog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-blogs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IBlog>> partialUpdateIBlog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IBlog iBlog
    ) throws URISyntaxException {
        log.debug("REST request to partial update IBlog partially : {}, {}", id, iBlog);
        if (iBlog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iBlog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iBlogRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IBlog> result = iBlogRepository
                    .findById(iBlog.getId())
                    .map(existingIBlog -> {
                        if (iBlog.getCreateUserId() != null) {
                            existingIBlog.setCreateUserId(iBlog.getCreateUserId());
                        }
                        if (iBlog.getTitle() != null) {
                            existingIBlog.setTitle(iBlog.getTitle());
                        }
                        if (iBlog.getLabel() != null) {
                            existingIBlog.setLabel(iBlog.getLabel());
                        }
                        if (iBlog.getClassify() != null) {
                            existingIBlog.setClassify(iBlog.getClassify());
                        }
                        if (iBlog.getContent() != null) {
                            existingIBlog.setContent(iBlog.getContent());
                        }
                        if (iBlog.getLikes() != null) {
                            existingIBlog.setLikes(iBlog.getLikes());
                        }
                        if (iBlog.getReplynumber() != null) {
                            existingIBlog.setReplynumber(iBlog.getReplynumber());
                        }
                        if (iBlog.getCreateTime() != null) {
                            existingIBlog.setCreateTime(iBlog.getCreateTime());
                        }
                        if (iBlog.getUpdateTime() != null) {
                            existingIBlog.setUpdateTime(iBlog.getUpdateTime());
                        }

                        return existingIBlog;
                    })
                    .flatMap(iBlogRepository::save);

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
     * {@code GET  /i-blogs} : get all the iBlogs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iBlogs in body.
     */
    @GetMapping("/i-blogs")
    public Mono<ResponseEntity<List<IBlog>>> getAllIBlogs(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of IBlogs");
        return iBlogRepository
            .count()
            .zipWith(iBlogRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /i-blogs/:id} : get the "id" iBlog.
     *
     * @param id the id of the iBlog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iBlog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-blogs/{id}")
    public Mono<ResponseEntity<IBlog>> getIBlog(@PathVariable Long id) {
        log.debug("REST request to get IBlog : {}", id);
        Mono<IBlog> iBlog = iBlogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iBlog);
    }

    /**
     * {@code DELETE  /i-blogs/:id} : delete the "id" iBlog.
     *
     * @param id the id of the iBlog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-blogs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIBlog(@PathVariable Long id) {
        log.debug("REST request to delete IBlog : {}", id);
        return iBlogRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
