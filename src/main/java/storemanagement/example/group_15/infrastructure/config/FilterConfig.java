package storemanagement.example.group_15.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import storemanagement.example.group_15.app.middleware.JwtAuthenticationFilter;
import storemanagement.example.group_15.filters.AuthorizationFilter;

@Configuration
public class FilterConfig {
    @Autowired
    private  JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private  AuthorizationFilter authorizationFilter;


    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.addUrlPatterns("/users/*");
        registrationBean.addUrlPatterns("/favorites/*");
        registrationBean.addUrlPatterns("/rating/*");
        registrationBean.addUrlPatterns("/carts/*");
        registrationBean.addUrlPatterns("/orders/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilterRegistration() {
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authorizationFilter);
        registrationBean.addUrlPatterns("/admin/*");
        registrationBean.addUrlPatterns("/users/*");
        registrationBean.addUrlPatterns("/favorites/*");
        registrationBean.addUrlPatterns("/rating/*");
        registrationBean.addUrlPatterns("/carts/*");
        registrationBean.addUrlPatterns("/orders/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
