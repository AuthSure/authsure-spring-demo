package com.authsure.demo;

import com.authsure.client.AuthSureClient;
import com.authsure.spring.security.authentication.AuthSureAuthenticationProvider;
import com.authsure.spring.security.web.AuthSureAuthenticationEntryPoint;
import com.authsure.spring.security.web.AuthSureAuthenticationFilter;
import com.authsure.spring.security.web.AuthSureLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration for application.
 *
 * @author Erik R. Jensen
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${authsure.url}")
  private String url;

  @Value("${authsure.flow}")
  private String flow;

  @Value("${authsure.username}")
  private String username;

  @Value("${authsure.password}")
  private String password;

  @Bean
  public AuthSureClient authSureClient() {
    return new AuthSureClient(url, username, password);
  }

  @Bean
  public AuthSureAuthenticationEntryPoint authSureAuthenticationEntryPoint() {
    return new AuthSureAuthenticationEntryPoint(authSureClient(), flow);
  }

  @Bean
  public AuthSureLogoutSuccessHandler authSureLogoutSuccessHandler() {
    return new AuthSureLogoutSuccessHandler(authSureClient(), flow);
  }

  @Bean
  public AuthSureAuthenticationFilter authSureAuthenticationFilter() throws Exception {
    AuthSureAuthenticationFilter filter = new AuthSureAuthenticationFilter(authSureClient(), flow);
    filter.setAuthenticationManager(authenticationManager());
    return filter;
  }

  @Bean
  public AuthSureAuthenticationProvider authSureAuthenticationProvider() {
    return new AuthSureAuthenticationProvider(authSureClient());
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authSureAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .addFilterAfter(
            authSureAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(authSureAuthenticationEntryPoint())
        .and()
        .logout()
        .logoutSuccessHandler(authSureLogoutSuccessHandler())
        // This was done because we don't want to complicate the demo with CSRF forcing a
        // POST on logout See
        // http://docs.spring.io/spring-security/site/docs/3.2.4.RELEASE/reference/htmlsingle/
        // #csrf-logout
        // See https://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .and()
        // Protect some resources so we can demo
        .authorizeRequests()
        .antMatchers("/protected").authenticated()
        .antMatchers("/another_protected").authenticated();
  }

}
