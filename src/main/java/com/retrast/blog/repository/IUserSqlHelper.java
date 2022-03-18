package com.retrast.blog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class IUserSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("ip", table, columnPrefix + "_ip"));
        columns.add(Column.aliased("username", table, columnPrefix + "_username"));
        columns.add(Column.aliased("nikename", table, columnPrefix + "_nikename"));
        columns.add(Column.aliased("password", table, columnPrefix + "_password"));
        columns.add(Column.aliased("sex", table, columnPrefix + "_sex"));
        columns.add(Column.aliased("emaile", table, columnPrefix + "_emaile"));
        columns.add(Column.aliased("avatar", table, columnPrefix + "_avatar"));
        columns.add(Column.aliased("create_time", table, columnPrefix + "_create_time"));
        columns.add(Column.aliased("update_time", table, columnPrefix + "_update_time"));
        columns.add(Column.aliased("create_user_id", table, columnPrefix + "_create_user_id"));
        columns.add(Column.aliased("update_user_id", table, columnPrefix + "_update_user_id"));
        columns.add(Column.aliased("birthday", table, columnPrefix + "_birthday"));
        columns.add(Column.aliased("company", table, columnPrefix + "_company"));
        columns.add(Column.aliased("phone", table, columnPrefix + "_phone"));

        return columns;
    }
}
