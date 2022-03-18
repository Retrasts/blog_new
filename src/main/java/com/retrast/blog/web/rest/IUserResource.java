package com.retrast.blog.web.rest;

import com.retrast.blog.domain.IUser;
import com.retrast.blog.repository.IUserRepository;
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
 * REST controller for managing {@link com.retrast.blog.domain.IUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IUserResource {

    private final Logger log = LoggerFactory.getLogger(IUserResource.class);

    private static final String ENTITY_NAME = "iUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IUserRepository iUserRepository;

    public IUserResource(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    /**
     * {@code POST  /i-users} : Create a new iUser.
     *
     * @param iUser the iUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iUser, or with status {@code 400 (Bad Request)} if the iUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/i-users")
    public Mono<ResponseEntity<IUser>> createIUser(@RequestBody IUser iUser) throws URISyntaxException {
        log.debug("REST request to save IUser : {}", iUser);
        if (iUser.getId() != null) {
            throw new BadRequestAlertException("A new iUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return iUserRepository
            .save(iUser)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/i-users/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /i-users/:id} : Updates an existing iUser.
     *
     * @param id the id of the iUser to save.
     * @param iUser the iUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iUser,
     * or with status {@code 400 (Bad Request)} if the iUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/i-users/{id}")
    public Mono<ResponseEntity<IUser>> updateIUser(@PathVariable(value = "id", required = false) final Long id, @RequestBody IUser iUser)
        throws URISyntaxException {
        log.debug("REST request to update IUser : {}, {}", id, iUser);
        if (iUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return iUserRepository
                    .save(iUser)
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
     * {@code PATCH  /i-users/:id} : Partial updates given fields of an existing iUser, field will ignore if it is null
     *
     * @param id the id of the iUser to save.
     * @param iUser the iUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iUser,
     * or with status {@code 400 (Bad Request)} if the iUser is not valid,
     * or with status {@code 404 (Not Found)} if the iUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the iUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/i-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IUser>> partialUpdateIUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IUser iUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update IUser partially : {}, {}", id, iUser);
        if (iUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return iUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IUser> result = iUserRepository
                    .findById(iUser.getId())
                    .map(existingIUser -> {
                        if (iUser.getIp() != null) {
                            existingIUser.setIp(iUser.getIp());
                        }
                        if (iUser.getUsername() != null) {
                            existingIUser.setUsername(iUser.getUsername());
                        }
                        if (iUser.getNikename() != null) {
                            existingIUser.setNikename(iUser.getNikename());
                        }
                        if (iUser.getPassword() != null) {
                            existingIUser.setPassword(iUser.getPassword());
                        }
                        if (iUser.getSex() != null) {
                            existingIUser.setSex(iUser.getSex());
                        }
                        if (iUser.getEmaile() != null) {
                            existingIUser.setEmaile(iUser.getEmaile());
                        }
                        if (iUser.getAvatar() != null) {
                            existingIUser.setAvatar(iUser.getAvatar());
                        }
                        if (iUser.getCreateTime() != null) {
                            existingIUser.setCreateTime(iUser.getCreateTime());
                        }
                        if (iUser.getUpdateTime() != null) {
                            existingIUser.setUpdateTime(iUser.getUpdateTime());
                        }
                        if (iUser.getCreateUserId() != null) {
                            existingIUser.setCreateUserId(iUser.getCreateUserId());
                        }
                        if (iUser.getUpdateUserId() != null) {
                            existingIUser.setUpdateUserId(iUser.getUpdateUserId());
                        }
                        if (iUser.getBirthday() != null) {
                            existingIUser.setBirthday(iUser.getBirthday());
                        }
                        if (iUser.getCompany() != null) {
                            existingIUser.setCompany(iUser.getCompany());
                        }
                        if (iUser.getPhone() != null) {
                            existingIUser.setPhone(iUser.getPhone());
                        }

                        return existingIUser;
                    })
                    .flatMap(iUserRepository::save);

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
     * {@code GET  /i-users} : get all the iUsers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iUsers in body.
     */
    @GetMapping("/i-users")
    public Mono<ResponseEntity<List<IUser>>> getAllIUsers(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of IUsers");
        return iUserRepository
            .count()
            .zipWith(iUserRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /i-users/:id} : get the "id" iUser.
     *
     * @param id the id of the iUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/i-users/{id}")
    public Mono<ResponseEntity<IUser>> getIUser(@PathVariable Long id) {
        log.debug("REST request to get IUser : {}", id);
        Mono<IUser> iUser = iUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iUser);
    }

    /**
     * {@code DELETE  /i-users/:id} : delete the "id" iUser.
     *
     * @param id the id of the iUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/i-users/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIUser(@PathVariable Long id) {
        log.debug("REST request to delete IUser : {}", id);
        return iUserRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
