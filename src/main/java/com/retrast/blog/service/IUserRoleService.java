package com.retrast.blog.service;

import com.retrast.blog.domain.IUserRole;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link IUserRole}.
 */
public interface IUserRoleService {
    /**
     * Save a iUserRole.
     *
     * @param iUserRole the entity to save.
     * @return the persisted entity.
     */
    Mono<IUserRole> save(IUserRole iUserRole);

    /**
     * Partially updates a iUserRole.
     *
     * @param iUserRole the entity to update partially.
     * @return the persisted entity.
     */
    Mono<IUserRole> partialUpdate(IUserRole iUserRole);

    /**
     * Get all the iUserRoles.
     *
     * @return the list of entities.
     */
    Flux<IUserRole> findAll();
    /**
     * Get all the IUserRole where Role is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<IUserRole> findAllWhereRoleIsNull();

    /**
     * Returns the number of iUserRoles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" iUserRole.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<IUserRole> findOne(Long id);

    /**
     * Delete the "id" iUserRole.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
