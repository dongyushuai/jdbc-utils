package com.fixiu.jdbc.dbpool;


import javax.sql.DataSource;

import com.fixiu.jdbc.statement.DatasourceStatement;

/**
 * 数据源连接包装器
 *
 * @author dongyushuai
 */
public interface DataSourcePoolWrapper {
    DataSource wrap(DatasourceStatement rptDs);
}
