package com.smartstock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean UserDetailsService users(PasswordEncoder encoder){
        return new InMemoryUserDetailsManager(
            User.withUsername("admin").password(encoder.encode("Admin@123")).roles("ADMIN").build(),
            User.withUsername("manager").password(encoder.encode("Manager@123")).roles("MANAGER").build(),
            User.withUsername("sales").password(encoder.encode("Sales@123")).roles("SALES").build());
    }
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        return http.authorizeHttpRequests(a->a
            .requestMatchers("/css/**","/login","/access-denied").permitAll()
            .requestMatchers("/settings/**").hasRole("ADMIN")
            .requestMatchers("/products/new","/products/save","/products/edit/**","/products/delete/**","/products/stock/**","/suppliers/new","/suppliers/save","/suppliers/edit/**").hasAnyRole("ADMIN","MANAGER")
            .requestMatchers("/suppliers/delete/**").hasRole("ADMIN")
            .requestMatchers("/suppliers/**").hasAnyRole("ADMIN","MANAGER")
            .requestMatchers("/orders/delete/**").hasRole("ADMIN")
            .requestMatchers("/orders/new","/orders/save","/orders/edit/**").hasAnyRole("ADMIN","MANAGER","SALES")
            .anyRequest().authenticated())
            .formLogin(f->f.loginPage("/login").defaultSuccessUrl("/",true).permitAll())
            .logout(l->l.logoutSuccessUrl("/login?logout"))
            .exceptionHandling(e->e.accessDeniedPage("/access-denied"))
            .build();
    }
}
