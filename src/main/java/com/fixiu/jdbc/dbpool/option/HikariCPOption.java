package com.fixiu.jdbc.dbpool.option;

/**
 * HikariCP连接池参数属性
 *
 * @author dongyushuai
 */
public class HikariCPOption {
	public static final String IS_ALLOW_POOL_SUSPENSION = "isAllowPoolSuspension";
	public static final String CATALOG = "catalog";
	public static final String CONNECTION_INIT_SQL = "connectionInitSql";
	public static final String CONNECTION_TEST_QUERY = "connectionTestQuery";
	public static final String CONNECTION_TIMEOUTMS = "connectionTimeoutMs";
	public static final String CLASSNAME = "className";
	public static final String EXCEPTION_OVERRIDE_CLASSNAME = "exceptionOverrideClassName";
	public static final String IDLE_TIMEOUTMS = "idleTimeoutMs";
	public static final String INITIALIZATION_FAIL_TIMEOUT = "initializationFailTimeout";
	public static final String ISOLATE = "isolate";
	public static final String LEAKD_ETECTION_THRESHOLDMS = "leakDetectionThresholdMs";
	public static final String MAX_POOL_SIZE = "maxPoolSize";
	public static final String MAX_LIFETIMEMS = "maxLifetimeMs";
	public static final String MIN_IDLE = "minIdle";
	public static final String POOL_NAME = "poolName";
	public static final String READ_ONLY = "readOnly";
	public static final String SCHEMA = "schema";
	public static final String ISOLATION_LEVEL = "isolationLevel";
	public static final String VALIDATIONT_IMEOUTMS = "validationTimeoutMs";

}