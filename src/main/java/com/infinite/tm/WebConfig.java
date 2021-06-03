package com.infinite.tm;

import org.apache.catalina.filters.RemoteIpFilter;
import org.apache.catalina.valves.RemoteHostValve;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
@SuppressWarnings("deprecation")
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/login").setViewName("security/login");

	}
	
	
	@Bean
	public TemplateResolver templateResolver() {
				 
	     ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
	        resolver.setPrefix("templates/");
	        return resolver;
	     
//		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//		resolver.setPrefix("templates/");
//		return templateResolver;

	}
	
	@Bean
	public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }
	
		
	@Bean
	public RemoteHostValve remoteHostValve() {
		return new RemoteHostValve();
	}

}
