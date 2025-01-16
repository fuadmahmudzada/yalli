package org.yalli.wah.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class CsrfCookieFilter extends OncePerRequestFilter {
    private final List<String> securedEndpoints = List.of("/v1/mentors/search/**",
            "/v1/users/register/**"
            , "/v1/users/reset-password/**"
            , "/v1/users/send-otp"
            , "/v1/users/confirm"
            , "/v1/users/search"
            , "/v1/events"
            , "/v1/files/{fileName}"
            , "/v1/admins/login");


    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isSecuredApi = securedEndpoints.stream().anyMatch(api -> antPathMatcher.match(api, request.getRequestURI()));

        if (isSecuredApi) {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            if (csrfToken != null) {
                csrfToken.getToken();
            }

        }
        filterChain.doFilter(request, response);


    }
}
