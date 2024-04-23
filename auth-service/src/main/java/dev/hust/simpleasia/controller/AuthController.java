package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.entity.dto.RegisterInitDTO;
import dev.hust.simpleasia.entity.dto.SignInRequest;
import dev.hust.simpleasia.entity.dto.SignInResponse;
import dev.hust.simpleasia.entity.dto.VerifyOTPRequest;
import dev.hust.simpleasia.entity.event.RegisterInitReq;
import dev.hust.simpleasia.service.AuthService;
import dev.hust.simpleasia.service.CustomerOTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public GeneralResponse<RegisterInitDTO> initRegister(@RequestBody RegisterInitReq request) {
        return authService.initRegisterRequest(request);
    }

    @GetMapping
    public GeneralResponse<UserCredential> registerStatus(@RequestParam("orderId") String orderId) {
        return authService.getCredentialStatus(orderId);
    }

    @PostMapping("/verify")
    public GeneralResponse<UserCredential> verify(@RequestBody VerifyOTPRequest request) {
        return authService.verify(request);
    }

    @PostMapping("/sign-in")
    public GeneralResponse<SignInResponse> signIn(@RequestBody SignInRequest request) {
        return authService.signIn(request);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }
}
