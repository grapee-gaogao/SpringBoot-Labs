package cn.iocoder.springboot.lab01.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
/**
 * 这个注解打开了一个开关：允许在 Controller 的方法上使用
 *
 * @PreAuthorize、@PostAuthorize、@PreFilter、@PostFilter 这四个注解。
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 配置请求地址的权限
                .authorizeRequests()
                    .antMatchers("/test/demo").permitAll() // 所有用户可访问
                    .antMatchers("/test/admin").hasRole("ADMIN") // 需要 ADMIN 角色  *语法糖，简写法，内部会自动加 ROLE_ 前缀
                    .antMatchers("/test/normal").access("hasRole('ROLE_NORMAL')") // 需要 NORMAL 角色。
                    // 其余所有请求至少需要登录
                    .anyRequest().authenticated()
                .and()
                // 设置 Form 表单登陆
                .formLogin()
//                    .loginPage("/login") // 登陆 URL 地址
                    .permitAll() // 所有用户可访问
                .and()
                // 配置退出相关
                .logout()
//                    .logoutUrl("/logout") // 退出 URL 地址
                    .permitAll(); // 所有用户可访问
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                // 使用内存中的 InMemoryUserDetailsManager——用户存在内存里，重启就没了
                inMemoryAuthentication()
                // 不使用 PasswordEncoder 密码编码器——密码明文存储
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                // 创建 admin 用户
                //Spring Security 内部会自动给角色加上 ROLE_前缀，所以实际角色是 ROLE_ADMIN
                .withUser("admin").password("admin").roles("ADMIN")
                // 创建 normal 用户
                // .and() — 返回上一级
                .and().withUser("normal").password("normal").roles("NORMAL");
    }

}
