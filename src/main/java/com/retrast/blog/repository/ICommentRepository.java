package com.retrast.blog.repository;

import com.retrast.blog.domain.IComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the IComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ICommentRepository extends R2dbcRepository<IComment, Long>, ICommentRepositoryInternal {
    Flux<IComment> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<IComment> findAll();

    @Override
    Mono<IComment> findById(Long id);

    @Override
    <S extends IComment> Mono<S> save(S entity);
}

interface ICommentRepositoryInternal {
    <S extends IComment> Mono<S> insert(S entity);
    <S extends IComment> Mono<S> save(S entity);
    Mono<Integer> update(IComment entity);

    Flux<IComment> findAll();
    Mono<IComment> findById(Long id);
    Flux<IComment> findAllBy(Pageable pageable);
    Flux<IComment> findAllBy(Pageable pageable, Criteria criteria);
}
