package com.fixiu.jdbc.dbpool;

import com.fixiu.jdbc.constant.QueryConstants;
import com.fixiu.jdbc.exception.CommonException;

/**
 * 数据源连接池工厂
 *
 * @author dongyushuai
 */
public class DataSourcePoolFactory {

    private DataSourcePoolFactory() {
    }

    public static DataSourcePoolWrapper create(final String className) {
        try {
            return (DataSourcePoolWrapper) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            throw new CommonException(QueryConstants.Error.ERROR_POOL_FACTORY_LOAD_CLASS, ex);
        }
    }
}
