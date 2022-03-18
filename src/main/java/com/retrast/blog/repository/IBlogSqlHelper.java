package com.retrast.blog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class IBlogSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("create_user_id", table, columnPrefix + "_create_user_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("label", table, columnPrefix + "_label"));
        columns.add(Column.aliased("classify", table, columnPrefix + "_classify"));
        columns.add(Column.aliased("content", table, columnPrefix + "_content"));
        columns.add(Column.aliased("likes", table, columnPrefix + "_likes"));
        columns.add(Column.aliased("replynumber", table, columnPrefix + "_replynumber"));
        columns.add(Column.aliased("create_time", table, columnPrefix + "_create_time"));
        columns.add(Column.aliased("update_time", table, columnPrefix + "_update_time"));

        return columns;
    }
}
