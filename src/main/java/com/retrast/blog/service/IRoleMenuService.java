package com.retrast.blog.service;

import com.retrast.blog.domain.IRoleMenu;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link IRoleMenu}.
 */
public interface IRoleMenuService {
    /**
     * Save a iRoleMenu.
     *
     * @param iRoleMenu the entity to save.
     * @return the persisted entity.
     */
    Mono<IRoleMenu> save(IRoleMenu iRoleMenu);

    /**
     * Partially updates a iRoleMenu.
     *
     * @param iRoleMenu the entity to update partially.
     * @return the persisted entity.
     */
    Mono<IRoleMenu> partialUpdate(IRoleMenu iRoleMenu);

    /**
     * Get all the iRoleMenus.
     *
     * @return the list of entities.
     */
    Flux<IRoleMenu> findAll();
    /**
     * Get all the IRoleMenu where Role is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<IRoleMenu> findAllWhereRoleIsNull();

    /**
     * Returns the number of iRoleMenus available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" iRoleMenu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<IRoleMenu> findOne(Long id);

    /**
     * Delete the "id" iRoleMenu.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
