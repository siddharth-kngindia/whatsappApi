package com.api.whatsapp.config;

import com.api.whatsapp.model.Profile;
import com.api.whatsapp.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<Profile> profileOptional = profileRepository.findByEmail(username);
        if (!profileOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        Profile profile = profileOptional.get();
        if (!passwordEncoder.matches(password, profile.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(
                profile, password, profile.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
