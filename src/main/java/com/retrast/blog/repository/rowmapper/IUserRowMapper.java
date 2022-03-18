package com.retrast.blog.repository.rowmapper;

import com.retrast.blog.domain.IUser;
import com.retrast.blog.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IUser}, with proper type conversions.
 */
@Service
public class IUserRowMapper implements BiFunction<Row, String, IUser> {

    private final ColumnConverter converter;

    public IUserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IUser} stored in the database.
     */
    @Override
    public IUser apply(Row row, String prefix) {
        IUser entity = new IUser();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIp(converter.fromRow(row, prefix + "_ip", String.class));
        entity.setUsername(converter.fromRow(row, prefix + "_username", String.class));
        entity.setNikename(converter.fromRow(row, prefix + "_nikename", String.class));
        entity.setPassword(converter.fromRow(row, prefix + "_password", String.class));
        entity.setSex(converter.fromRow(row, prefix + "_sex", Integer.class));
        entity.setEmaile(converter.fromRow(row, prefix + "_emaile", String.class));
        entity.setAvatar(converter.fromRow(row, prefix + "_avatar", String.class));
        entity.setCreateTime(converter.fromRow(row, prefix + "_create_time", LocalDate.class));
        entity.setUpdateTime(converter.fromRow(row, prefix + "_update_time", LocalDate.class));
        entity.setCreateUserId(converter.fromRow(row, prefix + "_create_user_id", Long.class));
        entity.setUpdateUserId(converter.fromRow(row, prefix + "_update_user_id", Long.class));
        entity.setBirthday(converter.fromRow(row, prefix + "_birthday", LocalDate.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", Integer.class));
        return entity;
    }
}
