package edu.iastate.coms309.springbootexperiment.configure;

import edu.iastate.coms309.springbootexperiment.account.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.formLogin()// 表单登录  来身份认证
                .loginPage("/login.html")// 自定义登录页面
                .and()
                .authorizeRequests()// 对请求授权
                .antMatchers("/login.html").permitAll()// 这些页面不需要身份认证,其他请求需要认证
                .anyRequest() // 任何请求
                .authenticated()//; // 都需要身份认证
                .and()
                .csrf().disable();// 禁用跨站攻击
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        @Autowired
        @Qualifier("UserDetailsService")
        UserDetailService userDetailService;
        
        auth.userDetailsService(userDetailService);
    }

}
