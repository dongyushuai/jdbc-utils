package com.fixiu.jdbc.constant;

/**
 * 数据库类型
 *
 * @author dongyushuai
 */
public enum DatabaseTypeEnum {
    /**
     * MySQL
     */
    MYSQL(QueryConstants.Datasource.DB_MYSQL),

    /**
     * SqlServer
     */
    SQLSERVER(QueryConstants.Datasource.DB_MSSQL),

    /**
     * Oracle
     */
    ORACLE(QueryConstants.Datasource.DB_ORACLE),

    /**
     * TiDB
     */
    TIDB(QueryConstants.Datasource.DB_TIDB),

    /**
     * Hive
     */
    HIVE(QueryConstants.Datasource.DB_HIVE);

    private final String value;

    DatabaseTypeEnum(final String value) {
        this.value = value;
    }

    public static DatabaseTypeEnum of(String arg) {
        return valueOf2(arg);
    }

    public static DatabaseTypeEnum valueOf2(String arg) {
        switch (arg) {
            case QueryConstants.Datasource.DB_MSSQL:
                return SQLSERVER;
            case QueryConstants.Datasource.DB_ORACLE:
                return ORACLE;
            case QueryConstants.Datasource.DB_TIDB:
            case QueryConstants.Datasource.DB_MYSQL:
            default:
                return MYSQL;
        }
    }

    public static boolean isInEnum(String value) {
        for (DatabaseTypeEnum databaseType : DatabaseTypeEnum.values()) {
            if (databaseType.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public String getValue() {
        return this.value;
    }
}
