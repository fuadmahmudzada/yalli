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
import java.util.Map;

public class CsrfCookieFilter extends OncePerRequestFilter {
    private final List<String> securedEndpoints = List.of(
            "/v1/admins",
            "/v1/admins/{adminId}",
            "/v1/admins/events/**",
            "/v1/admins/add-notification",
            "/v1/admins/create-group",
            "/v1/admins/delete-group",
            "/v1/admins/update-group/",
            "/v1/users/delete/{id}",
            "/v1/events/saveEvent",
            "/v1/events/unsaveEvent",
            "/v1/files/upload",
            "/v1/groups",
            "/v1/groups/users/{userId}",
            "/v1/groups",
            "/v1/mentors",
            "/v1/mentors/{id}"
            ,"/v1/users/login"
    );

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isSecuredApi = securedEndpoints.stream().anyMatch(api -> antPathMatcher.match(api, request.getRequestURI())) && !request.getMethod().equals("GET");
        if (isSecuredApi) {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            if (csrfToken != null) {
                csrfToken.getToken();
                response.setHeader("X-CSRF-TOKEN", csrfToken.getToken());
            }

        }
        filterChain.doFilter(request, response);


    }
}
