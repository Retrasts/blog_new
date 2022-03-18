package com.retrast.blog.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.retrast.blog.domain.IUserRole;
import com.retrast.blog.repository.rowmapper.IUserRoleRowMapper;
import com.retrast.blog.repository.rowmapper.IUserRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the IUserRole entity.
 */
@SuppressWarnings("unused")
class IUserRoleRepositoryInternalImpl implements IUserRoleRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final IUserRowMapper iuserMapper;
    private final IUserRoleRowMapper iuserroleMapper;

    private static final Table entityTable = Table.aliased("i_user_role", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("i_user", "e_user");

    public IUserRoleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        IUserRowMapper iuserMapper,
        IUserRoleRowMapper iuserroleMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.iuserMapper = iuserMapper;
        this.iuserroleMapper = iuserroleMapper;
    }

    @Override
    public Flux<IUserRole> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<IUserRole> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<IUserRole> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = IUserRoleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(IUserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));

        String select = entityManager.createSelect(selectFrom, IUserRole.class, pageable, criteria);
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
    public Flux<IUserRole> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<IUserRole> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private IUserRole process(Row row, RowMetadata metadata) {
        IUserRole entity = iuserroleMapper.apply(row, "e");
        entity.setUser(iuserMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends IUserRole> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends IUserRole> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update IUserRole with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(IUserRole entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
