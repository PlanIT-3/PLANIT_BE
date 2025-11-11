package woojooin.planit.global.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;
import woojooin.planit.global.config.datasource.RoutingDataSource;
import woojooin.planit.global.enums.DataSourceType;

@Configuration
@PropertySource({"classpath:/application.properties"})
@ComponentScan(basePackages = {"woojooin.planit"},
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class)
	})
@MapperScan(basePackages = {
	"woojooin.planit.domain.member.mapper",
	"woojooin.planit.domain.product.mapper",            // product mapper 추가
	"woojooin.planit.domain.goal.mapper",                // Goal mapper 추가
	"woojooin.planit.domain.account.mapper",            // Account mapper 추가
	"woojooin.planit.domain.openAi.mapper",                // OpenAI mapper 추가
	"woojooin.planit.domain.report.mapper",            // report mapper 추가
	"woojooin.planit.domain.tax.mapper",                // tax mapper 추가
	"woojooin.planit.domain.rebalance.mapper",            // rebalance mapper 추가
	"woojooin.planit.global.fcm.mapper"                    //fcm 추가
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

	@Value("${jdbc.master.url}")
	private String masterUrl;
	@Value("${jdbc.master.username}")
	private String masterUsername;
	@Value("${jdbc.master.password}")
	private String masterPassword;
	@Value("${jdbc.slave.url}")
	private String slaveUrl;
	@Value("${jdbc.slave.username}")
	private String slaveUsername;
	@Value("${jdbc.slave.password}")
	private String slavePassword;

	@Bean(name = "masterDataSource")
	public DataSource masterDataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(driver);
		config.setJdbcUrl(masterUrl);
		config.setUsername(masterUsername);
		config.setPassword(masterPassword);

		log.info("id = {} password = {}", masterUsername, masterPassword);
		return new HikariDataSource(config);
	}

	@Bean(name = "slaveDataSource")
	public DataSource slaveDataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(driver);
		config.setJdbcUrl(slaveUrl);
		config.setUsername(slaveUsername);
		config.setPassword(slavePassword);
		log.info("id={} password={}", slaveUsername, slavePassword);
		return new HikariDataSource(config);
	}

	@Bean
	public DataSource routingDataSource() {
		RoutingDataSource routingDataSource = new RoutingDataSource();
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceType.MASTER, masterDataSource());
		targetDataSources.put(DataSourceType.SLAVE, slaveDataSource());
		routingDataSource.setTargetDataSources(targetDataSources);
		routingDataSource.setDefaultTargetDataSource(masterDataSource());
		return routingDataSource;
	}

	@Autowired
	ApplicationContext applicationContext;

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setConfigLocation(
			applicationContext.getResource("classpath:/mybatis-config.xml"));
		sqlSessionFactory.setMapperLocations(
			applicationContext.getResources("classpath:/mapper/**/*.xml"));
		sqlSessionFactory.setDataSource(routingDataSource());

		return (SqlSessionFactory)sqlSessionFactory.getObject();
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager manager = new DataSourceTransactionManager(routingDataSource());
		return manager;
	}
}