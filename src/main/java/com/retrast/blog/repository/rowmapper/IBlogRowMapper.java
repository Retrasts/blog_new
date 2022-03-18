package com.retrast.blog.repository.rowmapper;

import com.retrast.blog.domain.IBlog;
import com.retrast.blog.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IBlog}, with proper type conversions.
 */
@Service
public class IBlogRowMapper implements BiFunction<Row, String, IBlog> {

    private final ColumnConverter converter;

    public IBlogRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IBlog} stored in the database.
     */
    @Override
    public IBlog apply(Row row, String prefix) {
        IBlog entity = new IBlog();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreateUserId(converter.fromRow(row, prefix + "_create_user_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setLabel(converter.fromRow(row, prefix + "_label", Long.class));
        entity.setClassify(converter.fromRow(row, prefix + "_classify", Long.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setLikes(converter.fromRow(row, prefix + "_likes", Long.class));
        entity.setReplynumber(converter.fromRow(row, prefix + "_replynumber", Long.class));
        entity.setCreateTime(converter.fromRow(row, prefix + "_create_time", LocalDate.class));
        entity.setUpdateTime(converter.fromRow(row, prefix + "_update_time", LocalDate.class));
        return entity;
    }
}
