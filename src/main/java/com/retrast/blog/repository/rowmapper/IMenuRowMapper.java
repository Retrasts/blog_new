package com.retrast.blog.repository.rowmapper;

import com.retrast.blog.domain.IMenu;
import com.retrast.blog.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IMenu}, with proper type conversions.
 */
@Service
public class IMenuRowMapper implements BiFunction<Row, String, IMenu> {

    private final ColumnConverter converter;

    public IMenuRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IMenu} stored in the database.
     */
    @Override
    public IMenu apply(Row row, String prefix) {
        IMenu entity = new IMenu();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        entity.setMenuName(converter.fromRow(row, prefix + "_menu_name", String.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        entity.setCreateTime(converter.fromRow(row, prefix + "_create_time", LocalDate.class));
        entity.setUpdateTime(converter.fromRow(row, prefix + "_update_time", LocalDate.class));
        entity.setCreateUserId(converter.fromRow(row, prefix + "_create_user_id", Long.class));
        entity.setUpdateUserId(converter.fromRow(row, prefix + "_update_user_id", Long.class));
        return entity;
    }
}
