package com.retrast.blog.repository;

import com.retrast.blog.domain.IRoleMenu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the IRoleMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IRoleMenuRepository extends R2dbcRepository<IRoleMenu, Long>, IRoleMenuRepositoryInternal {
    @Query("SELECT * FROM i_role_menu entity WHERE entity.id not in (select menus_id from i_role)")
    Flux<IRoleMenu> findAllWhereRoleIsNull();

    @Query("SELECT * FROM i_role_menu entity WHERE entity.menu_id = :id")
    Flux<IRoleMenu> findByMenu(Long id);

    @Query("SELECT * FROM i_role_menu entity WHERE entity.menu_id IS NULL")
    Flux<IRoleMenu> findAllWhereMenuIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<IRoleMenu> findAll();

    @Override
    Mono<IRoleMenu> findById(Long id);

    @Override
    <S extends IRoleMenu> Mono<S> save(S entity);
}

interface IRoleMenuRepositoryInternal {
    <S extends IRoleMenu> Mono<S> insert(S entity);
    <S extends IRoleMenu> Mono<S> save(S entity);
    Mono<Integer> update(IRoleMenu entity);

    Flux<IRoleMenu> findAll();
    Mono<IRoleMenu> findById(Long id);
    Flux<IRoleMenu> findAllBy(Pageable pageable);
    Flux<IRoleMenu> findAllBy(Pageable pageable, Criteria criteria);
}
