package com.retrast.blog.repository;

import com.retrast.blog.domain.IRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the IRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IRoleRepository extends R2dbcRepository<IRole, Long>, IRoleRepositoryInternal {
    Flux<IRole> findAllBy(Pageable pageable);

    @Query("SELECT * FROM i_role entity WHERE entity.users_id = :id")
    Flux<IRole> findByUsers(Long id);

    @Query("SELECT * FROM i_role entity WHERE entity.users_id IS NULL")
    Flux<IRole> findAllWhereUsersIsNull();

    @Query("SELECT * FROM i_role entity WHERE entity.menus_id = :id")
    Flux<IRole> findByMenus(Long id);

    @Query("SELECT * FROM i_role entity WHERE entity.menus_id IS NULL")
    Flux<IRole> findAllWhereMenusIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<IRole> findAll();

    @Override
    Mono<IRole> findById(Long id);

    @Override
    <S extends IRole> Mono<S> save(S entity);
}

interface IRoleRepositoryInternal {
    <S extends IRole> Mono<S> insert(S entity);
    <S extends IRole> Mono<S> save(S entity);
    Mono<Integer> update(IRole entity);

    Flux<IRole> findAll();
    Mono<IRole> findById(Long id);
    Flux<IRole> findAllBy(Pageable pageable);
    Flux<IRole> findAllBy(Pageable pageable, Criteria criteria);
}
