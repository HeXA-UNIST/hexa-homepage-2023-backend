package pro.hexa.backend.main.api.domain.user.domain.verification.service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.dto.EmailRequestDto;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;
import pro.hexa.backend.main.api.common.jwt.Jwt;
import pro.hexa.backend.main.api.domain.user.domain.verification.domain.Verification;
import pro.hexa.backend.main.api.domain.user.domain.verification.dto.UserFindIdRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.dto.UserFindPasswordVerifyIdRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.dto.UserFindVerificationRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.repository.VerificationRedisRepository;
import pro.hexa.backend.service.EmailService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserVerificationService {

    private final UserRepository userRepository;
    private final VerificationRedisRepository verificationRedisRepository;
    private final EmailService emailService;

    @Transactional
    public void findUserSendVerificationCode(UserFindIdRequestDto request) {
        String name = request.getName();
        String email = request.getEmail();

        if (name.isEmpty() || email.isEmpty()){
            throw new BadRequestException(BadRequestType.NULL_VALUE);
        }

        User user = userRepository.findByNameAndEmail(name, email)
            .orElseThrow(() -> new BadRequestException(BadRequestType.CANNOT_FIND_USER));

        // Generate verification code
        String verificationCode = generateVerificationCode();

        // Send verification code to the user's email
        EmailRequestDto emailRequestDto = EmailRequestDto.builder()
            .sendTo(email)
            .Subject("Verification Code")
            .Text("Your verification code is: " + verificationCode)
            .build();
        emailService.send(emailRequestDto);

        // Store the verification code in the user's record
        Verification verification = Verification.create(verificationCode, user.getId());
        verificationRedisRepository.save(verification);
    }

    public String verifyId(UserFindVerificationRequestDto request) {
        String name = request.getName();
        String email = request.getEmail();
        String userVerificationCode = request.getVerificationCode();

        User user = userRepository.findByNameAndEmail(name, email)
            .orElseThrow(() -> new BadRequestException(BadRequestType.CANNOT_FIND_USER));

        Verification verification = verificationRedisRepository.findByUserId(user.getId())
            .orElseThrow(() -> new BadRequestException(BadRequestType.CANNOT_FIND_USER));

        if (!userVerificationCode.equals(verification.getVerificationCode())) {
            throw new BadRequestException(BadRequestType.INCORRECT_VERIFICATION_CODE);
        }

        return maskingId(user.getId());
    }

    public String verifyPassword(UserFindPasswordVerifyIdRequestDto request) {
        String id = request.getId();
        String userVerificationCode = request.getVerificationCode();

        User user = userRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(BadRequestType.CANNOT_FIND_USER));

        Verification verification = verificationRedisRepository.findByUserId(user.getId())
            .orElseThrow(() -> new BadRequestException(BadRequestType.CANNOT_FIND_USER));

        if (verification.getVerificationCode().isEmpty()) {
            throw new BadRequestException(BadRequestType.NULL_VERIFICATION_CODE);
        }

        if (!userVerificationCode.equals(verification.getVerificationCode())) {
            throw new BadRequestException(BadRequestType.INCORRECT_VERIFICATION_CODE);
        }

        return Jwt.generateChangePasswordToken(id);
    }

    private String maskingId(@NonNull String id) {
        return Arrays.stream(id.split(""))
            .map(ch -> {
                if (ch.charAt(0) % 4 == 0) {
                    return "*";
                } else {
                    return ch;
                }
            }).collect(Collectors.joining());
    }

    private String generateVerificationCode() {
        // 6자리 난수 생성
        SecureRandom secureRandom = new SecureRandom();
        int code = secureRandom.nextInt(900000) + 100000; // 100000 이상 999999 이하의 난수 생성
        return String.valueOf(code);
    }
}
