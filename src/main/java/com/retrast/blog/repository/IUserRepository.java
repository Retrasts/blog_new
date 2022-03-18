package com.retrast.blog.repository;

import com.retrast.blog.domain.IUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the IUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IUserRepository extends R2dbcRepository<IUser, Long>, IUserRepositoryInternal {
    Flux<IUser> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<IUser> findAll();

    @Override
    Mono<IUser> findById(Long id);

    @Override
    <S extends IUser> Mono<S> save(S entity);
}

interface IUserRepositoryInternal {
    <S extends IUser> Mono<S> insert(S entity);
    <S extends IUser> Mono<S> save(S entity);
    Mono<Integer> update(IUser entity);

    Flux<IUser> findAll();
    Mono<IUser> findById(Long id);
    Flux<IUser> findAllBy(Pageable pageable);
    Flux<IUser> findAllBy(Pageable pageable, Criteria criteria);
}
