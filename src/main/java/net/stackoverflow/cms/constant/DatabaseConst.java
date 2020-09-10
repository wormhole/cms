package net.stackoverflow.cms.constant;

/**
 * 数据库相关常量
 *
 * @author 凉衫薄
 */
public class DatabaseConst {

    public static final String CHECK_DB_EXIST_SQL = "SELECT count(SCHEMA_NAME) as COUNT FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='cms'";

    public static final String COUNT_COLUMN = "COUNT";

    public static final String SQL_PATH = "sql/cms.sql";
}
