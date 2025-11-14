package woojooin.planit.global.config.datasource;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import woojooin.planit.global.enums.DataSourceType;

@Aspect
@Component
public class DataSourceAspect {

	@Before("@annotation(woojooin.planit.global.config.datasource.ReadOnly)")
	public void setReadDataSource() {
		DataSourceContextHolder.set(DataSourceType.SLAVE);
	}

	@After("@annotation(woojooin.planit.global.config.datasource.ReadOnly)")
	public void clearDataSource() {
		DataSourceContextHolder.clear();
	}
}
