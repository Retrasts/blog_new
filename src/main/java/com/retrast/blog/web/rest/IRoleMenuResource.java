package com.retrast.blog.web.rest;

import com.retrast.blog.domain.IRoleMenu;
import com.retrast.blog.repository.IRoleMenuRepository;
import com.retrast.blog.service.IRoleMenuService;
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
 * REST controller for managing {@link com.retrast.blog.domain.IRoleMenu}.
 */
@RestController
@RequestMapping("/api")
public class IRoleMenuResource {

    private final Logger log = LoggerFactory.getLogger(IRoleMenuResource.class);

    private static final String ENTITY_NAME = "iRoleMenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IRoleMenuService iRoleMenuService;

    private final IRoleMenuRepository iRoleMenuRepository;

    public IRoleMenuResource(IRoleMenuService iRoleMenuService, IRoleMenuRepository iRoleMenuRepository) {
        this.iRoleMenuService = iRoleMenuService;
        this.iRoleMenuRepository = iRoleMenuRepository;
    }

    /**
     * {@code POST  /i-role-menus} : Create a new iRoleMenu.
     *
     * @param iRoleMenu the iRoleMenu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iRoleMenu, or with status {@code 400 (Bad Request)} if the iRoleMenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-role-menus")
    public Mono<ResponseEntity<IRoleMenu>> createIRoleMenu(@RequestBody IRoleMenu iRoleMenu) throws URISyntaxException {
        log.debug("REST request to save IRoleMenu : {}", iRoleMenu);
        if (iRoleMenu.getId() != null) {
            throw new BadRequestAlertException("A new iRoleMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return iRoleMenuService
            .save(iRoleMenu)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/i-role-menus/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /i-role-menus/:id} : Updates an existing iRoleMenu.
     *
     * @param id the id of the iRoleMenu to save.
     * @param iRoleMenu the iRoleMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iRoleMenu,
     * or with status {@code 400 (Bad Request)} if the iRoleMenu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iRoleMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-role-menus/{id}")
    public Mono<ResponseEntity<IRoleMenu>> updateIRoleMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IRoleMenu iRoleMenu
    ) throws URISyntaxException {
        log.debug("REST request to update IRoleMenu : {}, {}", id, iRoleMenu);
        if (iRoleMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iRoleMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iRoleMenuRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return iRoleMenuService
                    .save(iRoleMenu)
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
     * {@code PATCH  /i-role-menus/:id} : Partial updates given fields of an existing iRoleMenu, field will ignore if it is null
     *
     * @param id the id of the iRoleMenu to save.
     * @param iRoleMenu the iRoleMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iRoleMenu,
     * or with status {@code 400 (Bad Request)} if the iRoleMenu is not valid,
     * or with status {@code 404 (Not Found)} if the iRoleMenu is not found,
     * or with status {@code 500 (Internal Server Error)} if the iRoleMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-role-menus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IRoleMenu>> partialUpdateIRoleMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IRoleMenu iRoleMenu
    ) throws URISyntaxException {
        log.debug("REST request to partial update IRoleMenu partially : {}, {}", id, iRoleMenu);
        if (iRoleMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iRoleMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iRoleMenuRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IRoleMenu> result = iRoleMenuService.partialUpdate(iRoleMenu);

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
     * {@code GET  /i-role-menus} : get all the iRoleMenus.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iRoleMenus in body.
     */
    @GetMapping("/i-role-menus")
    public Mono<List<IRoleMenu>> getAllIRoleMenus(@RequestParam(required = false) String filter) {
        if ("role-is-null".equals(filter)) {
            log.debug("REST request to get all IRoleMenus where role is null");
            return iRoleMenuService.findAllWhereRoleIsNull().collectList();
        }
        log.debug("REST request to get all IRoleMenus");
        return iRoleMenuService.findAll().collectList();
    }

    /**
     * {@code GET  /i-role-menus} : get all the iRoleMenus as a stream.
     * @return the {@link Flux} of iRoleMenus.
     */
    @GetMapping(value = "/i-role-menus", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IRoleMenu> getAllIRoleMenusAsStream() {
        log.debug("REST request to get all IRoleMenus as a stream");
        return iRoleMenuService.findAll();
    }

    /**
     * {@code GET  /i-role-menus/:id} : get the "id" iRoleMenu.
     *
     * @param id the id of the iRoleMenu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iRoleMenu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-role-menus/{id}")
    public Mono<ResponseEntity<IRoleMenu>> getIRoleMenu(@PathVariable Long id) {
        log.debug("REST request to get IRoleMenu : {}", id);
        Mono<IRoleMenu> iRoleMenu = iRoleMenuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iRoleMenu);
    }

    /**
     * {@code DELETE  /i-role-menus/:id} : delete the "id" iRoleMenu.
     *
     * @param id the id of the iRoleMenu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-role-menus/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIRoleMenu(@PathVariable Long id) {
        log.debug("REST request to delete IRoleMenu : {}", id);
        return iRoleMenuService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
