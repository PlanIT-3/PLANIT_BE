package woojooin.planit.global.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan(basePackages = {
        "woojooin.planit.domain.account.api.controller",
        "woojooin.planit.domain.member.api.controller",
	"woojooin.planit.global.security.controller",
	"woojooin.planit.domain.openAi.controller",
	"woojooin.planit.domain.goal.isa.controller",
	"woojooin.planit.domain.goal.goalAccount.controller",
        "woojooin.planit.domain.goal.api.controller",
	"woojooin.planit.domain.goal.deposit.controller",
	"woojooin.planit.domain.product.controller",
	"woojooin.planit.domain.tax.controller",  // tax 컨트롤러 추가
	"woojooin.planit.domain.report.controller",
	"woojooin.planit.global.exception",
	"woojooin.planit.domain.rebalance.controller",
	"woojooin.planit.global.controller",  // FCM 컨트롤러 추가
})
public class ServletConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/resources/**")
			.addResourceLocations("/resources/");

		// Swagger UI 리소스를 위한 핸들러 설정
		registry.addResourceHandler("/swagger-ui.html")
			.addResourceLocations("classpath:/META-INF/resources/");
		// Swagger WebJar 리소스 설정
		registry.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");
		// Swagger 리소스 설정
		registry.addResourceHandler("/swagger-resources/**")
			.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/v2/api-docs")
			.addResourceLocations("classpath:/META-INF/resources/");
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();

		bean.setViewClass(JstlView.class);
		bean.setPrefix("/WEB-INF/views/");
		bean.setSuffix(".jsp");

		registry.viewResolver(bean);
	}
}
