package com.retrast.blog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class IRoleSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("role_name", table, columnPrefix + "_role_name"));
        columns.add(Column.aliased("remark", table, columnPrefix + "_remark"));
        columns.add(Column.aliased("create_time", table, columnPrefix + "_create_time"));
        columns.add(Column.aliased("update_time", table, columnPrefix + "_update_time"));
        columns.add(Column.aliased("create_user_id", table, columnPrefix + "_create_user_id"));
        columns.add(Column.aliased("update_user_id", table, columnPrefix + "_update_user_id"));

        columns.add(Column.aliased("users_id", table, columnPrefix + "_users_id"));
        columns.add(Column.aliased("menus_id", table, columnPrefix + "_menus_id"));
        return columns;
    }
}
