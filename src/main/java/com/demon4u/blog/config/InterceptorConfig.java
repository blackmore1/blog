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
				"/js/**", "/images/**", "/webfonts/**", "/X-admin/**", "/**/*.ico", AppConfig.FILE_STATIC_PATH + "/**", AppConfig.PREVIEW_URL + "/**", AppConfig.DOWNLOAD_PRE + "/**"));
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	/**
	 * 添加外部资源
	 * */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//addResourceHandler是指你想在url请求的路径
		//addResourceLocations是图片存放的真实路径 末尾必须有/
		String location = "file:" + AppConfig.globalPath + (AppConfig.globalPath.endsWith("/") ? "" : "/");
		registry.addResourceHandler(AppConfig.DOWNLOAD_PRE +"/**").addResourceLocations(location);
	}

}
