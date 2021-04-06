package com.fixiu.jdbc.dbpool;

import javax.sql.DataSource;

import org.apache.commons.collections4.MapUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.fixiu.jdbc.constant.QueryConstants;
import com.fixiu.jdbc.dbpool.option.DruidOption;
import com.fixiu.jdbc.exception.CommonException;
import com.fixiu.jdbc.statement.DatasourceStatement;

/**
 * Druid数据源连接池包装类
 *
 * <a href="https://github.com/alibaba/druid/wiki>Druid</a>
 *
 * @author dongyushuai
 */
public class DruidDataSourcePool implements DataSourcePoolWrapper {
    @Override
    public DataSource wrap(DatasourceStatement datasourceStatement) {
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName(datasourceStatement.getDriverClass());
            dataSource.setUrl(datasourceStatement.getJdbcUrl());
            dataSource.setUsername(datasourceStatement.getUsername());
            dataSource.setPassword(datasourceStatement.getPassword());
            dataSource.setInitialSize(MapUtils.getInteger(datasourceStatement.getOptions(), DruidOption.INITIAL_SIZE, 3));
            dataSource.setMaxActive(MapUtils.getInteger(datasourceStatement.getOptions(), DruidOption.MAX_ACTIVE, 20));
            dataSource.setMinIdle(MapUtils.getInteger(datasourceStatement.getOptions(), DruidOption.MIN_IDLE, 1));
            dataSource.setMaxWait(MapUtils.getInteger(datasourceStatement.getOptions(), DruidOption.MAX_WAIT, 60000));
            dataSource.setTimeBetweenEvictionRunsMillis(MapUtils.getInteger(datasourceStatement.getOptions(),
                    DruidOption.TIME_BETWEEN_EVICTION_RUNS_MILLIS, 60000));
            dataSource.setMinEvictableIdleTimeMillis(MapUtils.getInteger(datasourceStatement.getOptions(),
                    DruidOption.MIN_EVICTABLE_IDLE_TIME_MILLIS, 300000));
            dataSource.setValidationQuery(MapUtils.getString(datasourceStatement.getOptions(), DruidOption.VALIDATION_QUERY, "select 1"));
            dataSource.setTestWhileIdle(
                    MapUtils.getBoolean(datasourceStatement.getOptions(), DruidOption.TEST_WHILE_IDLE, true));
            dataSource.setTestOnBorrow(
                    MapUtils.getBoolean(datasourceStatement.getOptions(), DruidOption.TEST_ON_BORROW, false));
            dataSource.setTestOnReturn(
                    MapUtils.getBoolean(datasourceStatement.getOptions(), DruidOption.TEST_ON_RETURN, false));
            dataSource.setMaxOpenPreparedStatements(MapUtils.getInteger(datasourceStatement.getOptions(),
                    DruidOption.MAX_OPEN_PREPARED_STATEMENTS, 20));
            dataSource.setRemoveAbandoned(
                    MapUtils.getBoolean(datasourceStatement.getOptions(), DruidOption.REMOVE_ABANDONED, true));
            dataSource.setRemoveAbandonedTimeout(MapUtils.getInteger(datasourceStatement.getOptions(),
                    DruidOption.REMOVE_ABANDONED_TIMEOUT, 1800));
            dataSource.setLogAbandoned(
                    MapUtils.getBoolean(datasourceStatement.getOptions(), DruidOption.LOG_ABANDONED, true));
            return dataSource;
        } catch (Exception ex) {
            throw new CommonException(QueryConstants.Error.ERROR_DATASOURCE_POOL_CREATE, "Druid", ex);
        }
    }
}
