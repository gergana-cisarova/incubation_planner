package com.example.incubation_planner.config;


import com.example.incubation_planner.services.impl.IncubationUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final IncubationUserService incubationUserService;
    private final PasswordEncoder passwordEncoder;

    public ApplicationSecurityConfig(IncubationUserService incubationUserService, PasswordEncoder passwordEncoder) {
        this.incubationUserService = incubationUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/", "/users/login", "/users/register",
                        "/ideas/all", "/results/*").permitAll()
                .antMatchers("/projects/archive/*", "/projects/delete/*", "/projects/update/*",
                        "/ideas/accept/*", "/ideas/delete/*", "/users/manage",
                        "/users/delete/*", "/activities/add", "/roles/add", "/statistics").hasRole("ADMIN")
                .antMatchers("/ideas/add", "/ideas/details/*",
                        "/projects/owned", "/projects/all",
                        "/projects/details/*", "/projects/update/*", "/projects/join/*", "/projects/leave/*", "/projects/publish/*",
                        "/projects/own", "/projects/api", "/projects/collaborations",
                        "/ideas/accept/*", "/ideas/delete/*", "/users/manage", "/users/delete/*", "/activities/add").hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/home")
                .failureForwardUrl("/users/login-error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(incubationUserService)
                .passwordEncoder(passwordEncoder);
    }
}