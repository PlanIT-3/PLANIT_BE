package woojooin.planit.global.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
	@Bean(initMethod = "migrate")
	public Flyway flyway(DataSource dataSource) {
		Flyway flyway = Flyway.configure()
			.dataSource(dataSource)
			.locations("classpath:db/migration")
			.baselineOnMigrate(true)
			.load();

		// ❗ repair 먼저 실행하고
		flyway.repair();

		// ✅ 그 다음 migrate 정상 실행
		return flyway;
	}
}
