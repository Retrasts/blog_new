package com.retrast.blog.web.rest;

import com.retrast.blog.domain.IUserRole;
import com.retrast.blog.repository.IUserRoleRepository;
import com.retrast.blog.service.IUserRoleService;
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
 * REST controller for managing {@link com.retrast.blog.domain.IUserRole}.
 */
@RestController
@RequestMapping("/api")
public class IUserRoleResource {

    private final Logger log = LoggerFactory.getLogger(IUserRoleResource.class);

    private static final String ENTITY_NAME = "iUserRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IUserRoleService iUserRoleService;

    private final IUserRoleRepository iUserRoleRepository;

    public IUserRoleResource(IUserRoleService iUserRoleService, IUserRoleRepository iUserRoleRepository) {
        this.iUserRoleService = iUserRoleService;
        this.iUserRoleRepository = iUserRoleRepository;
    }

    /**
     * {@code POST  /i-user-roles} : Create a new iUserRole.
     *
     * @param iUserRole the iUserRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iUserRole, or with status {@code 400 (Bad Request)} if the iUserRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-user-roles")
    public Mono<ResponseEntity<IUserRole>> createIUserRole(@RequestBody IUserRole iUserRole) throws URISyntaxException {
        log.debug("REST request to save IUserRole : {}", iUserRole);
        if (iUserRole.getId() != null) {
            throw new BadRequestAlertException("A new iUserRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return iUserRoleService
            .save(iUserRole)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/i-user-roles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /i-user-roles/:id} : Updates an existing iUserRole.
     *
     * @param id the id of the iUserRole to save.
     * @param iUserRole the iUserRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iUserRole,
     * or with status {@code 400 (Bad Request)} if the iUserRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iUserRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-user-roles/{id}")
    public Mono<ResponseEntity<IUserRole>> updateIUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IUserRole iUserRole
    ) throws URISyntaxException {
        log.debug("REST request to update IUserRole : {}, {}", id, iUserRole);
        if (iUserRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iUserRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iUserRoleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return iUserRoleService
                    .save(iUserRole)
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
     * {@code PATCH  /i-user-roles/:id} : Partial updates given fields of an existing iUserRole, field will ignore if it is null
     *
     * @param id the id of the iUserRole to save.
     * @param iUserRole the iUserRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iUserRole,
     * or with status {@code 400 (Bad Request)} if the iUserRole is not valid,
     * or with status {@code 404 (Not Found)} if the iUserRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the iUserRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-user-roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IUserRole>> partialUpdateIUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IUserRole iUserRole
    ) throws URISyntaxException {
        log.debug("REST request to partial update IUserRole partially : {}, {}", id, iUserRole);
        if (iUserRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iUserRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iUserRoleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IUserRole> result = iUserRoleService.partialUpdate(iUserRole);

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
     * {@code GET  /i-user-roles} : get all the iUserRoles.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iUserRoles in body.
     */
    @GetMapping("/i-user-roles")
    public Mono<List<IUserRole>> getAllIUserRoles(@RequestParam(required = false) String filter) {
        if ("role-is-null".equals(filter)) {
            log.debug("REST request to get all IUserRoles where role is null");
            return iUserRoleService.findAllWhereRoleIsNull().collectList();
        }
        log.debug("REST request to get all IUserRoles");
        return iUserRoleService.findAll().collectList();
    }

    /**
     * {@code GET  /i-user-roles} : get all the iUserRoles as a stream.
     * @return the {@link Flux} of iUserRoles.
     */
    @GetMapping(value = "/i-user-roles", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IUserRole> getAllIUserRolesAsStream() {
        log.debug("REST request to get all IUserRoles as a stream");
        return iUserRoleService.findAll();
    }

    /**
     * {@code GET  /i-user-roles/:id} : get the "id" iUserRole.
     *
     * @param id the id of the iUserRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iUserRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-user-roles/{id}")
    public Mono<ResponseEntity<IUserRole>> getIUserRole(@PathVariable Long id) {
        log.debug("REST request to get IUserRole : {}", id);
        Mono<IUserRole> iUserRole = iUserRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iUserRole);
    }

    /**
     * {@code DELETE  /i-user-roles/:id} : delete the "id" iUserRole.
     *
     * @param id the id of the iUserRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-user-roles/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIUserRole(@PathVariable Long id) {
        log.debug("REST request to delete IUserRole : {}", id);
        return iUserRoleService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
