package dev.hust.simpleasia.config;

import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerDetailsService implements UserDetailsService {
    @Autowired
    private UserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> userCredential = repository.findFirstByEmail(username);

        return userCredential.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + username));
    }
}
