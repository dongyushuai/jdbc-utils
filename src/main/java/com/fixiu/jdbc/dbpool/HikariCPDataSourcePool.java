package com.fixiu.jdbc.dbpool;

import javax.sql.DataSource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.fixiu.jdbc.constant.QueryConstants;
import com.fixiu.jdbc.dbpool.option.HikariCPOption;
import com.fixiu.jdbc.exception.CommonException;
import com.fixiu.jdbc.statement.DatasourceStatement;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Hikari数据源连接池包装类
 *
 * @author dongyushuai
 */
public class HikariCPDataSourcePool implements DataSourcePoolWrapper {
	
	@Override
    public DataSource wrap(DatasourceStatement datasourceStatement) {
        try {
        	HikariConfig config = new HikariConfig();
        	config.setJdbcUrl(datasourceStatement.getJdbcUrl());
        	config.setUsername(datasourceStatement.getUsername());
        	config.setPassword(datasourceStatement.getPassword());
        	config.setAutoCommit(false);
        	if(StringUtils.isNotBlank(datasourceStatement.getDriverClass())) {
        		config.setDriverClassName(datasourceStatement.getDriverClass());
        	}
        	config.setAllowPoolSuspension(MapUtils.getBooleanValue(datasourceStatement.getOptions(), HikariCPOption.IS_ALLOW_POOL_SUSPENSION, config.isAllowPoolSuspension()));
        	config.setCatalog(MapUtils.getString(datasourceStatement.getOptions(), HikariCPOption.CATALOG, config.getCatalog()));
        	config.setConnectionInitSql(MapUtils.getString(datasourceStatement.getOptions(), HikariCPOption.CONNECTION_INIT_SQL, config.getConnectionInitSql()));
        	config.setConnectionTestQuery(MapUtils.getString(datasourceStatement.getOptions(), HikariCPOption.CONNECTION_TEST_QUERY, config.getConnectionTestQuery()));
        	config.setConnectionTimeout(MapUtils.getLongValue(datasourceStatement.getOptions(), HikariCPOption.CONNECTION_TIMEOUTMS, config.getConnectionTimeout()));
        	config.setDataSourceClassName(MapUtils.getString(datasourceStatement.getOptions(), HikariCPOption.CLASSNAME, config.getDataSourceClassName()));
        	config.setExceptionOverrideClassName(MapUtils.getString(datasourceStatement.getOptions(), HikariCPOption.EXCEPTION_OVERRIDE_CLASSNAME, config.getExceptionOverrideClassName()));
        	config.setIdleTimeout(MapUtils.getLongValue(datasourceStatement.getOptions(), HikariCPOption.IDLE_TIMEOUTMS, config.getIdleTimeout()));
        	config.setInitializationFailTimeout(MapUtils.getLongValue(datasourceStatement.getOptions(), HikariCPOption.INITIALIZATION_FAIL_TIMEOUT, config.getInitializationFailTimeout()));
        	config.setIsolateInternalQueries(MapUtils.getBooleanValue(datasourceStatement.getOptions(), HikariCPOption.ISOLATE, config.isIsolateInternalQueries()));
        	config.setLeakDetectionThreshold(MapUtils.getLongValue(datasourceStatement.getOptions(), HikariCPOption.LEAKD_ETECTION_THRESHOLDMS, config.getLeakDetectionThreshold()));
        	config.setMaximumPoolSize(MapUtils.getIntValue(datasourceStatement.getOptions(), HikariCPOption.MAX_POOL_SIZE, config.getMaximumPoolSize()));
        	config.setMaxLifetime(MapUtils.getLongValue(datasourceStatement.getOptions(), HikariCPOption.MAX_LIFETIMEMS, config.getMaxLifetime()));
        	config.setMinimumIdle(MapUtils.getIntValue(datasourceStatement.getOptions(), HikariCPOption.MIN_IDLE, config.getMinimumIdle()));
        	config.setPoolName(MapUtils.getString(datasourceStatement.getOptions(), HikariCPOption.POOL_NAME, config.getPoolName()));
        	config.setReadOnly(MapUtils.getBooleanValue(datasourceStatement.getOptions(), HikariCPOption.READ_ONLY, config.isReadOnly()));
        	config.setSchema(MapUtils.getString(datasourceStatement.getOptions(), HikariCPOption.SCHEMA, config.getSchema()));
        	config.setTransactionIsolation(MapUtils.getString(datasourceStatement.getOptions(), HikariCPOption.ISOLATION_LEVEL, config.getTransactionIsolation()));
        	config.setValidationTimeout(MapUtils.getLongValue(datasourceStatement.getOptions(), HikariCPOption.VALIDATIONT_IMEOUTMS, config.getValidationTimeout()));
        	HikariDataSource dataSource = new HikariDataSource(config);
            return dataSource;
        } catch (Exception ex) {
            throw new CommonException(QueryConstants.Error.ERROR_DATASOURCE_POOL_CREATE, "HikariCP", ex);
        }
    }
}
