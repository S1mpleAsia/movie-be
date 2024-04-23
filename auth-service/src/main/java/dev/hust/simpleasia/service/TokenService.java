package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.entity.dto.AuthRequest;
import dev.hust.simpleasia.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserCredentialRepository repository;

    public String generateToken(AuthRequest authRequest) {
        if (authRequest.getEmail().isEmpty() || authRequest.getPassword().isEmpty()) {
            throw new BusinessException("Email or password is empty");
        }

        Optional<UserCredential> credential = repository.findFirstByEmail(authRequest.getEmail());

        if (credential.isEmpty())
            throw new BusinessException(String.format("User not found with email: %s", authRequest.getEmail()));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                ));

        if(authentication.isAuthenticated()) {
            return jwtService.createToken(authRequest.getEmail());
        } else {
            throw new BusinessException("Invalid access");
        }
    }

    public void validateToken(String token) {
        jwtService.verifyToken(token);
    }
}
