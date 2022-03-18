package com.retrast.blog.repository.rowmapper;

import com.retrast.blog.domain.IUserRole;
import com.retrast.blog.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IUserRole}, with proper type conversions.
 */
@Service
public class IUserRoleRowMapper implements BiFunction<Row, String, IUserRole> {

    private final ColumnConverter converter;

    public IUserRoleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IUserRole} stored in the database.
     */
    @Override
    public IUserRole apply(Row row, String prefix) {
        IUserRole entity = new IUserRole();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
