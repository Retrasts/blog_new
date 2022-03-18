package com.retrast.blog.repository.rowmapper;

import com.retrast.blog.domain.ILabel;
import com.retrast.blog.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ILabel}, with proper type conversions.
 */
@Service
public class ILabelRowMapper implements BiFunction<Row, String, ILabel> {

    private final ColumnConverter converter;

    public ILabelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ILabel} stored in the database.
     */
    @Override
    public ILabel apply(Row row, String prefix) {
        ILabel entity = new ILabel();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAlias(converter.fromRow(row, prefix + "_alias", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        return entity;
    }
}
