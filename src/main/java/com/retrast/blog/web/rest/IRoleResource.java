package com.retrast.blog.web.rest;

import com.retrast.blog.domain.IRole;
import com.retrast.blog.repository.IRoleRepository;
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
 * REST controller for managing {@link com.retrast.blog.domain.IRole}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IRoleResource {

    private final Logger log = LoggerFactory.getLogger(IRoleResource.class);

    private static final String ENTITY_NAME = "iRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IRoleRepository iRoleRepository;

    public IRoleResource(IRoleRepository iRoleRepository) {
        this.iRoleRepository = iRoleRepository;
    }

    /**
     * {@code POST  /i-roles} : Create a new iRole.
     *
     * @param iRole the iRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iRole, or with status {@code 400 (Bad Request)} if the iRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-roles")
    public Mono<ResponseEntity<IRole>> createIRole(@RequestBody IRole iRole) throws URISyntaxException {
        log.debug("REST request to save IRole : {}", iRole);
        if (iRole.getId() != null) {
            throw new BadRequestAlertException("A new iRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return iRoleRepository
            .save(iRole)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/i-roles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /i-roles/:id} : Updates an existing iRole.
     *
     * @param id the id of the iRole to save.
     * @param iRole the iRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iRole,
     * or with status {@code 400 (Bad Request)} if the iRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-roles/{id}")
    public Mono<ResponseEntity<IRole>> updateIRole(@PathVariable(value = "id", required = false) final Long id, @RequestBody IRole iRole)
        throws URISyntaxException {
        log.debug("REST request to update IRole : {}, {}", id, iRole);
        if (iRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iRoleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return iRoleRepository
                    .save(iRole)
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
     * {@code PATCH  /i-roles/:id} : Partial updates given fields of an existing iRole, field will ignore if it is null
     *
     * @param id the id of the iRole to save.
     * @param iRole the iRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iRole,
     * or with status {@code 400 (Bad Request)} if the iRole is not valid,
     * or with status {@code 404 (Not Found)} if the iRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the iRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IRole>> partialUpdateIRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IRole iRole
    ) throws URISyntaxException {
        log.debug("REST request to partial update IRole partially : {}, {}", id, iRole);
        if (iRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iRoleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IRole> result = iRoleRepository
                    .findById(iRole.getId())
                    .map(existingIRole -> {
                        if (iRole.getRoleName() != null) {
                            existingIRole.setRoleName(iRole.getRoleName());
                        }
                        if (iRole.getRemark() != null) {
                            existingIRole.setRemark(iRole.getRemark());
                        }
                        if (iRole.getCreateTime() != null) {
                            existingIRole.setCreateTime(iRole.getCreateTime());
                        }
                        if (iRole.getUpdateTime() != null) {
                            existingIRole.setUpdateTime(iRole.getUpdateTime());
                        }
                        if (iRole.getCreateUserId() != null) {
                            existingIRole.setCreateUserId(iRole.getCreateUserId());
                        }
                        if (iRole.getUpdateUserId() != null) {
                            existingIRole.setUpdateUserId(iRole.getUpdateUserId());
                        }

                        return existingIRole;
                    })
                    .flatMap(iRoleRepository::save);

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
     * {@code GET  /i-roles} : get all the iRoles.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iRoles in body.
     */
    @GetMapping("/i-roles")
    public Mono<ResponseEntity<List<IRole>>> getAllIRoles(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of IRoles");
        return iRoleRepository
            .count()
            .zipWith(iRoleRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /i-roles/:id} : get the "id" iRole.
     *
     * @param id the id of the iRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-roles/{id}")
    public Mono<ResponseEntity<IRole>> getIRole(@PathVariable Long id) {
        log.debug("REST request to get IRole : {}", id);
        Mono<IRole> iRole = iRoleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iRole);
    }

    /**
     * {@code DELETE  /i-roles/:id} : delete the "id" iRole.
     *
     * @param id the id of the iRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-roles/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIRole(@PathVariable Long id) {
        log.debug("REST request to delete IRole : {}", id);
        return iRoleRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
