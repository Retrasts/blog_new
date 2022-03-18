package com.retrast.blog.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.retrast.blog.domain.IBlog;
import com.retrast.blog.repository.rowmapper.IBlogRowMapper;
import com.retrast.blog.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the IBlog entity.
 */
@SuppressWarnings("unused")
class IBlogRepositoryInternalImpl implements IBlogRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final IBlogRowMapper iblogMapper;

    private static final Table entityTable = Table.aliased("i_blog", EntityManager.ENTITY_ALIAS);

    public IBlogRepositoryInternalImpl(R2dbcEntityTemplate template, EntityManager entityManager, IBlogRowMapper iblogMapper) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.iblogMapper = iblogMapper;
    }

    @Override
    public Flux<IBlog> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<IBlog> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<IBlog> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = IBlogSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, IBlog.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(crit ->
                new StringBuilder(select)
                    .append(" ")
                    .append("WHERE")
                    .append(" ")
                    .append(alias)
                    .append(".")
                    .append(crit.toString())
                    .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<IBlog> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<IBlog> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private IBlog process(Row row, RowMetadata metadata) {
        IBlog entity = iblogMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends IBlog> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends IBlog> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update IBlog with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(IBlog entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
