package edu.iastate.coms309.cyschedulebackend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SpringbootSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final
    AuthenticationProvider authenticationProvider;

    public SpringbootSecurityConfiguration(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.formLogin()// 表单登录  来身份认证
                .loginPage("/login.html")// 自定义登录页面
                .and()
                .authorizeRequests()// 对请求授权
                .antMatchers(new String[]{
                        "/api/v1/getSalt",
                        "/api/v1/register"
                }).permitAll()// 这些页面不需要身份认证,其他请求需要认证
                .anyRequest() // 任何请求
                .authenticated()//; // 都需要身份认证
                .and()
                .csrf().disable();// 禁用跨站攻击
    }
}
