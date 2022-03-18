package com.retrast.blog.repository.rowmapper;

import com.retrast.blog.domain.IRoleMenu;
import com.retrast.blog.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IRoleMenu}, with proper type conversions.
 */
@Service
public class IRoleMenuRowMapper implements BiFunction<Row, String, IRoleMenu> {

    private final ColumnConverter converter;

    public IRoleMenuRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IRoleMenu} stored in the database.
     */
    @Override
    public IRoleMenu apply(Row row, String prefix) {
        IRoleMenu entity = new IRoleMenu();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setMenuId(converter.fromRow(row, prefix + "_menu_id", Long.class));
        return entity;
    }
}
