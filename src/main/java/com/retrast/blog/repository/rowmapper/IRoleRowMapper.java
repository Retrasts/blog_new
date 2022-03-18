package com.retrast.blog.repository.rowmapper;

import com.retrast.blog.domain.IRole;
import com.retrast.blog.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IRole}, with proper type conversions.
 */
@Service
public class IRoleRowMapper implements BiFunction<Row, String, IRole> {

    private final ColumnConverter converter;

    public IRoleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IRole} stored in the database.
     */
    @Override
    public IRole apply(Row row, String prefix) {
        IRole entity = new IRole();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRoleName(converter.fromRow(row, prefix + "_role_name", String.class));
        entity.setRemark(converter.fromRow(row, prefix + "_remark", String.class));
        entity.setCreateTime(converter.fromRow(row, prefix + "_create_time", LocalDate.class));
        entity.setUpdateTime(converter.fromRow(row, prefix + "_update_time", LocalDate.class));
        entity.setCreateUserId(converter.fromRow(row, prefix + "_create_user_id", Long.class));
        entity.setUpdateUserId(converter.fromRow(row, prefix + "_update_user_id", Long.class));
        entity.setUsersId(converter.fromRow(row, prefix + "_users_id", Long.class));
        entity.setMenusId(converter.fromRow(row, prefix + "_menus_id", Long.class));
        return entity;
    }
}
