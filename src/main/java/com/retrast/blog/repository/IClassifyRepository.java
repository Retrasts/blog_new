package com.retrast.blog.repository;

import com.retrast.blog.domain.IClassify;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the IClassify entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IClassifyRepository extends R2dbcRepository<IClassify, Long>, IClassifyRepositoryInternal {
    // just to avoid having unambigous methods
    @Override
    Flux<IClassify> findAll();

    @Override
    Mono<IClassify> findById(Long id);

    @Override
    <S extends IClassify> Mono<S> save(S entity);
}

interface IClassifyRepositoryInternal {
    <S extends IClassify> Mono<S> insert(S entity);
    <S extends IClassify> Mono<S> save(S entity);
    Mono<Integer> update(IClassify entity);

    Flux<IClassify> findAll();
    Mono<IClassify> findById(Long id);
    Flux<IClassify> findAllBy(Pageable pageable);
    Flux<IClassify> findAllBy(Pageable pageable, Criteria criteria);
}
