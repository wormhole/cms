package net.stackoverflow.cms;

import net.stackoverflow.cms.constant.DatabaseConst;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;

/**
 * 启动类
 *
 * @author 凉衫薄
 */
@SpringBootApplication
@MapperScan(basePackages = {"net.stackoverflow.cms.dao"}, sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class CmsApplication {

    public static void main(String[] args) {
        initDataBase();
        SpringApplication.run(CmsApplication.class, args);
    }

    /**
     * 初始化数据库
     */
    private static void initDataBase() {
        try {
            Properties props = Resources.getResourceAsProperties("application-prod.properties");
            String url = props.getProperty("cms.database.url");
            String username = props.getProperty("spring.datasource.username");
            String password = props.getProperty("spring.datasource.password");
            String driver = props.getProperty("spring.datasource.driver-class-name");

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(DatabaseConst.CHECK_DB_EXIST_SQL);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(DatabaseConst.COUNT_COLUMN) == 0) {
                ScriptRunner runner = new ScriptRunner(conn);
                runner.setErrorLogWriter(null);
                runner.setLogWriter(null);
                Resources.setCharset(StandardCharsets.UTF_8);
                runner.runScript(Resources.getResourceAsReader(DatabaseConst.SQL_PATH));
            }
            ps.close();
            conn.close();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
