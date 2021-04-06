package com.fixiu.jdbc.dbpool;

import javax.sql.DataSource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;

import com.fixiu.jdbc.constant.QueryConstants;
import com.fixiu.jdbc.dbpool.option.Dbcp2Option;
import com.fixiu.jdbc.exception.CommonException;
import com.fixiu.jdbc.statement.DatasourceStatement;

/**
 * DBCP2数据源连接池包装类
 *
 * @author dongyushuai
 */
public class Dbcp2DataSourcePool implements DataSourcePoolWrapper {
    @Override
    public DataSource wrap(DatasourceStatement datasourceStatement) {
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(datasourceStatement.getDriverClass());
            dataSource.setUrl(datasourceStatement.getJdbcUrl());
            dataSource.setUsername(datasourceStatement.getUsername());
            dataSource.setPassword(datasourceStatement.getPassword());
            dataSource.setInitialSize(MapUtils.getInteger(datasourceStatement.getOptions(), Dbcp2Option.INITIAL_SIZE, 3));
            dataSource.setMaxIdle(MapUtils.getInteger(datasourceStatement.getOptions(), Dbcp2Option.MAX_IDLE, 20));
            dataSource.setMinIdle(MapUtils.getInteger(datasourceStatement.getOptions(), Dbcp2Option.MAX_IDLE, 1));
            dataSource.setLogAbandoned(
                    MapUtils.getBoolean(datasourceStatement.getOptions(), Dbcp2Option.LOG_ABANDONED, true));
            dataSource.setRemoveAbandonedTimeout(MapUtils.getInteger(datasourceStatement.getOptions(),
                    Dbcp2Option.REMOVE_ABANDONED_TIMEOUT, 180));
            dataSource.setMaxWaitMillis(MapUtils.getInteger(datasourceStatement.getOptions(), Dbcp2Option.MAX_WAIT, 1000));
            return dataSource;
        } catch (Exception ex) {
            throw new CommonException(QueryConstants.Error.ERROR_DATASOURCE_POOL_CREATE, "Dbcp2", ex);
        }
    }
}
