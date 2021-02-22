package com.demon4u.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:conf/appConfig.properties"})
public class AppConfig {
    public static String domain;

    @Value("${domain}")
    public void setDomain(String domain) {
        AppConfig.domain = domain;
    }
}
