# JDBC工具包

## 一、简介

### 1.1概述

提供基于`JDBC`的基本CRUD封装。

### 1.2坐标

```xml
<dependency>
    <groupId>com.fixiu</groupId>
    <artifactId>jdbc-utils</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

### 1.3 特性

- 支持多种连接类型：`MySQL`、`Oracle`、`SqlServer`、`TiDB`和`Hive`
- 支持多种连接池：`C3P0`、`Dbcp2`、`HikariCP`和`Druid`
- 支持CRUD操作

### 1.4 注意

- 使用对应数据源时，请自助在使用的服务中添加对应数据源的驱动

## 二、使用

### 2.1查询

#### 2.1.1 使用查询器构建工厂类构建查询器

- 查询器构建工厂类 : `com.fixiu.jdbc.QueryFactory`

- 查询器类：`com.fixiu.jdbc.Query`

- 查询器构建参数说明

  |   参数名称    | 参数类型 |         是否必须         | 说明          |
  | :-----------: | :------: | :----------------------: | :------------ |
  |  driverClass  |  String  |            是            | 驱动类        |
  | datasourceUrl |  String  |            是            | 数据源连接URL |
  |   username    |  String  |            是            | 用户名        |
  |   password    |  String  |            是            | 密码          |
  |    dbType     |   enum   |            是            | 数据库类型    |
  |  dbPoolType   |   enum   |            是            | 连接池类型    |
  |    options    |   Map    | 连接池类型为`NO`时非必须 | 连接池参数    |

#### 2.1.2 查询器支持的操作

- `Connection getJdbcConnection()` : 获取JDBC连接
- `Map query(sqlText)` : 获取**可执行SQL**执行结果，结果返回一个`Map`，`Map`中包含一个值为`DATA`的`Key`，对应的`Value`是一个列表，列表中的每一个元素为一个`Map`，该`Map`中的`Key`为查询列，值为每一行记录对应列的值。
- `Map query(sqlText, pageStatement)` : 分页获取**可执行SQL**执行结果，`pageStatement`为分页参数
- `long queryCount(sqlText)`: 获取**可执行SQL**的结果数量
- `String countSql(sqlText)` : 将**可执行SQL**转换为`COUNT SQL`
- `String pageSql(sqlText, sqlPageStatement)` : 将**可执行SQL**转换为分页`SQL`，会自动判断当前数据库类型，按照数据库类型生成不同的`SQL`

#### 2.1.3 查询器的使用

```java
DatasourceStatement ds = new DatasourceStatement(
		"FIXIU-TEST", 
		"com.mysql.cj.jdbc.Driver",
		"jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC",
		"root", 
		"123456", 
		"com.fixiu.jdbc.query.MySqlQueryer", 
		"com.fixiu.jdbc.dbpool.HikariCPDataSourcePool");

// 1. initialization Query or Update instance
Query query = QueryFactory.create(ds);
// --------------OR--------------

Map<String, Object> options = new HashMap<>();
// Argument example
options.put("database_conn_pool_arg_key", "database_conn_pool_arg_value");

Query query = QueryFactory.create(
		"FIXIU-TEST", 
		"com.mysql.cj.jdbc.Driver", 
		"jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC",
		"root", 
		"123456", 
		DatabaseTypeEnum.MYSQL, 
		DBPoolTypeEnum.HIKARI, 
		options);
// 2. Execute SQL and get results
Map<String, Object> result = query.query("SELECT * FROM yours_table");
```

### 2.2 增删改

#### 2.2.1 构建增删改执行器对象：

1. 增删改执行器类 : `com.fixiu.jdbc.Update`

2. 增删改执行器构建参数说明

   |   参数名称    | 参数类型 |         是否必须         | 说明          |
   | :-----------: | :------: | :----------------------: | :------------ |
   |  driverClass  |  String  |            是            | 驱动类        |
   | datasourceUrl |  String  |            是            | 数据源连接URL |
   |   username    |  String  |            是            | 用户名        |
   |   password    |  String  |            是            | 密码          |
   |    dbType     |   enum   |            是            | 数据库类型    |
   |  dbPoolType   |   enum   |            是            | 连接池类型    |
   |    options    |   Map    | 连接池类型为`NO`时非必须 | 连接池参数    |

#### 2.2.2 增删改执行器支持的操作

- `Connection getJdbcConnection()` : 获取JDBC连接
- `executeAndClose(sqlText)` : 执行增删改`SQL`并且自动提交，关闭连接
- `executeAndClose(sqlTexts)` : 批量执行增删改`SQL`并且自动提交，关闭连接
- `execute(sqlText)` : 执行增删改`SQL`，返回JDBC连接，并且不提交
- `execute(sqlTexts)` : 批量执行增删改`SQL`，返回JDBC连接，并且不提交
- `commitAndClose()` : 提交并且关闭连接

### 2.2.3 增删改执行器的使用

```java
DatasourceStatement ds = new DatasourceStatement(
		"FIXIU-TEST", 
		"com.mysql.cj.jdbc.Driver",
		"jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC",
		"root", 
		"123456", 
		"com.fixiu.jdbc.query.MySqlQueryer", 
		"com.fixiu.jdbc.dbpool.HikariCPDataSourcePool");

// 1. initialization Query or Update instance
Update update = UpdateFactory.create(ds);
// --------------OR--------------

Map<String, Object> options = new HashMap<>();
// Argument example
options.put("database_conn_pool_arg_key", "database_conn_pool_arg_value");

Update update = UpdateFactory.create(
		"FIXIU-TEST", 
		"com.mysql.cj.jdbc.Driver", 
		"jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC",
		"root", 
		"123456", 
		DatabaseTypeEnum.MYSQL, 
		DBPoolTypeEnum.HIKARI, 
		options);
// 2. Execute SQL and get results
int result = update.executeAndClose("INSERT INTO at_jdbc(a) values('A')");
```

