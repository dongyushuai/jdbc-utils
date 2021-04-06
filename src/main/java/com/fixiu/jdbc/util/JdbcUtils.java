package com.fixiu.jdbc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fixiu.jdbc.constant.QueryConstants;
import com.fixiu.jdbc.dbpool.DataSourcePoolFactory;
import com.fixiu.jdbc.exception.CommonException;
import com.fixiu.jdbc.statement.DatasourceStatement;

/**
 * JDBC工具类
 *
 * @author dongyushuai
 */
public class JdbcUtils {

    private JdbcUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
    private static final Map<String, DataSource> DATA_SOURCE_MAP = new ConcurrentHashMap<>(100);
    private static final Map<String, DatasourceStatement> KEY_MAP = new ConcurrentHashMap<>(100);


    public static DataSource getDataSource(DatasourceStatement datasourceStatement) {
        DataSource dataSource;
        String key = datasourceStatement.getDatasourceCode();
        if (KEY_MAP.containsKey(key)) {
            // 存在
            DatasourceStatement old = KEY_MAP.get(key);
            if (old.equals(datasourceStatement)) {
                dataSource = DATA_SOURCE_MAP.get(key);
            } else {
                // 释放连接
                try {
                    DATA_SOURCE_MAP.get(key).getConnection().close();
                } catch (SQLException e) {
                    logger.warn("connection release failed.");
                }
                // 新建datasource
                KEY_MAP.put(key, datasourceStatement);
                dataSource = DataSourcePoolFactory.create(datasourceStatement.getDbPoolClass()).wrap(datasourceStatement);
                DATA_SOURCE_MAP.put(key, dataSource);
            }
        } else {
            // 不存在
            KEY_MAP.put(key, datasourceStatement);
            dataSource = DataSourcePoolFactory.create(datasourceStatement.getDbPoolClass()).wrap(datasourceStatement);
            DATA_SOURCE_MAP.put(key, dataSource);
        }
        return dataSource;
    }

    public static void releaseJdbcResource(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            logger.error("Release jdbc resource error", ex);
            throw new CommonException(QueryConstants.Error.ERROR_RELEASE_JDBC_RESOURCE, ex);
        }
    }
}
