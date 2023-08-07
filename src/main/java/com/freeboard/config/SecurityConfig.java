package com.freeboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeboard.config.filter.CustomEmailPasswordFilter;
import com.freeboard.config.handler.Http401Handler;
import com.freeboard.config.handler.Http403Handler;
import com.freeboard.config.handler.LoginFailureHandler;
import com.freeboard.config.handler.LoginSuccessHandler;
import com.freeboard.domain.user.User;
import com.freeboard.domain.user.UserPrincipal;
import com.freeboard.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers("/favicon.ico")
			.requestMatchers("/error");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests()
			.anyRequest().permitAll()
			.and()
			.logout()
			.logoutUrl("/api/auth/logout")
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
			.and()
			.addFilterBefore(customEmailPasswordFilter(), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(e -> {
				e.accessDeniedHandler(new Http403Handler(objectMapper));
				e.authenticationEntryPoint(new Http401Handler(objectMapper));
			})
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> {
			User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));

			return new UserPrincipal(user);
		};
	}

	@Bean
	public CustomEmailPasswordFilter customEmailPasswordFilter() {
		CustomEmailPasswordFilter filter = new CustomEmailPasswordFilter(objectMapper);
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
		filter.setAuthenticationFailureHandler(new LoginFailureHandler(objectMapper));
		filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
		return filter;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService(userRepository));
		provider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(provider);
	}
}
