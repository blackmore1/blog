package com.demon4u.blog.security.config;

import com.demon4u.blog.constant.AppType;
import com.demon4u.blog.constant.RoleType;
import com.demon4u.blog.security.dao.RoleDao;
import com.demon4u.blog.security.dao.UserDao;
import com.demon4u.blog.security.entity.RoleEntity;
import com.demon4u.blog.security.entity.UserEntity;
import com.demon4u.blog.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.*;

@EnableOAuth2Client
@EnableAuthorizationServer
@Order(6)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 如果有允许匿名的url，填在下面
                .anyRequest().permitAll()
//                .anyRequest().authenticated()
                .and()
                // 设置登陆页
                .formLogin().loginPage("/login")
//                .loginProcessingUrl("/doLogin")
                // 设置登陆成功页
//                .defaultSuccessUrl("/")
                .permitAll()
                // 自定义登陆用户名和密码参数，默认为username和password
//                .usernameParameter("username")
//                .passwordParameter("password")
                .and()
                .logout().permitAll()
                .and().rememberMe().userDetailsService(userDetailsService).tokenRepository(persistentTokenRepository())
                .and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                ;

        // 关闭CSRF跨域
        http.csrf().disable();
        http.headers().frameOptions().disable();//后台无法访问
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        //去掉";"黑名单
        firewall.setAllowSemicolon(true);
        //加入自定义的防火墙
        web.httpFirewall(firewall);
        super.configure(web);
    }
    /**
     * 持久化token
     *
     * Security中，默认是使用PersistentTokenRepository的子类InMemoryTokenRepositoryImpl，将token放在内存中
     * 如果使用JdbcTokenRepositoryImpl，会创建表persistent_logins，将token持久化到数据库
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource); // 设置数据源
//        tokenRepository.setCreateTableOnStartup(true); // 启动创建表，创建成功后注释掉
        return tokenRepository;
    }

    //github=============================================
    @Autowired
    OAuth2ClientContext oauth2ClientContext;
    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
    @Bean
    @ConfigurationProperties("github")
    public ClientResources github() {
        return new ClientResources();
    }
    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(github(), "/github/callback"));
        filter.setFilters(filters);
        return filter;
    }

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter = new OAuth2ClientAuthenticationProcessingFilter(path);
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(client.getResource().getUserInfoUri(),
                client.getClient().getClientId());
        tokenServices.setRestTemplate(oAuth2RestTemplate);
        tokenServices.setPrincipalExtractor(map -> {
            String username = (String) map.get("login");
            String appId = map.get("id").toString();
            UserEntity user = userDao.findByAppTypeAndAppId(AppType.GITHUB, appId);
            if (user == null) {
                user = new UserEntity();
                user.setUsername(AppType.GITHUB +"_" + appId);
                user.setNickname(username);
                user.setPassword(generatePwd(10));
                user.setAppType(AppType.GITHUB);
                user.setAppId(appId);
                user.setAvatar((String) map.get("avatar_url"));
                user.setCreateTime(new Date());
                userDao.save(user);
            }
            List<RoleEntity> roles = roleDao.findByUserId(user.getId());
//            if (CollectionUtils.isEmpty(roles)) {
//                roles.add(new RoleEntity(user.getId(), RoleType.ROLE_USER));
//            }
            user.setAuthorities(roles);
            return user;
        });
        tokenServices.setAuthoritiesExtractor(map -> {
            String appId = map.get("id").toString();
            UserEntity user = userDao.findByAppTypeAndAppId(AppType.GITHUB, appId);
            List<RoleEntity> roles = roleDao.findByUserId(user.getId());
            List<GrantedAuthority> list = new ArrayList<>();
            for (RoleEntity role : roles) {
                list.add((GrantedAuthority) role::getName);
            }
            if (list.isEmpty()) {
                list.add((GrantedAuthority) () -> RoleType.ROLE_USER);
            }
            return list;
        });
        oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);
        oAuth2ClientAuthenticationFilter.setRememberMeServices(rememberMeServices());
        return oAuth2ClientAuthenticationFilter;
    }

    private RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(), userDetailsService, persistentTokenRepository());
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    private String generatePwd(Integer length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val.append(String.valueOf(random.nextInt(10)));
        }
        return val.toString();
    }
}

class ClientResources {

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();

    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }
}