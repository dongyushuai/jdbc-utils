package com.fixiu.jdbc.dbpool;

import javax.sql.DataSource;

import org.apache.commons.collections4.MapUtils;

import com.fixiu.jdbc.constant.QueryConstants;
import com.fixiu.jdbc.dbpool.option.C3p0Option;
import com.fixiu.jdbc.exception.CommonException;
import com.fixiu.jdbc.statement.DatasourceStatement;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * c3p0数据源连接池包装类
 *
 * <a href="http://www.mchange.com/projects/c3p0/#quickstart>c3po</a>
 *
 * @author dongyushuai
 */
public class C3p0DataSourcePool implements DataSourcePoolWrapper {
    @Override
    public DataSource wrap(DatasourceStatement datasourceStatement) {
        try {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(datasourceStatement.getDriverClass());
            dataSource.setJdbcUrl(datasourceStatement.getJdbcUrl());
            dataSource.setUser(datasourceStatement.getUsername());
            dataSource.setPassword(datasourceStatement.getPassword());
            dataSource.setInitialPoolSize(
                    MapUtils.getInteger(datasourceStatement.getOptions(), C3p0Option.INITIAL_POOL_SIZE, 3));
            dataSource.setMinPoolSize(MapUtils.getInteger(datasourceStatement.getOptions(), C3p0Option.MIN_POOL_SIZE, 1));
            dataSource.setMaxPoolSize(MapUtils.getInteger(datasourceStatement.getOptions(), C3p0Option.MAX_POOL_SIZE, 20));
            dataSource.setMaxStatements(
                    MapUtils.getInteger(datasourceStatement.getOptions(), C3p0Option.MAX_STATEMENTS, 50));
            dataSource.setMaxIdleTime(
                    MapUtils.getInteger(datasourceStatement.getOptions(), C3p0Option.MAX_IDLE_TIME, 1800));
            dataSource.setAcquireIncrement(
                    MapUtils.getInteger(datasourceStatement.getOptions(), C3p0Option.ACQUIRE_INCREMENT, 3));
            dataSource.setAcquireRetryAttempts(MapUtils.getInteger(datasourceStatement.getOptions(),
                    C3p0Option.BREAK_AFTER_ACQUIRE_FAILURE, 30));
            dataSource.setIdleConnectionTestPeriod(MapUtils.getInteger(datasourceStatement.getOptions(),
                    C3p0Option.IDLE_CONNECTION_TEST_PERIOD, 60));
            dataSource.setBreakAfterAcquireFailure(MapUtils.getBoolean(datasourceStatement.getOptions(),
                    C3p0Option.BREAK_AFTER_ACQUIRE_FAILURE, false));
            dataSource.setTestConnectionOnCheckout(MapUtils.getBoolean(datasourceStatement.getOptions(),
                    C3p0Option.TEST_CONNECTION_ON_CHECKOUT, false));
            return dataSource;
        } catch (Exception ex) {
            throw new CommonException(QueryConstants.Error.ERROR_DATASOURCE_POOL_CREATE, "C3p0", ex);
        }
    }
}
