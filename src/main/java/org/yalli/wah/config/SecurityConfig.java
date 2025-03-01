package org.yalli.wah.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.yalli.wah.filter.CsrfCookieFilter;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
        csrfTokenRepository.setCookieCustomizer(responseCookieBuilder -> responseCookieBuilder.httpOnly(false).sameSite("none").secure(true));

        http.authorizeHttpRequests((requests) -> requests
                                .requestMatchers(HttpMethod.GET, "/v1/admins").hasAnyRole("ADMIN", "SUPER_ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.GET, "/v1/admins/{adminId}").hasAnyRole("ADMIN", "SUPER_ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.POST, "/v1/admins").hasAnyRole("ADMIN", "SUPER_ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.PUT, "/v1/admins/{adminId}").hasAnyRole("ADMIN", "SUPER_ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.PATCH, "/v1/admins/{adminId}").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/v1/admins/{adminId}").hasRole("SUPER_ADMIN")
                                .requestMatchers("/v1/admins/add-notification", "/v1/admins/create-group", "/v1/admins/delete-group"
                                        , "/v1/admins/events/**", "/v1/admins/update-group/").hasAnyRole("ADMIN", "SUPER_ADMIN", "MODERATOR")
                                .requestMatchers("/v1/users/delete/{id}", "v1/users/search/**").authenticated()
                                .requestMatchers("v1/comments").authenticated()
                                .requestMatchers("v1/events/saveEvent", "v1/events/unsaveEvent").authenticated()
                                .requestMatchers("v1/files/upload").authenticated()
                                .requestMatchers(HttpMethod.GET, "v1/groups/users/{userId}").authenticated()
                                .requestMatchers("v1/groups/{groupId}/users/{userId}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "v1/groups").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "v1/groups/users/{userId}").authenticated()
                                .requestMatchers(HttpMethod.POST, "v1/groups").authenticated()
                                .requestMatchers(HttpMethod.POST, "v1/mentors").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "v1/mentors/{id}").authenticated()
                                .requestMatchers("v1/notifications/getAll").authenticated()
                                .requestMatchers("/api/loginSuccess", "/api/user").authenticated()
                                .requestMatchers("v1/user/user-info", "/", "/error").permitAll()
                                .requestMatchers("v1/experiences/comments/{link}").authenticated()
                                .requestMatchers(HttpMethod.POST, "v1/experience").authenticated()
//                                .requestMatchers("/oauth2/**").permitAll()
//                                .requestMatchers("/oauth/**").permitAll()
//                                .requestMatchers("/v1/users/login").authenticated()
//                                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
//                                .requestMatchers("v1/users/login/oauth2/code/google").permitAll()
//                                .requestMatchers("/v1/users/login/oauth2/code/google", "/error").permitAll()
//                                .requestMatchers("v1/users/print-user-name").authenticated()
//                                .requestMatchers("v1/users/secure").authenticated()
//
//                                .requestMatchers("/loginFailure").permitAll()
//                                .requestMatchers("/user").authenticated()
//
//                                .requestMatchers("/loginFailure").permitAll()
//                                .requestMatchers("/user").authenticated()
//                                .requestMatchers("/", "/error", "/webjars/**", "/login/**",  "/login?error=true").permitAll() // Allow access to essential resources
                                .anyRequest().permitAll()

//                        .requestMatchers("/v1/mentors/search/**", "v1/users/login",
//                                "/v1/users/register/**"
//                                , "v1/users/reset-password/**"
//                                , "v1/users/send-otp"
//                                , "v1/users/confirm").permitAll()
//                        .requestMatchers("v1/events/{id}").permitAll()
//                        .requestMatchers("v1/files/{fileName}").permitAll()
//                        .requestMatchers(HttpMethod.GET, "v1/groups/{id}").permitAll()
//                        .requestMatchers(HttpMethod.GET, "v1/groups").permitAll()
//                        .requestMatchers(HttpMethod.GET, "v1/mentors/{id}").permitAll()
//                        .requestMatchers("/v1/admins/login").permitAll()
//                        .requestMatchers("/swagger-ui/**").permitAll()
//                        .requestMatchers("/swagger-config").permitAll()
//                        .requestMatchers("/v3/api-docs/**").permitAll()

                )
                .logout(logoutConfigurer -> logoutConfigurer.logoutUrl("/v1/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("SESSIONID", "XSRF-TOKEN")
                )
                .securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .sessionFixation().migrateSession()
                )
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOriginPatterns(Collections.singletonList("*"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization", "X-CSRF-TOKEN"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .csrf(csrfConfigurer -> csrfConfigurer.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/v1/mentors/search/**", "/v1/users/login",
                                "/v1/users/register/**"
                                , "/v1/users/reset-password/**"
                                , "/v1/users/send-otp"
                                , "/v1/users/confirm")
                        .ignoringRequestMatchers("/v1/events/{id}")
                        .ignoringRequestMatchers(request -> request.getMethod().equals(HttpMethod.GET.name())
                                && request.getRequestURI().startsWith("/v1/events/"))
                        .ignoringRequestMatchers("/v1/files/{fileName}")
                        .ignoringRequestMatchers("/v1/admins/login")
                        .ignoringRequestMatchers("/swagger-ui/**")
                        .ignoringRequestMatchers("/oauth2/**", "/login/**")
                        .ignoringRequestMatchers("/swagger-config")
                        .ignoringRequestMatchers("/v3/api-docs/**")
                        .ignoringRequestMatchers("/api/loginSuccess")
                        .ignoringRequestMatchers("/api/user").ignoringRequestMatchers("/v1/users/map/coordinates")
                        .csrfTokenRepository(csrfTokenRepository))


                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/", true)
                )

                .formLogin(
                        httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginProcessingUrl("v1/users/login")
                                .successHandler((request, response, authentication) -> response.setStatus(HttpStatus.NO_CONTENT.value()))
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .httpBasic(withDefaults());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        ProjectAuthenticationProvider authenticationProvider =
                new ProjectAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(true);
        return providerManager;
    }

    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofStrict();
    }


}
