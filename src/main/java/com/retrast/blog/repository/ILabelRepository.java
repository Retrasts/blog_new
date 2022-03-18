package com.retrast.blog.repository;

import com.retrast.blog.domain.ILabel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the ILabel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ILabelRepository extends R2dbcRepository<ILabel, Long>, ILabelRepositoryInternal {
    Flux<ILabel> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<ILabel> findAll();

    @Override
    Mono<ILabel> findById(Long id);

    @Override
    <S extends ILabel> Mono<S> save(S entity);
}

interface ILabelRepositoryInternal {
    <S extends ILabel> Mono<S> insert(S entity);
    <S extends ILabel> Mono<S> save(S entity);
    Mono<Integer> update(ILabel entity);

    Flux<ILabel> findAll();
    Mono<ILabel> findById(Long id);
    Flux<ILabel> findAllBy(Pageable pageable);
    Flux<ILabel> findAllBy(Pageable pageable, Criteria criteria);
}
