package com.wipro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	 private final JwtTokenProvider jwtTokenProvider;
	
	 @Autowired
	 private CustomUserDetailsService customUserDetailsService;

	    @Value("${spring.security.user.email}")
	    private String adminEmail;

	    @Value("${spring.security.user.password}")
	    private String adminPassword;

	    @Value("${spring.security.user.roles}")
	    private String adminRole;
	    
    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/api/public/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
    
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    
    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	 auth.inMemoryAuthentication()
         .withUser(adminEmail)
         .password(passwordEncoder().encode(adminPassword))
         .roles(adminRole);
    	
    	 auth.userDetailsService(customUserDetailsService)
         .passwordEncoder(passwordEncoder());
 }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        	http
        		.csrf().disable()
        		.headers()
        		.frameOptions().disable()
        		.and()
        		.authorizeRequests()
        		.antMatchers("/actuator/**").permitAll()
        		.antMatchers("/", "/welcome", "/admin/login", "/user/register", "/user/login").permitAll()
        		.antMatchers("/error","/public/**","/api/auth/register","/api/auth/login").permitAll()
        		.antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/api/cart/**", "/orders/**").hasAnyRole("ADMIN", "CUSTOMER")
                .antMatchers("/products/delete/{id}","/products/add","/products/addMultiple","/products/update/product/{id}").hasRole("ADMIN") 
                .antMatchers("/products/{id}","/products/all").hasAnyRole("ADMIN","CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/shopping/home", true)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new UsernamePasswordAuthenticationFilter(authenticationManager()), JwtAuthenticationFilter.class);
    
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }
}
