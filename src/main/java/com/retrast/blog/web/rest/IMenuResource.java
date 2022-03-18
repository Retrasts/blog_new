package com.retrast.blog.web.rest;

import com.retrast.blog.domain.IMenu;
import com.retrast.blog.repository.IMenuRepository;
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
 * REST controller for managing {@link com.retrast.blog.domain.IMenu}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IMenuResource {

    private final Logger log = LoggerFactory.getLogger(IMenuResource.class);

    private static final String ENTITY_NAME = "iMenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IMenuRepository iMenuRepository;

    public IMenuResource(IMenuRepository iMenuRepository) {
        this.iMenuRepository = iMenuRepository;
    }

    /**
     * {@code POST  /i-menus} : Create a new iMenu.
     *
     * @param iMenu the iMenu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iMenu, or with status {@code 400 (Bad Request)} if the iMenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-menus")
    public Mono<ResponseEntity<IMenu>> createIMenu(@RequestBody IMenu iMenu) throws URISyntaxException {
        log.debug("REST request to save IMenu : {}", iMenu);
        if (iMenu.getId() != null) {
            throw new BadRequestAlertException("A new iMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return iMenuRepository
            .save(iMenu)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/i-menus/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /i-menus/:id} : Updates an existing iMenu.
     *
     * @param id the id of the iMenu to save.
     * @param iMenu the iMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iMenu,
     * or with status {@code 400 (Bad Request)} if the iMenu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-menus/{id}")
    public Mono<ResponseEntity<IMenu>> updateIMenu(@PathVariable(value = "id", required = false) final Long id, @RequestBody IMenu iMenu)
        throws URISyntaxException {
        log.debug("REST request to update IMenu : {}, {}", id, iMenu);
        if (iMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iMenuRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return iMenuRepository
                    .save(iMenu)
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
     * {@code PATCH  /i-menus/:id} : Partial updates given fields of an existing iMenu, field will ignore if it is null
     *
     * @param id the id of the iMenu to save.
     * @param iMenu the iMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iMenu,
     * or with status {@code 400 (Bad Request)} if the iMenu is not valid,
     * or with status {@code 404 (Not Found)} if the iMenu is not found,
     * or with status {@code 500 (Internal Server Error)} if the iMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-menus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IMenu>> partialUpdateIMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IMenu iMenu
    ) throws URISyntaxException {
        log.debug("REST request to partial update IMenu partially : {}, {}", id, iMenu);
        if (iMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iMenuRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IMenu> result = iMenuRepository
                    .findById(iMenu.getId())
                    .map(existingIMenu -> {
                        if (iMenu.getUrl() != null) {
                            existingIMenu.setUrl(iMenu.getUrl());
                        }
                        if (iMenu.getMenuName() != null) {
                            existingIMenu.setMenuName(iMenu.getMenuName());
                        }
                        if (iMenu.getParentId() != null) {
                            existingIMenu.setParentId(iMenu.getParentId());
                        }
                        if (iMenu.getCreateTime() != null) {
                            existingIMenu.setCreateTime(iMenu.getCreateTime());
                        }
                        if (iMenu.getUpdateTime() != null) {
                            existingIMenu.setUpdateTime(iMenu.getUpdateTime());
                        }
                        if (iMenu.getCreateUserId() != null) {
                            existingIMenu.setCreateUserId(iMenu.getCreateUserId());
                        }
                        if (iMenu.getUpdateUserId() != null) {
                            existingIMenu.setUpdateUserId(iMenu.getUpdateUserId());
                        }

                        return existingIMenu;
                    })
                    .flatMap(iMenuRepository::save);

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
     * {@code GET  /i-menus} : get all the iMenus.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iMenus in body.
     */
    @GetMapping("/i-menus")
    public Mono<ResponseEntity<List<IMenu>>> getAllIMenus(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of IMenus");
        return iMenuRepository
            .count()
            .zipWith(iMenuRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /i-menus/:id} : get the "id" iMenu.
     *
     * @param id the id of the iMenu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iMenu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-menus/{id}")
    public Mono<ResponseEntity<IMenu>> getIMenu(@PathVariable Long id) {
        log.debug("REST request to get IMenu : {}", id);
        Mono<IMenu> iMenu = iMenuRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iMenu);
    }

    /**
     * {@code DELETE  /i-menus/:id} : delete the "id" iMenu.
     *
     * @param id the id of the iMenu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-menus/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIMenu(@PathVariable Long id) {
        log.debug("REST request to delete IMenu : {}", id);
        return iMenuRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
