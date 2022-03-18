package com.retrast.blog.repository.rowmapper;

import com.retrast.blog.domain.IComment;
import com.retrast.blog.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IComment}, with proper type conversions.
 */
@Service
public class ICommentRowMapper implements BiFunction<Row, String, IComment> {

    private final ColumnConverter converter;

    public ICommentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IComment} stored in the database.
     */
    @Override
    public IComment apply(Row row, String prefix) {
        IComment entity = new IComment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreateTime(converter.fromRow(row, prefix + "_create_time", LocalDate.class));
        entity.setCreateUserId(converter.fromRow(row, prefix + "_create_user_id", Long.class));
        entity.setBlogId(converter.fromRow(row, prefix + "_blog_id", Long.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setLikes(converter.fromRow(row, prefix + "_likes", Long.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        return entity;
    }
}
