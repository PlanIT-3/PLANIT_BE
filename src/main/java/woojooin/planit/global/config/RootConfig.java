package woojooin.planit.global.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:/application.properties"})
@ComponentScan(basePackages = {"woojooin.planit"},
<<<<<<< Updated upstream
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class)
    })
@MapperScan(basePackages  = {
    "woojooin.planit.domain.member.mapper",
    "woojooin.planit.domain.account.mapper",  // Account mapper 추가
    "woojooin.planit.domain.goal.isa.mapper",  // ISA 계좌 mapper 추가
    "woojooin.planit.domain.goal.deposit.mapper",  // Deposit mapper 추가
    "woojooin.planit.domain.product.mapper",
    "woojooin.planit.domain.goal.mapper"  // Goal mapper 추가
=======
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class)
        })
@MapperScan(basePackages = {
        "woojooin.planit.domain.member.mapper",
        "woojooin.planit.domain.goal.isa.mapper",  // ISA 계좌 mapper 추가
        "woojooin.planit.domain.goal.deposit.mapper",  // Deposit mapper 추가
        "woojooin.planit.domain.product.mapper",
        "woojooin.planit.domain.goal.mapper", // Goal mapper 추가
        "woojooin.planit.domain.goal.goalAccount.mapper"  // GoalAccount mapper 추가
>>>>>>> Stashed changes
})
@Slf4j
@EnableTransactionManagement
public class RootConfig {
    @Value("${jdbc.driver}")
    String driver;
    @Value("${jdbc.url}")
    String url;
    @Value("${jdbc.username}")
    String username;
    @Value("${jdbc.password}")
    String password;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setConfigLocation(
                applicationContext.getResource("classpath:/mybatis-config.xml"));
        sqlSessionFactory.setDataSource(dataSource());

        sqlSessionFactory.setMapperLocations(
                applicationContext.getResources("classpath:/mapper/**/*.xml"));

        return (SqlSessionFactory) sqlSessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource());
        return manager;
    }
}