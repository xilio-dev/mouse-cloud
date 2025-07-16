package cn.xilio.mouse.cloud.shardingsphere.javaapi;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JavaAPITest {
    public static void main(String[] args) throws SQLException, IOException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();// 构建真实数据源
        HikariDataSource ds1 = new HikariDataSource();
        ds1.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/ds1?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        ds1.setUsername("root");
        ds1.setPassword("123456");
        dataSourceMap.put("ds1", ds1);
       // HikariDataSource ds2 = new HikariDataSource();

        Collection<RuleConfiguration> ruleConfigs = new ArrayList<>();// 构建具体规则
        Properties props = new Properties(); // 构建属性配置
        DataSource dataSource = ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, ruleConfigs, props);


        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from user");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1)+": "+resultSet.getString(2));
        }
        System.out.println(connection);

    }
}
