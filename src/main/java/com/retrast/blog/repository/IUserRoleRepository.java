package com.retrast.blog.repository;

import com.retrast.blog.domain.IUserRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the IUserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IUserRoleRepository extends R2dbcRepository<IUserRole, Long>, IUserRoleRepositoryInternal {
    @Query("SELECT * FROM i_user_role entity WHERE entity.id not in (select users_id from i_role)")
    Flux<IUserRole> findAllWhereRoleIsNull();

    @Query("SELECT * FROM i_user_role entity WHERE entity.user_id = :id")
    Flux<IUserRole> findByUser(Long id);

    @Query("SELECT * FROM i_user_role entity WHERE entity.user_id IS NULL")
    Flux<IUserRole> findAllWhereUserIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<IUserRole> findAll();

    @Override
    Mono<IUserRole> findById(Long id);

    @Override
    <S extends IUserRole> Mono<S> save(S entity);
}

interface IUserRoleRepositoryInternal {
    <S extends IUserRole> Mono<S> insert(S entity);
    <S extends IUserRole> Mono<S> save(S entity);
    Mono<Integer> update(IUserRole entity);

    Flux<IUserRole> findAll();
    Mono<IUserRole> findById(Long id);
    Flux<IUserRole> findAllBy(Pageable pageable);
    Flux<IUserRole> findAllBy(Pageable pageable, Criteria criteria);
}
