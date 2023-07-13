package pro.hexa.backend.main.api.domain.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.hexa.backend.main.api.domain.login.dto.FindIdRequestDto;
import pro.hexa.backend.main.api.domain.login.dto.ErrorCheckResponse;
import pro.hexa.backend.main.api.domain.login.dto.FindpwChangePwRequestDto;
import pro.hexa.backend.main.api.domain.login.dto.FindpwRequestDto;
import pro.hexa.backend.main.api.domain.login.dto.FindpwSendCodeRequestDto;
import pro.hexa.backend.main.api.domain.login.dto.FindpwWithCodeResponse;
import pro.hexa.backend.main.api.domain.login.dto.VerifyEmailWithCodeRequestDto;
import pro.hexa.backend.main.api.domain.login.dto.FindIdWithCodeResponse;
import pro.hexa.backend.main.api.domain.login.dto.UserCreateRequestDto;
import pro.hexa.backend.main.api.domain.login.service.UserService;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(description = "회원가입")
    @PostMapping("/create")
    public ResponseEntity<String> userSignup(@RequestBody UserCreateRequestDto request) {
        return new ResponseEntity<>(userService.userSignup(request), HttpStatus.OK);
    }


    @Operation(description = "아이디 찾기 이메일 인증코드 발급")
    @PostMapping("/findid")
    public ResponseEntity<ErrorCheckResponse> userFindId(@RequestBody FindIdRequestDto request) {
        return new ResponseEntity<>(userService.userFindId(request), HttpStatus.OK);
    }

    @Operation(description = "아이디 찾기 이메일 인증코드 인증")
    @PostMapping("/findidWithCode")
    public ResponseEntity<FindIdWithCodeResponse> userFindIdWithCode(@RequestBody VerifyEmailWithCodeRequestDto request) {
        return new ResponseEntity<>(userService.userFindIdWithCode(request), HttpStatus.OK);
    }


    @Operation(description = "아이디 찾기 이메일 인증코드 인증")
    @PostMapping("/findpw")
    public ResponseEntity<ErrorCheckResponse> userFindPw(@RequestBody FindpwRequestDto request) {
        return new ResponseEntity<>(userService.userFindPw(request), HttpStatus.OK);
    }

    @Operation(description = "아이디 찾기 이메일 인증코드 인증")
    @PostMapping("/findpwSendCode")
    public ResponseEntity<ErrorCheckResponse> userFindPwSendCode(@RequestBody FindpwSendCodeRequestDto request) {
        return new ResponseEntity<>(userService.userFindPwSendCode(request), HttpStatus.OK);
    }

    @Operation(description = "아이디 찾기 이메일 인증코드 인증")
    @PostMapping("/findpwWithCode")
    public ResponseEntity<FindpwWithCodeResponse> userFindPwWithCode(@RequestBody VerifyEmailWithCodeRequestDto request) {
        return new ResponseEntity<>(userService.userFindPwWithCode(request), HttpStatus.OK);
    }

    @Operation(description = "아이디 찾기 이메일 인증코드 인증")
    @PostMapping("/findpwChangePw")
    public ResponseEntity<ErrorCheckResponse> userFindPwChangePw(@RequestBody FindpwChangePwRequestDto request) {
        return new ResponseEntity<>(userService.userFindPwChangePw(request), HttpStatus.OK);
    }


}
