package com.retrast.blog.repository.rowmapper;

import com.retrast.blog.domain.IClassify;
import com.retrast.blog.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IClassify}, with proper type conversions.
 */
@Service
public class IClassifyRowMapper implements BiFunction<Row, String, IClassify> {

    private final ColumnConverter converter;

    public IClassifyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IClassify} stored in the database.
     */
    @Override
    public IClassify apply(Row row, String prefix) {
        IClassify entity = new IClassify();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAlias(converter.fromRow(row, prefix + "_alias", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        return entity;
    }
}
