package com.retrast.blog.web.rest;

import com.retrast.blog.domain.ILabel;
import com.retrast.blog.repository.ILabelRepository;
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
 * REST controller for managing {@link com.retrast.blog.domain.ILabel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ILabelResource {

    private final Logger log = LoggerFactory.getLogger(ILabelResource.class);

    private static final String ENTITY_NAME = "iLabel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ILabelRepository iLabelRepository;

    public ILabelResource(ILabelRepository iLabelRepository) {
        this.iLabelRepository = iLabelRepository;
    }

    /**
     * {@code POST  /i-labels} : Create a new iLabel.
     *
     * @param iLabel the iLabel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iLabel, or with status {@code 400 (Bad Request)} if the iLabel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-labels")
    public Mono<ResponseEntity<ILabel>> createILabel(@RequestBody ILabel iLabel) throws URISyntaxException {
        log.debug("REST request to save ILabel : {}", iLabel);
        if (iLabel.getId() != null) {
            throw new BadRequestAlertException("A new iLabel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return iLabelRepository
            .save(iLabel)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/i-labels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /i-labels/:id} : Updates an existing iLabel.
     *
     * @param id the id of the iLabel to save.
     * @param iLabel the iLabel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iLabel,
     * or with status {@code 400 (Bad Request)} if the iLabel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iLabel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-labels/{id}")
    public Mono<ResponseEntity<ILabel>> updateILabel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ILabel iLabel
    ) throws URISyntaxException {
        log.debug("REST request to update ILabel : {}, {}", id, iLabel);
        if (iLabel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iLabel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iLabelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return iLabelRepository
                    .save(iLabel)
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
     * {@code PATCH  /i-labels/:id} : Partial updates given fields of an existing iLabel, field will ignore if it is null
     *
     * @param id the id of the iLabel to save.
     * @param iLabel the iLabel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iLabel,
     * or with status {@code 400 (Bad Request)} if the iLabel is not valid,
     * or with status {@code 404 (Not Found)} if the iLabel is not found,
     * or with status {@code 500 (Internal Server Error)} if the iLabel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-labels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ILabel>> partialUpdateILabel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ILabel iLabel
    ) throws URISyntaxException {
        log.debug("REST request to partial update ILabel partially : {}, {}", id, iLabel);
        if (iLabel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iLabel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iLabelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ILabel> result = iLabelRepository
                    .findById(iLabel.getId())
                    .map(existingILabel -> {
                        if (iLabel.getName() != null) {
                            existingILabel.setName(iLabel.getName());
                        }
                        if (iLabel.getAlias() != null) {
                            existingILabel.setAlias(iLabel.getAlias());
                        }
                        if (iLabel.getDescription() != null) {
                            existingILabel.setDescription(iLabel.getDescription());
                        }
                        if (iLabel.getParentId() != null) {
                            existingILabel.setParentId(iLabel.getParentId());
                        }

                        return existingILabel;
                    })
                    .flatMap(iLabelRepository::save);

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
     * {@code GET  /i-labels} : get all the iLabels.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iLabels in body.
     */
    @GetMapping("/i-labels")
    public Mono<ResponseEntity<List<ILabel>>> getAllILabels(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of ILabels");
        return iLabelRepository
            .count()
            .zipWith(iLabelRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /i-labels/:id} : get the "id" iLabel.
     *
     * @param id the id of the iLabel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iLabel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-labels/{id}")
    public Mono<ResponseEntity<ILabel>> getILabel(@PathVariable Long id) {
        log.debug("REST request to get ILabel : {}", id);
        Mono<ILabel> iLabel = iLabelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iLabel);
    }

    /**
     * {@code DELETE  /i-labels/:id} : delete the "id" iLabel.
     *
     * @param id the id of the iLabel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-labels/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteILabel(@PathVariable Long id) {
        log.debug("REST request to delete ILabel : {}", id);
        return iLabelRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
