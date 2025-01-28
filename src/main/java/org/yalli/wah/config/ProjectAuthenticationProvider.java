package org.yalli.wah.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class ProjectAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public ProjectAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) { // Constructor injection
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    //bu dao authenticaton provider kimi bir seydir ve melumatlarin hardan elde olunacagi haqda melumat verir
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        UserDetails dbUser = userDetailsService.loadUserByUsername(username);
        if(passwordEncoder.matches(authentication.getCredentials().toString(), dbUser.getPassword())){
            return new UsernamePasswordAuthenticationToken(username, authentication.getCredentials().toString(), dbUser.getAuthorities());
        } else{
            throw  new BadCredentialsException("Password isn't correct");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
