package com.retrast.blog.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.retrast.blog.domain.IRoleMenu;
import com.retrast.blog.repository.rowmapper.IMenuRowMapper;
import com.retrast.blog.repository.rowmapper.IRoleMenuRowMapper;
import com.retrast.blog.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the IRoleMenu entity.
 */
@SuppressWarnings("unused")
class IRoleMenuRepositoryInternalImpl implements IRoleMenuRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final IMenuRowMapper imenuMapper;
    private final IRoleMenuRowMapper irolemenuMapper;

    private static final Table entityTable = Table.aliased("i_role_menu", EntityManager.ENTITY_ALIAS);
    private static final Table menuTable = Table.aliased("i_menu", "menu");

    public IRoleMenuRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        IMenuRowMapper imenuMapper,
        IRoleMenuRowMapper irolemenuMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.imenuMapper = imenuMapper;
        this.irolemenuMapper = irolemenuMapper;
    }

    @Override
    public Flux<IRoleMenu> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<IRoleMenu> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<IRoleMenu> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = IRoleMenuSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(IMenuSqlHelper.getColumns(menuTable, "menu"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(menuTable)
            .on(Column.create("menu_id", entityTable))
            .equals(Column.create("id", menuTable));

        String select = entityManager.createSelect(selectFrom, IRoleMenu.class, pageable, criteria);
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
    public Flux<IRoleMenu> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<IRoleMenu> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private IRoleMenu process(Row row, RowMetadata metadata) {
        IRoleMenu entity = irolemenuMapper.apply(row, "e");
        entity.setMenu(imenuMapper.apply(row, "menu"));
        return entity;
    }

    @Override
    public <S extends IRoleMenu> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends IRoleMenu> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update IRoleMenu with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(IRoleMenu entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
