package pro.hexa.backend.main.api.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserCreateRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserFindPasswordChangeRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserFindPasswordRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.login.service.UserService;
import pro.hexa.backend.main.api.domain.user.domain.verification.dto.UserFindIdRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.dto.UserFindPasswordVerifyIdRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.dto.UserFindVerificationRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.verification.service.UserVerificationService;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserVerificationService userVerificationService;

    @Operation(description = "회원가입")
    @PostMapping("/create")
    public ResponseEntity<String> userSignup(@RequestBody UserCreateRequestDto request) {
        return ResponseEntity.ok(userService.signupUser(request));
    }

    @Operation(description = "아이디 찾기(인증번호 전송)")
    @PostMapping("/find_id(SendVerificationCode)")
    public ResponseEntity<String> findUserIdSendVerificationCode(@RequestBody UserFindIdRequestDto request) {
        userVerificationService.findUserSendVerificationCode(request);
        return ResponseEntity.ok("Verification code is sent successfully!");
    }

    @Operation(description = "아이디 찾기(인증번호 확인)")
    @PostMapping("/find_id(verifyVerificationCode)")
    public ResponseEntity<String> idVerifyVerificationCode(@RequestBody UserFindVerificationRequestDto request) {
        String userid = userVerificationService.verifyId(request);
        return ResponseEntity.ok(userid);
    }

    @Operation(description = "비밀번호 찾기(id 입력)")
    @PostMapping("/find_passwordbyId")
    public ResponseEntity<String> findUserPasswordById(@RequestBody UserFindPasswordRequestDto request) {
        String userId = userService.findUserPasswordById(request);
        return ResponseEntity.ok(userId);
    }

    @Operation(description = "비밀번호 찾기(인증번호 전송)")
    @PostMapping("/find_password_SendVerificationCode")
    public ResponseEntity<String> findUserPasswordSendVerificationCode(@RequestBody UserFindIdRequestDto request) {
        userVerificationService.findUserSendVerificationCode(request);
        return ResponseEntity.ok("Verification code is sent successfully!");
    }

    @Operation(description = "비밀번호 찾기(인증번호 확인)")
    @PostMapping("/find_password_verifyVerificiationCode")
    public ResponseEntity<String> passwordVerifyVerificationCode(@RequestBody UserFindPasswordVerifyIdRequestDto request) {
        String token = userVerificationService.verifyPassword(request);
        return ResponseEntity.ok(token);
    }

    @Operation(description = "비밀번호 찾기(비밀번호 변경)")
    @PostMapping("/password_change")
    public ResponseEntity<Boolean> changingUserPassword(@RequestBody UserFindPasswordChangeRequestDto request, String token) {
        userService.changeUserPassword(request, token);
        return ResponseEntity.ok(true);
    }
}
