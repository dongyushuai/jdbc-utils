package com.fixiu.jdbc;

import java.lang.reflect.Constructor;
import java.util.Map;

import com.fixiu.jdbc.constant.DBPoolTypeEnum;
import com.fixiu.jdbc.constant.DatabaseTypeEnum;
import com.fixiu.jdbc.constant.QueryConstants;
import com.fixiu.jdbc.exception.CommonException;
import com.fixiu.jdbc.statement.DatasourceStatement;

/**
 * 增删改执行器工厂方法类
 *
 * @author dongyushuai
 */
public class UpdateFactory {

    public static Update create(String datasourceCode, String driverClass, String datasourceUrl, String username, String password, DatabaseTypeEnum dbType, DBPoolTypeEnum dbPoolType, Map<String, Object> options) {
        // 设置查询类和连接池类
        String queryClass = String.format(QueryConstants.Datasource.QUERYER_TEMPLATE, dbType.getValue());
        String dbPoolClass = String.format(QueryConstants.Datasource.DBPOOL_TEMPLATE, dbPoolType.getValue());
        return create(new DatasourceStatement(datasourceCode, driverClass, datasourceUrl, username, password, queryClass, dbPoolClass, options));
    }

    public static Update create(DatasourceStatement dataSource) {
        try {
            Class<?> clazz = Class.forName(dataSource.getQueryerClass());
            Constructor<?> constructor = clazz.getConstructor(DatasourceStatement.class);
            return (Update) constructor.newInstance(dataSource);
        } catch (Exception ex) {
            throw new CommonException(QueryConstants.Error.ERROR_QUERYER_NOT_FOUND, ex);
        }
    }

}
