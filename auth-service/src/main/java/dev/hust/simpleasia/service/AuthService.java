package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.core.utils.EventType;
import dev.hust.simpleasia.core.utils.TopicName;
import dev.hust.simpleasia.entity.domain.CustomerOTP;
import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.entity.dto.*;
import dev.hust.simpleasia.entity.event.RegisterInitReq;
import dev.hust.simpleasia.entity.event.RegisterInitResp;
import dev.hust.simpleasia.mapper.CredentialMapstruct;
import dev.hust.simpleasia.port.KafkaClient;
import dev.hust.simpleasia.repository.UserCredentialRepository;
import dev.hust.simpleasia.utils.AccountStatus;
import dev.hust.simpleasia.utils.Constants;
import dev.hust.simpleasia.utils.DateUtils;
import io.azam.ulidj.ULID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final UserCredentialRepository repository;
    private final CredentialMapstruct credentialMapstruct;
    private final KafkaClient kafkaClient;
    private final CustomerOTPService customerOTPService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserCredentialRepository userCredentialRepository;

    public GeneralResponse<RegisterInitDTO> initRegisterRequest(RegisterInitReq request) {
        String orderId = ULID.random();
        request.setOrderId(orderId);
        request.setEventType(EventType.INIT_SIGNUP);

        kafkaClient.put(TopicName.EVENT_BACKBONE.getName(), orderId, request);

        RegisterInitDTO response = RegisterInitDTO.builder()
                .orderId(orderId)
                .build();

        return GeneralResponse.success(response);
    }

    public GeneralResponse<RegisterInitResp> initRegister(RegisterInitReq registerInitReq) {
        Optional<UserCredential> credential = repository.findFirstByEmailAndStatus(registerInitReq.getEmail(), AccountStatus.INIT.getStatus());

        if (credential.isPresent()) throw new BusinessException("Email has been already register.");
        registerInitReq.setPassword(passwordEncoder.encode(registerInitReq.getPassword()));

        UserCredential entity = credentialMapstruct.toEntity(registerInitReq);
        entity.setStatus(AccountStatus.INIT.getStatus());
        repository.save(entity);

        return GeneralResponse.success(buildResponse(registerInitReq));
    }

    public void updateStatus(String email, String status) {
        Optional<UserCredential> credential = repository.findFirstByEmail(email);

        if (credential.isEmpty()) throw new BusinessException("Can not found your account.");
        UserCredential userCredential = credential.get();
        userCredential.setStatus(status);
        repository.save(userCredential);
    }

    private RegisterInitResp buildResponse(RegisterInitReq req) {
        return RegisterInitResp.builder()
                .orderId(ULID.random())
                .status(AccountStatus.INIT.getStatus())
                .email(req.getEmail())
                .eventType(EventType.INIT_SIGNUP_RESP)
                .build();
    }

    public GeneralResponse<UserCredential> getCredentialStatus(String orderId) {
        UserCredential userCredential = repository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Something went wrong !!!"));

        return GeneralResponse.success(userCredential);
    }

    public GeneralResponse<UserCredential> verify(VerifyOTPRequest request) {
        String verifyOTP = customerOTPService.verifyOTP(request.getEmail(), request.getOtp());

        if (verifyOTP.equals(Constants.EXPIRED_MESSAGE)) throw new BusinessException(Constants.EXPIRED_MESSAGE);

        else if (verifyOTP.equals(Constants.FAILED_MESSAGE)) throw new BusinessException(Constants.FAILED_MESSAGE);

        else {
            UserCredential userCredential = repository.findFirstByEmail(request.getEmail())
                    .orElseThrow(() -> new BusinessException("Can not found any account."));

            userCredential.setStatus(AccountStatus.ACTIVE.getStatus());
            return GeneralResponse.success(userCredential);
        }
    }

    public GeneralResponse<SignInResponse> signIn(SignInRequest request) {
        UserCredential userCredential = repository.findFirstByEmailAndStatus(request.getEmail(), AccountStatus.ACTIVE.getStatus())
                .orElseThrow(() -> new BusinessException("Account has not register yet"));

        boolean matches = passwordEncoder.matches(request.getPassword(), userCredential.getPassword());

        if (!matches) throw new BusinessException("Sign in failed");

        String token = tokenService.generateToken(
                AuthRequest.builder()
                        .username(request.getEmail())
                        .password(request.getPassword())
                        .email(request.getEmail())
                        .build());
        return GeneralResponse.success(credentialMapstruct.toResponse(userCredential, token));
    }

    public void validateToken(String token) {
        tokenService.validateToken(token);
    }

    public GeneralResponse<UserCredential> getDetail(String id) {
        UserCredential credential = userCredentialRepository.findById(id).orElseThrow(() -> new BusinessException("Can not get credential"));

        return GeneralResponse.success(credential);
    }

    public GeneralResponse<List<UserCredential>> getAllUser() {
        List<UserCredential> userCredentialList = userCredentialRepository.findAll();
        return GeneralResponse.success(userCredentialList);
    }

    public GeneralResponse<UserCredential> bannedUser(BannedUserRequest request) {
        UserCredential userCredential = userCredentialRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException("Can not found the user"));

        userCredential.setStatus(AccountStatus.INACTIVE.getStatus());
        UserCredential response = userCredentialRepository.saveAndFlush(userCredential);

        return GeneralResponse.success(response);

    }

    public GeneralResponse<ExistedUserResponse> checkExisted(SignInRequest request) {
        UserCredential userCredential = userCredentialRepository.findFirstByEmail(request.getEmail()).orElse(null);

        if (userCredential == null) return GeneralResponse.success(ExistedUserResponse.builder()
                .email(request.getEmail())
                .isExisted(false)
                .build());

        else return GeneralResponse.success(ExistedUserResponse.builder()
                .email(request.getEmail())
                .isExisted(true)
                .build());
    }

    public GeneralResponse<Boolean> resendOtp(ResendOtpRequest request) {
        if (request.getEmail() == null) throw new BusinessException("Invalid email");

        userCredentialRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new BusinessException("Can not found any account"));

        CustomerOTP customerOTP = CustomerOTP.builder()
                .orderId(ULID.random())
                .email(request.getEmail()).build();
        customerOTPService.generateOTP(customerOTP);

        return GeneralResponse.success(true);
    }

    public GeneralResponse<UserCredential> updateInfo(CredentialUpdateRequest request) {
        UserCredential userCredential = userCredentialRepository.findById(request.getId()).orElseThrow(() -> new BusinessException("Can not found any account"));

        if (request.getFullName() != null) userCredential.setFullName(request.getFullName());
        if (request.getGender() != null) userCredential.setGender(request.getGender());
        if (request.getBirthday() != null)
            userCredential.setBirthday(DateUtils.toDate(request.getBirthday(), "yyyy-MM-dd"));
        if (request.getRegion() != null) userCredential.setRegion(request.getRegion());

        UserCredential persistedCredential = userCredentialRepository.saveAndFlush(userCredential);

        return GeneralResponse.success(persistedCredential);
    }

    public GeneralResponse<UserCredential> changePwd(SignInRequest request) {
        UserCredential userCredential = userCredentialRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new BusinessException("Can not found account"));
        userCredential.setPassword(passwordEncoder.encode(request.getPassword()));
        UserCredential persistedCredential = userCredentialRepository.saveAndFlush(userCredential);

        return GeneralResponse.success(persistedCredential);
    }

    public GeneralResponse<UserCredential> updateAvatar(AvatarUpdateRequest request) {
        UserCredential userCredential = userCredentialRepository.findById(request.getId()).orElseThrow(() -> new BusinessException("Can not found user"));
        userCredential.setImagePath(request.getPath());
        userCredentialRepository.save(userCredential);

        return GeneralResponse.success(userCredential);
    }

    public GeneralResponse<List<Long>> getUserOverview(String period) {
        if (period.equals("Month")) {
            return GeneralResponse.success(getMonthlyUser());
        } else if (period.equals("Week")) {
            return GeneralResponse.success(getWeeklyUser());
        }

        return GeneralResponse.error("Invalid input format", new BusinessException("Invalid input format"));
    }

    private List<Long> getWeeklyUser() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        Date startOfWeekDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfWeekDate = Date.from(endOfWeek.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        List<UserCredential> userCredentialList = userCredentialRepository.findAllByDateRange(startOfWeekDate, endOfWeekDate);
        Map<DayOfWeek, Long> userSummary = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek day : DayOfWeek.values()) {
            userSummary.put(day, 0L);
        }

        for (UserCredential credential : userCredentialList) {
            LocalDate createdDate = credential.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DayOfWeek day = createdDate.getDayOfWeek();
            userSummary.put(day, userSummary.get(day) + 1);
        }

        // Convert map values to Long[]
        List<Long> response = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            response.add(userSummary.get(day));
        }

        return response;
    }

    private List<Long> getMonthlyUser() {
        List<UserCredential> userCredentialList = userCredentialRepository.findAllByYear(Year.now().getValue());
        Map<Month, Long> monthlyUser = new EnumMap<>(Month.class);

        for (Month month : Month.values()) {
            monthlyUser.put(month, 0L);
        }

        for (UserCredential userCredential : userCredentialList) {
            Month month = Month.of(userCredential.getCreatedAt().getMonth());

            monthlyUser.put(month, monthlyUser.get(month) + 1);
        }

        List<Long> response = new ArrayList<>();
        for (Month month : Month.values()) {
            response.add(monthlyUser.get(month));
        }

        return response;
    }

    public GeneralResponse<Long> getTotalUser() {
        long count = userCredentialRepository.count();
        return GeneralResponse.success(count);
    }
}
