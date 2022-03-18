package com.retrast.blog.service;

import com.retrast.blog.domain.IClassify;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link IClassify}.
 */
public interface IClassifyService {
    /**
     * Save a iClassify.
     *
     * @param iClassify the entity to save.
     * @return the persisted entity.
     */
    Mono<IClassify> save(IClassify iClassify);

    /**
     * Partially updates a iClassify.
     *
     * @param iClassify the entity to update partially.
     * @return the persisted entity.
     */
    Mono<IClassify> partialUpdate(IClassify iClassify);

    /**
     * Get all the iClassifies.
     *
     * @return the list of entities.
     */
    Flux<IClassify> findAll();

    /**
     * Returns the number of iClassifies available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" iClassify.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<IClassify> findOne(Long id);

    /**
     * Delete the "id" iClassify.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
