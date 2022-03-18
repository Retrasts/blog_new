package com.retrast.blog.web.rest;

import com.retrast.blog.domain.IClassify;
import com.retrast.blog.repository.IClassifyRepository;
import com.retrast.blog.service.IClassifyService;
import com.retrast.blog.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.retrast.blog.domain.IClassify}.
 */
@RestController
@RequestMapping("/api")
public class IClassifyResource {

    private final Logger log = LoggerFactory.getLogger(IClassifyResource.class);

    private static final String ENTITY_NAME = "iClassify";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IClassifyService iClassifyService;

    private final IClassifyRepository iClassifyRepository;

    public IClassifyResource(IClassifyService iClassifyService, IClassifyRepository iClassifyRepository) {
        this.iClassifyService = iClassifyService;
        this.iClassifyRepository = iClassifyRepository;
    }

    /**
     * {@code POST  /i-classifies} : Create a new iClassify.
     *
     * @param iClassify the iClassify to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iClassify, or with status {@code 400 (Bad Request)} if the iClassify has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-classifies")
    public Mono<ResponseEntity<IClassify>> createIClassify(@RequestBody IClassify iClassify) throws URISyntaxException {
        log.debug("REST request to save IClassify : {}", iClassify);
        if (iClassify.getId() != null) {
            throw new BadRequestAlertException("A new iClassify cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return iClassifyService
            .save(iClassify)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/i-classifies/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /i-classifies/:id} : Updates an existing iClassify.
     *
     * @param id the id of the iClassify to save.
     * @param iClassify the iClassify to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iClassify,
     * or with status {@code 400 (Bad Request)} if the iClassify is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iClassify couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-classifies/{id}")
    public Mono<ResponseEntity<IClassify>> updateIClassify(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IClassify iClassify
    ) throws URISyntaxException {
        log.debug("REST request to update IClassify : {}, {}", id, iClassify);
        if (iClassify.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iClassify.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iClassifyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return iClassifyService
                    .save(iClassify)
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
     * {@code PATCH  /i-classifies/:id} : Partial updates given fields of an existing iClassify, field will ignore if it is null
     *
     * @param id the id of the iClassify to save.
     * @param iClassify the iClassify to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iClassify,
     * or with status {@code 400 (Bad Request)} if the iClassify is not valid,
     * or with status {@code 404 (Not Found)} if the iClassify is not found,
     * or with status {@code 500 (Internal Server Error)} if the iClassify couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-classifies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IClassify>> partialUpdateIClassify(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IClassify iClassify
    ) throws URISyntaxException {
        log.debug("REST request to partial update IClassify partially : {}, {}", id, iClassify);
        if (iClassify.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iClassify.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iClassifyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IClassify> result = iClassifyService.partialUpdate(iClassify);

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
     * {@code GET  /i-classifies} : get all the iClassifies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iClassifies in body.
     */
    @GetMapping("/i-classifies")
    public Mono<List<IClassify>> getAllIClassifies() {
        log.debug("REST request to get all IClassifies");
        return iClassifyService.findAll().collectList();
    }

    /**
     * {@code GET  /i-classifies} : get all the iClassifies as a stream.
     * @return the {@link Flux} of iClassifies.
     */
    @GetMapping(value = "/i-classifies", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IClassify> getAllIClassifiesAsStream() {
        log.debug("REST request to get all IClassifies as a stream");
        return iClassifyService.findAll();
    }

    /**
     * {@code GET  /i-classifies/:id} : get the "id" iClassify.
     *
     * @param id the id of the iClassify to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iClassify, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-classifies/{id}")
    public Mono<ResponseEntity<IClassify>> getIClassify(@PathVariable Long id) {
        log.debug("REST request to get IClassify : {}", id);
        Mono<IClassify> iClassify = iClassifyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iClassify);
    }

    /**
     * {@code DELETE  /i-classifies/:id} : delete the "id" iClassify.
     *
     * @param id the id of the iClassify to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-classifies/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIClassify(@PathVariable Long id) {
        log.debug("REST request to delete IClassify : {}", id);
        return iClassifyService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
