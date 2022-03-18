package com.retrast.blog.repository;

import com.retrast.blog.domain.IBlog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the IBlog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IBlogRepository extends R2dbcRepository<IBlog, Long>, IBlogRepositoryInternal {
    Flux<IBlog> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<IBlog> findAll();

    @Override
    Mono<IBlog> findById(Long id);

    @Override
    <S extends IBlog> Mono<S> save(S entity);
}

interface IBlogRepositoryInternal {
    <S extends IBlog> Mono<S> insert(S entity);
    <S extends IBlog> Mono<S> save(S entity);
    Mono<Integer> update(IBlog entity);

    Flux<IBlog> findAll();
    Mono<IBlog> findById(Long id);
    Flux<IBlog> findAllBy(Pageable pageable);
    Flux<IBlog> findAllBy(Pageable pageable, Criteria criteria);
}
