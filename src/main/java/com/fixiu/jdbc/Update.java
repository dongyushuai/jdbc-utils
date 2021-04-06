package com.fixiu.jdbc;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.fixiu.jdbc.constant.DBPoolTypeEnum;
import com.fixiu.jdbc.constant.DatabaseTypeEnum;
import com.fixiu.jdbc.exception.CommonException;
import com.fixiu.jdbc.statement.DatasourceStatement;
import com.fixiu.jdbc.util.JdbcUtils;

/**
 * @author dongyushuai
 */
public class Update {
    private DatasourceStatement datasourceStatement;
    private Connection connection;

    public Update(DatasourceStatement datasourceStatement) {
        this.datasourceStatement = datasourceStatement;
    }

    public Update(String datasourceCode, String driverClass, String datasourceUrl, String username, String password, DatabaseTypeEnum dbType, DBPoolTypeEnum dbPoolType, Map<String, Object> options) {
        // 设置查询类和连接池类
        this(new DatasourceStatement(datasourceCode, driverClass, datasourceUrl, username, password, dbType, dbPoolType, options));
    }

    public int executeAndClose(String sqlText) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = this.getJdbcConnection();
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            return stmt.executeUpdate(sqlText);
        } catch (Exception ex) {
            throw new CommonException(ex);
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, null);
            this.connection = null;
        }
    }

    public int[] executeAndClose(List<String> sqlTexts) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = this.getJdbcConnection();
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            for (String text : sqlTexts) {
                stmt.addBatch(text);
            }
            int[] batchResult = stmt.executeBatch();
            stmt.clearBatch();
            return batchResult;
        } catch (Exception ex) {
            throw new CommonException(ex);
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, null);
            this.connection = null;
        }
    }

    public Connection execute(String sqlText) {
        Connection conn;
        Statement stmt = null;
        try {
            conn = getJdbcConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlText);
        } catch (Exception ex) {
            throw new CommonException(ex);
        } finally {
            JdbcUtils.releaseJdbcResource(null, stmt, null);
        }
        return conn;
    }

    /**
     * 批量更新
     * <strong>慎用，只有部分数据库支持</strong>
     * 批量更新语句中禁止使用<code>SELECT</code>
     * 可以通过 {@link java.sql.DatabaseMetaData#supportsBatchUpdates()} 方法查看当前数据源是否支持批量更新
     *
     * @param sqlTexts 批量更新命令
     * @return 数据库连接
     */
    public Connection execute(List<String> sqlTexts) {
        Connection conn;
        Statement stmt = null;
        try {
            conn = getJdbcConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (String text : sqlTexts) {
                stmt.addBatch(text);
            }
            stmt.executeBatch();
            stmt.clearBatch();
        } catch (Exception ex) {
            throw new CommonException(ex);
        } finally {
            JdbcUtils.releaseJdbcResource(null, stmt, null);
        }
        return conn;
    }

    public void commitAndClose() {
        Connection connection = null;
        try {
            connection = getJdbcConnection();
            connection.commit();
        } catch (Exception ex) {
            throw new CommonException(ex);
        } finally {
            JdbcUtils.releaseJdbcResource(connection, null, null);
        }
    }

    /**
     * 获取当前查询器的JDBC Connection对象
     *
     * @return Connection
     */
    public Connection getJdbcConnection() {
        if (connection == null) {
            try {
                Class.forName(this.datasourceStatement.getDriverClass());
                connection = JdbcUtils.getDataSource(this.datasourceStatement).getConnection();
            } catch (Exception ex) {
                throw new CommonException(ex);
            }
        }
        return connection;
    }
}
