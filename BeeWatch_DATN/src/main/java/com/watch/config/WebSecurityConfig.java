package com.watch.config;

import com.watch.controller.CustomOAuth2UserService;
import com.watch.secutity.services.UserDetailsServiceImpl;
import com.watch.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    AccountService accountService;
    //fb gg
    @Autowired
    private CustomOAuth2UserService oauthUserService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/", "/login", "/logout", "/login/oauth2/**").permitAll();

        http.authorizeRequests().antMatchers("/admin", "/admin/beestore","/assets/admin/main/homeAdmin.html").access("hasAnyRole('ROLE_ADMIN')");

        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
        http.authorizeRequests()
                .antMatchers("/beestore/cartItem").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .loginPage("/auth/login/form")
                .defaultSuccessUrl("/beestore")
                .userInfoEndpoint()
                .userService(oauthUserService);


        http.formLogin()
                .loginPage("/auth/login/form")
                .loginProcessingUrl("/login2")
                .defaultSuccessUrl("/auth/login/success", false);

        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(1 * 24 * 60 * 60);


        http.exceptionHandling()
                .accessDeniedPage("/auth/unauthoried");

        http.logout();

        //login fb gg
        /*
         * http.oauth2Login() .loginPage("/auth/login/form")
         * .defaultSuccessUrl("/oauth2/login/success",true)
         * .failureUrl("/auth/login/error")
         * .authorizationEndpoint().baseUri("/oauth2/authorization");
         */





    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl(); // Ta lưu tạm remember me trong memory (RAM). Nếu cần mình có thể lưu trong database
        return memory;
    }
    

}