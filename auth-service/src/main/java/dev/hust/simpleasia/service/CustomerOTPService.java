package dev.hust.simpleasia.service;

import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.CustomerOTP;
import dev.hust.simpleasia.repository.CustomerOTPRepository;
import dev.hust.simpleasia.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerOTPService {
    private final CustomerOTPRepository customerOTPRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void generateOTP(CustomerOTP customer) {
        log.info("--- Start sending OTP ---");
        String otp = String.format("%06d", new Random().nextInt(100000));

        customer.setOtp(passwordEncoder.encode(otp));
        customer.setTimestamp(new Date());

        customerOTPRepository.save(customer);
        log.info("Sending OTP: [{}] to email [{}] at time: [{}]", otp, customer.getEmail(), customer.getTimestamp());
        sendOTP(customer.getEmail(), otp);
    }

    public void sendOTP(String email, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("noreply@accounts.dev", "Movie System Platform");
            helper.setTo(email);

            String subject = otp + " is your verification code";

            String content = "<div style=\"padding: 48px 32px; font-family: sans-serif;\">\n" +
                    "<p style=\"font-size: 24px; line-height: 36px;\">Movie System Platform</p>\n" +
                    "<p style=\"font-size: 32px; line-height: 39px; font-weight: bold; margin-top: 10px;\">Verification Code</p>\n" +
                    "<div style=\"margin-top: 10px\">\n" +
                    "<p style=\"font-size: 14px; line-height: 21px;\">Enter the following verification code when prompted:</p>\n" +
                    "<p style=\"font-size: 40px; line-height: 60px; font-weight: bold;\">" +
                    otp +
                    "</p>\n" +
                    "</div>\n" +
                    "<div style=\"font-size: 14px; line-height: 21px; margin-top: 10px;\">\n" +
                    "<p>To protect your account, do not share this code.</p>\n" +
                    "<p style=\"font-weight: bold;\">Didn't request this?</p>\n" +
                    "<p>If you didn't make this request, you can safely ignore this email.</p>\n" +
                    "</div>\n" +
                    "</div>";

            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("--- Send OTP success ---");
        } catch (Exception ex) {
            log.error("Send OTP got exception: [{}]", ex.getMessage(), ex);
            throw new BusinessException("Send OTP failed");
        }
    }

    public String verifyOTP(String email, String otp) {
        Optional<CustomerOTP> record = customerOTPRepository.findFirstByEmailOrderByTimestampDesc(email);

        if(record.isEmpty()) return Constants.EXPIRED_MESSAGE;

        boolean matches = passwordEncoder.matches(otp, record.get().getOtp());

        if(matches) return Constants.SUCCESS_MESSAGE;

        return Constants.FAILED_MESSAGE;
    }
}
