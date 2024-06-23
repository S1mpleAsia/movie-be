package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.entity.dto.*;
import dev.hust.simpleasia.entity.event.RegisterInitReq;
import dev.hust.simpleasia.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/resend-otp")
    public GeneralResponse<Boolean> resendOtp(@RequestBody ResendOtpRequest request) {
        return authService.resendOtp(request);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }

    @GetMapping("/detail")
    public GeneralResponse<UserCredential> getDetail(@RequestParam("id") String id) {
        return authService.getDetail(id);
    }

    @GetMapping("/user")
    public GeneralResponse<List<UserCredential>> getAllUser() {
        return authService.getAllUser();
    }

    @PostMapping("/user/update")
    public GeneralResponse<UserCredential> updateInfo(@RequestBody CredentialUpdateRequest request) {
        return authService.updateInfo(request);
    }

    @PostMapping("/user/banned")
    public GeneralResponse<UserCredential> bannedUser(@RequestBody BannedUserRequest request) {
        return authService.bannedUser(request);
    }

    @PostMapping("/check-existed")
    public GeneralResponse<ExistedUserResponse> checkExisted(@RequestBody SignInRequest request) {
        return authService.checkExisted(request);
    }

    @PostMapping("/user/pwd")
    public GeneralResponse<UserCredential> changePwd(@RequestBody SignInRequest request) {
        return authService.changePwd(request);
    }

    @PostMapping("/user/avatar")
    public GeneralResponse<UserCredential> updateAvatar(@RequestBody AvatarUpdateRequest request) {
        return authService.updateAvatar(request);
    }

    @GetMapping("/user/overview")
    public GeneralResponse<List<Long>> getUserOverview(@RequestParam("period") String period) {
        return authService.getUserOverview(period);
    }

    @GetMapping("/user/summary")
    public GeneralResponse<Long> getTotalUser() {
        return authService.getTotalUser();
    }
}
