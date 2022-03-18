package com.retrast.blog.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ICommentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("create_time", table, columnPrefix + "_create_time"));
        columns.add(Column.aliased("create_user_id", table, columnPrefix + "_create_user_id"));
        columns.add(Column.aliased("blog_id", table, columnPrefix + "_blog_id"));
        columns.add(Column.aliased("content", table, columnPrefix + "_content"));
        columns.add(Column.aliased("likes", table, columnPrefix + "_likes"));
        columns.add(Column.aliased("parent_id", table, columnPrefix + "_parent_id"));

        return columns;
    }
}
