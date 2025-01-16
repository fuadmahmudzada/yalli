package org.yalli.wah.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.yalli.wah.filter.CsrfCookieFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.authorizeHttpRequests((requests) -> requests
                                .requestMatchers(HttpMethod.GET, "/v1/admins").hasAnyRole("ADMIN", "SUPER_ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.GET, "/v1/admins/{adminId}").hasAnyRole("ADMIN", "SUPER_ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.POST, "/v1/admins/").hasAnyRole("ADMIN", "SUPER_ADMIN", "MODERATOR")
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
                                .requestMatchers(HttpMethod.PUT, "v1/groups/").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "v1/groups/users/{userId}").authenticated()
                                .requestMatchers(HttpMethod.POST, "v1/groups").authenticated()
                                .requestMatchers("v1/groups").authenticated()
                                .requestMatchers(HttpMethod.POST, "v1/mentors").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "v1/mentors/{id}").authenticated()
                                .requestMatchers("v1/notifications/getAll").authenticated()
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
                .securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS).maximumSessions(21)
                                .maxSessionsPreventsLogin(true))


                .csrf(csrfConfigurer -> csrfConfigurer.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/v1/mentors/search/**", "/v1/users/login",
                                "/v1/users/register/**"
                                , "/v1/users/reset-password/**"
                                , "/v1/users/send-otp"
                                , "/v1/users/confirm")
                        .ignoringRequestMatchers("/v1/events/{id}")
                        .ignoringRequestMatchers("/v1/files/{fileName}")
//                        .ignoringRequestMatchers(
//                                "v1/groups/{id}")
//                        .ignoringRequestMatchers(
//                                "v1/groups")
//                        .ignoringRequestMatchers(
//                                "v1/mentors/{id}")
                        .ignoringRequestMatchers("/v1/admins/login")
                        .ignoringRequestMatchers("/swagger-ui/**")
                        .ignoringRequestMatchers("/swagger-config")
                        .ignoringRequestMatchers("/v3/api-docs/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .formLogin(withDefaults())
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
}
