package pro.hexa.backend.main.api.domain.login.controller;

import io.jsonwebtoken.Claims;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.common.jwt.Jwt;
import pro.hexa.backend.main.api.common.jwt.JwtTokenType;
import pro.hexa.backend.main.api.domain.user.controller.UserController;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserCreateRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserFindPasswordChangeRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserFindPasswordRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.domain.Verification;
import pro.hexa.backend.main.api.domain.user.domain.verification.dto.UserFindIdRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.dto.UserFindPasswordVerifyIdRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.dto.UserFindVerificationRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.repository.VerificationRedisRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pro.hexa.backend.main.api.common.jwt.Jwt.JWT_TOKEN_TYPE;
import static pro.hexa.backend.main.api.common.jwt.Jwt.JWT_USER_ID;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationRedisRepository verificationRedisRepository;

    @Test
    void userSignup() {
        //given
        String userId = "testId";
        UserCreateRequestDto request = new UserCreateRequestDto(
            userId,
            "test@hexa.pro",
            0,
            0,
            "2021",
            "",
            "test",
            "qwer1234",
            "qwer1234"
        );

        //when
        userController.userSignup(request);

        //then
        User user = userRepository.findById(userId)
            .orElse(null);

        assertNotNull(user);
        assertThat(request.getId()).isEqualTo(user.getId());
        assertThat(request.getGender()).isEqualTo(user.getGender().getApiValue());
        assertThat(request.getState()).isEqualTo(user.getState().getApiValue());
        assertThat(request.getRegYear()).isEqualTo(user.getRegYear().toString());
        assertThat(request.getRegNum()).isEqualTo(user.getRegNum());
    }

    @Test
    void findUserIdSendVerificationCode() {
        //given
        String userId = "testId+01";
        String userEmail = "test+01@hexa.pro";
        String userName = "test+01";

        User user = User.create(
            userId,
            userEmail,
            GENDER_TYPE.남,
            STATE_TYPE.재학,
            (short) 2021,
            "",
            userName,
            "qwer1234",
            AUTHORIZATION_TYPE.Member
        );
        userRepository.saveAndFlush(user);

        UserFindIdRequestDto request = new UserFindIdRequestDto(
            userName,
            userEmail
        );
        userController.findUserIdSendVerificationCode(request);

        //when
        Verification verification = verificationRedisRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalStateException("verificationCode not created"));

        // then
        assertNotNull(verification.getVerificationCode());
        assertNotEquals("", verification.getVerificationCode());
    }

    @Test
    void idVerifyVerificationCode() {
        // given
        String userId = "testId+02";
        String userEmail = "test+02@hexa.pro";
        String userName = "test+02";

        User user = User.create(
            userId,
            userEmail,
            GENDER_TYPE.남,
            STATE_TYPE.재학,
            (short) 2021,
            "",
            userName,
            "qwer1234",
            AUTHORIZATION_TYPE.Member
        );
        userRepository.saveAndFlush(user);

        UserFindIdRequestDto request2 = new UserFindIdRequestDto(
            userName,
            userEmail
        );
        userController.findUserIdSendVerificationCode(request2);

        Verification verification = verificationRedisRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalStateException("verificationCode not created"));

        // when
        UserFindVerificationRequestDto request3 = new UserFindVerificationRequestDto(
            userName,
            userEmail,
            verification.getVerificationCode()
        );
        String foundUserId = Optional.ofNullable(userController.idVerifyVerificationCode(request3))
            .map(HttpEntity::getBody)
            .orElse(null);

        // then
        assertEquals(userId, foundUserId);
    }

    @Test
    void findUserPasswordById() {
        // given
        String userId = "testId+03";
        String userEmail = "test+03@hexa.pro";
        String userName = "test+03";

        User user = User.create(
            userId,
            userEmail,
            GENDER_TYPE.남,
            STATE_TYPE.재학,
            (short) 2021,
            "",
            userName,
            "qwer1234",
            AUTHORIZATION_TYPE.Member
        );
        userRepository.saveAndFlush(user);

        // when
        UserFindPasswordRequestDto request = new UserFindPasswordRequestDto(userId);
        String foundUserId = Optional.ofNullable(userController.findUserPasswordById(request))
            .map(HttpEntity::getBody)
            .orElse(null);

        // then
        assertEquals(userId, foundUserId);
    }

    @Test
    void findUserPasswordSendVerificationCode() {
        // given
        String userId = "testId+04";
        String userEmail = "test+04@hexa.pro";
        String userName = "test+04";

        User user = User.create(
            userId,
            userEmail,
            GENDER_TYPE.남,
            STATE_TYPE.재학,
            (short) 2021,
            "",
            userName,
            "qwer1234",
            AUTHORIZATION_TYPE.Member
        );
        userRepository.saveAndFlush(user);

        UserFindIdRequestDto request2 = new UserFindIdRequestDto(
            userName,
            userEmail
        );
        userController.findUserPasswordSendVerificationCode(request2);

        // when
        Verification verification = verificationRedisRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalStateException("verificationCode not created"));

        // then
        String verificationCode = verification.getVerificationCode();
        assertNotNull(verificationCode);
        assertNotEquals("", verificationCode);
    }

    @Test
    void passwordVerifyVerificationCode() {
        // given
        String userId = "testId+05";
        String userEmail = "test+05@hexa.pro";
        String userName = "test+05";

        User user = User.create(
            userId,
            userEmail,
            GENDER_TYPE.남,
            STATE_TYPE.재학,
            (short) 2021,
            "",
            userName,
            "qwer1234",
            AUTHORIZATION_TYPE.Member
        );
        userRepository.saveAndFlush(user);

        UserFindIdRequestDto request2 = new UserFindIdRequestDto(
            userName,
            userEmail
        );
        userController.findUserPasswordSendVerificationCode(request2);

        Verification verification = verificationRedisRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalStateException("verificationCode not created"));

        //when
        UserFindPasswordVerifyIdRequestDto request3 = new UserFindPasswordVerifyIdRequestDto(
            userId,
            verification.getVerificationCode()
        );
        ResponseEntity<String> response = userController.passwordVerifyVerificationCode(request3);

        // then
        String token = Optional.ofNullable(response)
            .map(HttpEntity::getBody)
            .orElse(null);
        assertNotNull(token);

        Claims claims = Jwt.validate(token, Jwt.jwtSecretKey);
        JwtTokenType jwtTokenType = (JwtTokenType) claims.get(JWT_TOKEN_TYPE);
        String tokenUserId = (String) claims.get(JWT_USER_ID);
        assertEquals(JwtTokenType.CHANGE_PW_TOKEN, jwtTokenType);
        assertEquals(userId, tokenUserId);
    }

    @Test
    void changingUserPassword() {
        // given
        String userId = "testId+06";
        String userEmail = "test+06@hexa.pro";
        String userName = "test+06";

        User user = User.create(
            userId,
            userEmail,
            GENDER_TYPE.남,
            STATE_TYPE.재학,
            (short) 2021,
            "",
            userName,
            "qwer1234",
            AUTHORIZATION_TYPE.Member
        );
        userRepository.saveAndFlush(user);

        UserFindIdRequestDto request2 = new UserFindIdRequestDto(
            userName,
            userEmail
        );
        userController.findUserPasswordSendVerificationCode(request2);

        Verification verification = verificationRedisRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalStateException("verificationCode not created"));

        UserFindPasswordVerifyIdRequestDto request3 = new UserFindPasswordVerifyIdRequestDto(
            userId,
            verification.getVerificationCode()
        );
        ResponseEntity<String> response = userController.passwordVerifyVerificationCode(request3);
        String token = response.getBody();

        String newPassword = "password1asfdkjsfkdjlfsdahla";
        UserFindPasswordChangeRequestDto request4 = new UserFindPasswordChangeRequestDto(newPassword, newPassword);

        //when
        userController.changingUserPassword(request4, token);

        // then
        User foundUser = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("user not created"));

        assertEquals(newPassword, foundUser.getPassword());
    }
}
