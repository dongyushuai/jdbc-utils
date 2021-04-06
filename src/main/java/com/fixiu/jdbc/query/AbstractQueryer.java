package com.fixiu.jdbc.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fixiu.jdbc.Query;
import com.fixiu.jdbc.constant.BaseConstants;
import com.fixiu.jdbc.constant.QueryConstants;
import com.fixiu.jdbc.exception.CommonException;
import com.fixiu.jdbc.parser.CountSqlParser;
import com.fixiu.jdbc.statement.DatasourceStatement;
import com.fixiu.jdbc.statement.SqlPageStatement;
import com.fixiu.jdbc.util.JdbcUtils;

/**
 * 查询抽象类
 *
 * @author dongyushuai
 */
public abstract class AbstractQueryer implements Query {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected DatasourceStatement dataSource;

    /**
     * 处理SQL
     */
    protected CountSqlParser parser = new CountSqlParser();

    protected AbstractQueryer(DatasourceStatement dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取Map数据
     */
    @Override
    public Map<String, Object> query(String sqlText) {
        Connection conn = null;
        Statement stmt = null;
        try {
            this.logger.debug(sqlText);
            conn = this.getJdbcConnection();
            stmt = conn.createStatement();
            Map<String, Object> resultMap = new HashMap<>(BaseConstants.Digital.SIXTEEN);
            selectMapDataBySql(stmt, sqlText, resultMap);
            return resultMap;
        } catch (Exception ex) {
            this.logger.error(String.format("SqlText:%s，Msg:%s", sqlText, ex));
            throw new CommonException(QueryConstants.Error.ERROR_PARAMETER_SETTING, ex);
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, null);
        }
    }

    /**
     * 获取Map数据
     */
    @Override
    public Map<String, Object> query(String sqlText, SqlPageStatement pageStatement) {
        return query(prePageSqlText(sqlText, pageStatement));
    }

    /**
     * 获取查询SQL执行的总条数
     */
    @Override
    public long queryCount(String sqlText) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            this.logger.debug(sqlText);
            conn = this.getJdbcConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(this.getSmartCountSql(sqlText));
            rs.next();
            return rs.getLong(1);
        } catch (Exception ex) {
            this.logger.error(String.format("SqlText:%s，Msg:%s", sqlText, ex));
            throw new CommonException(QueryConstants.Error.ERROR_PARAMETER_SETTING, ex);
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
    }

    /**
     * 查询sql返回Map元素
     */
    protected void selectMapDataBySql(Statement stmt, String sql, Map<String, Object> resultMap) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsMataData;
            int count;
            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> map;
            while (rs.next()) {
                rsMataData = rs.getMetaData();
                count = rsMataData.getColumnCount();
                map = new HashMap<>(BaseConstants.Digital.SIXTEEN);
                for (int i = 1; i <= count; i++) {
                    map.put(StringUtils.upperCase(rsMataData.getColumnLabel(i)), rs.getObject(i));
                }
                mapList.add(map);
            }
            resultMap.put(QueryConstants.DataXmlAttr.DEFAULT_DS, mapList);
        } catch (Exception ex) {
            throw new CommonException(QueryConstants.Error.ERROR_PARAMETER_SETTING, ex);
        } finally {
            JdbcUtils.releaseJdbcResource(null, null, rs);
        }
    }

    @Override
    public String countSql(String sqlText) {
        return getSmartCountSql(sqlText);
    }

    @Override
    public String pageSql(String sqlText, SqlPageStatement sqlPageStatement) {
        return prePageSqlText(sqlText, sqlPageStatement);
    }

    abstract protected String prePageSqlText(String sqlText, SqlPageStatement pageStatement);


    /**
     * 预处理获取查询条数的sql语句
     *
     * @param sqlText sql
     * @return 预处理后的sql语句
     */
    protected String getSmartCountSql(String sqlText) {
        return parser.getSmartCountSql(sqlText);
    }

    /**
     * 获取当前查询器的JDBC Connection对象
     *
     * @return Connection
     */
    @Override
    public Connection getJdbcConnection() {
        try {
            Class.forName(this.dataSource.getDriverClass());
            return JdbcUtils.getDataSource(this.dataSource).getConnection();
        } catch (Exception ex) {
            throw new CommonException(ex);
        }
    }
}
