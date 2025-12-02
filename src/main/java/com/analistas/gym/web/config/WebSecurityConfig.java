package com.analistas.gym.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home", "login", "/css/**","/js/**","/img/**").permitAll()
				.anyRequest().authenticated())
			.formLogin((form) -> form
				.loginPage("/login")
				.defaultSuccessUrl("/home", true)
				.permitAll())
			.logout((logout) -> logout.permitAll()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login"));

		return http.build();
	}

    @Bean
	public UserDetailsService userDetailsService() {
	    		UserDetails userAdmin = User.withDefaultPasswordEncoder()
				.username("ale")
				.password("velazco")
				.roles("ADMIN")
				.build();

                UserDetails user = User.withDefaultPasswordEncoder()
				.username("sad")
				.password("satan")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(userAdmin, user);
	}

}
