package com.app.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.blog.service.UserAuthService;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAuthService userAuthService;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;
	
    @Override
    public void configure(WebSecurity web) throws Exception {
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
    	// auth.userDetailsService(userAuthService).passwordEncoder(passwordEncoder());
    	auth.userDetailsService(userAuthService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http
    	.csrf()
    	.disable()
    	.cors()
    	.disable()
    	.authorizeRequests()
    	.antMatchers("/login/","/register",
    			"/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**")
    	.permitAll()
    	//.antMatchers("/api/auth/consumer/cart").hasRole("USER")
    	.anyRequest()
    	.authenticated()
    	.and()
    	.sessionManagement()
    	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    	.and()
    	.exceptionHandling()
    	.authenticationEntryPoint(apiAuthenticationEntryPoint);
    	
    	http
    	.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtAuthFilterRegister(JwtFilter filter) {
    	FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    // added
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return NoOpPasswordEncoder.getInstance();
    	// return new BCryptPasswordEncoder();
    }

}
