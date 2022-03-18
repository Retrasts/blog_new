package com.retrast.blog.repository;

import com.retrast.blog.domain.IMenu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the IMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IMenuRepository extends R2dbcRepository<IMenu, Long>, IMenuRepositoryInternal {
    Flux<IMenu> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<IMenu> findAll();

    @Override
    Mono<IMenu> findById(Long id);

    @Override
    <S extends IMenu> Mono<S> save(S entity);
}

interface IMenuRepositoryInternal {
    <S extends IMenu> Mono<S> insert(S entity);
    <S extends IMenu> Mono<S> save(S entity);
    Mono<Integer> update(IMenu entity);

    Flux<IMenu> findAll();
    Mono<IMenu> findById(Long id);
    Flux<IMenu> findAllBy(Pageable pageable);
    Flux<IMenu> findAllBy(Pageable pageable, Criteria criteria);
}
