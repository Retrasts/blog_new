package com.retrast.blog.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.retrast.blog.domain.IRole;
import com.retrast.blog.repository.rowmapper.IRoleMenuRowMapper;
import com.retrast.blog.repository.rowmapper.IRoleRowMapper;
import com.retrast.blog.repository.rowmapper.IUserRoleRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the IRole entity.
 */
@SuppressWarnings("unused")
class IRoleRepositoryInternalImpl implements IRoleRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final IUserRoleRowMapper iuserroleMapper;
    private final IRoleMenuRowMapper irolemenuMapper;
    private final IRoleRowMapper iroleMapper;

    private static final Table entityTable = Table.aliased("i_role", EntityManager.ENTITY_ALIAS);
    private static final Table usersTable = Table.aliased("i_user_role", "users");
    private static final Table menusTable = Table.aliased("i_role_menu", "menus");

    public IRoleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        IUserRoleRowMapper iuserroleMapper,
        IRoleMenuRowMapper irolemenuMapper,
        IRoleRowMapper iroleMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.iuserroleMapper = iuserroleMapper;
        this.irolemenuMapper = irolemenuMapper;
        this.iroleMapper = iroleMapper;
    }

    @Override
    public Flux<IRole> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<IRole> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<IRole> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = IRoleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(IUserRoleSqlHelper.getColumns(usersTable, "users"));
        columns.addAll(IRoleMenuSqlHelper.getColumns(menusTable, "menus"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(usersTable)
            .on(Column.create("users_id", entityTable))
            .equals(Column.create("id", usersTable))
            .leftOuterJoin(menusTable)
            .on(Column.create("menus_id", entityTable))
            .equals(Column.create("id", menusTable));

        String select = entityManager.createSelect(selectFrom, IRole.class, pageable, criteria);
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
    public Flux<IRole> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<IRole> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private IRole process(Row row, RowMetadata metadata) {
        IRole entity = iroleMapper.apply(row, "e");
        entity.setUsers(iuserroleMapper.apply(row, "users"));
        entity.setMenus(irolemenuMapper.apply(row, "menus"));
        return entity;
    }

    @Override
    public <S extends IRole> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends IRole> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update IRole with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(IRole entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
