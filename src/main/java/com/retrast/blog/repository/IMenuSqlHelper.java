package com.retrast.blog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class IMenuSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("url", table, columnPrefix + "_url"));
        columns.add(Column.aliased("menu_name", table, columnPrefix + "_menu_name"));
        columns.add(Column.aliased("parent_id", table, columnPrefix + "_parent_id"));
        columns.add(Column.aliased("create_time", table, columnPrefix + "_create_time"));
        columns.add(Column.aliased("update_time", table, columnPrefix + "_update_time"));
        columns.add(Column.aliased("create_user_id", table, columnPrefix + "_create_user_id"));
        columns.add(Column.aliased("update_user_id", table, columnPrefix + "_update_user_id"));

        return columns;
    }
}
