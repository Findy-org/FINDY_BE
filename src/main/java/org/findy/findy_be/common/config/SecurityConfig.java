package org.findy.findy_be.common.config;

import java.util.Arrays;

import org.findy.findy_be.auth.oauth.filter.TokenAuthenticationFilter;
import org.findy.findy_be.auth.oauth.handler.OAuth2AuthenticationFailureHandler;
import org.findy.findy_be.auth.oauth.handler.OAuth2AuthenticationSuccessHandler;
import org.findy.findy_be.auth.oauth.handler.TokenAccessDeniedHandler;
import org.findy.findy_be.auth.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import org.findy.findy_be.auth.oauth.service.CustomOAuth2UserService;
import org.findy.findy_be.auth.oauth.service.CustomUserDetailsService;
import org.findy.findy_be.auth.oauth.token.AuthTokenProvider;
import org.findy.findy_be.auth.properties.CorsProperties;
import org.findy.findy_be.user.repository.UserRefreshTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private static final String API_PREFIX = "/api";
	private static final String ADMIN_API_PREFIX = "/api/admin";

	private final CorsProperties corsProperties;
	private final AppProperties appProperties;
	private final AuthTokenProvider tokenProvider;
	private final CustomUserDetailsService userDetailsService;
	private final CustomOAuth2UserService oAuth2UserService;
	private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
	private final UserRefreshTokenRepository userRefreshTokenRepository;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
			.requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
			.requestMatchers(new AntPathRequestMatcher("/css/**"))
			.requestMatchers(new AntPathRequestMatcher("/js/**"))
			.requestMatchers(new AntPathRequestMatcher("/img/**"))
			.requestMatchers(new AntPathRequestMatcher("/lib/**"));
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws
		Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(e -> e
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				.accessDeniedHandler(tokenAccessDeniedHandler))
			.authorizeHttpRequests(authorizeHttpRequests ->
				authorizeHttpRequests
					.requestMatchers("/favicon.ico/**", "/css/**", "/js/**", "/img/**", "/lib/**")
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, "/index.html"))
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, "/**"))
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, "/oauth2/authorization/**"))
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, API_PREFIX + "/login"))
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, API_PREFIX + "/auth/**"))
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, ADMIN_API_PREFIX + "/**"))
					.hasRole("ADMIN")
					.requestMatchers(new MvcRequestMatcher(introspector, "/findy/api-docs/**"))
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, "/actuator/**"))
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, "/grafana/**"))
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, "/findy/swagger-ui/index.html"))
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, "/findy/swagger-ui/**")) // Swagger UI 접근 허용
					.permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, "/v3/api-docs/**")) // Swagger API docs 접근 허용
					.permitAll()
					.anyRequest().authenticated())
			.oauth2Login(oauth2Configurer ->
				oauth2Configurer
					.userInfoEndpoint(userInfoEndpointConfig ->
						userInfoEndpointConfig.userService(oAuth2UserService) // UserService 설정
					)
					.successHandler(oAuth2AuthenticationSuccessHandler())
					.failureHandler(oAuth2AuthenticationFailureHandler()));

		http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter(tokenProvider);
	}

	@Bean
	public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
		return new OAuth2AuthenticationSuccessHandler(
			tokenProvider,
			appProperties,
			userRefreshTokenRepository,
			oAuth2AuthorizationRequestBasedOnCookieRepository()
		);
	}

	@Bean
	public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
		return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
	}

	@Bean
	public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
		return new OAuth2AuthorizationRequestBasedOnCookieRepository();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return new ProviderManager(provider);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
		corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
		corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
		corsConfig.setAllowCredentials(true);
		corsConfigSource.registerCorsConfiguration("/**", corsConfig);
		return corsConfigSource;
	}
}
