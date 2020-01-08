package com.demon4u.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	@Autowired
	private TimeInterceptor timeInterceptor;

	/**
	 * 使TimeInterceptor生效，并移除静态资源的timelog统计
	 * */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(timeInterceptor).excludePathPatterns(Arrays.asList("/css/**", "/editor.md/**",
				"/js/**", "/images/**", "/webfonts/**", "/X-admin/**", "/**/*.ico"));
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
