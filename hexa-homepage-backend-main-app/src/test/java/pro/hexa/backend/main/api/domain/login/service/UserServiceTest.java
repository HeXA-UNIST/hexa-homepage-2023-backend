package pro.hexa.backend.main.api.domain.login.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.jwt.Jwt;
import pro.hexa.backend.main.api.domain.login.dto.UserCreateRequestDto;
import pro.hexa.backend.main.api.domain.login.dto.UserFindIdRequestDto;
import pro.hexa.backend.main.api.domain.login.dto.UserFindPasswordRequestDto1;
import pro.hexa.backend.main.api.domain.login.dto.UserFindPasswordRequestDto2;
import pro.hexa.backend.main.api.domain.login.dto.UserFindPasswordRequestDto3;
import pro.hexa.backend.main.api.domain.login.dto.UserFindVerificationRequestDto;
import pro.hexa.backend.service.EmailService;

class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    @Spy
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("정상적인 회원가입 테스트")
    void signupUserNormal() {
        //given
        UserCreateRequestDto userCreateRequestDto = createDefaultUserCreateRequestDtoForTest();

        Mockito.when(userRepository.existsById(userCreateRequestDto.getId()))
            .thenReturn(false);
        User user = makeUserForTest();
        Mockito.when(userRepository.save(any()))
            .thenReturn(user);

        String result = userService.signupUser(userCreateRequestDto);

        //when, then
        Assertions.assertEquals(result, userCreateRequestDto.getId());

    }

    @Test
    void findUserSendVerificationCode() {
        User user = makeUserForTest();
        when(userRepository.findByNameAndEmail("seonuk", "asd@example.com"))
            .thenReturn(Optional.of(user));

        UserFindIdRequestDto requestDto1 = new UserFindIdRequestDto("seonuk", "asd@example.com");
        UserFindIdRequestDto requestDto2 = new UserFindIdRequestDto("seonuk", "");
        UserFindIdRequestDto requestDto3 = new UserFindIdRequestDto("", "asd@example.com");

        assertDoesNotThrow(() -> {
            userService.findUserSendVerificationCode(requestDto1);
        });
        assertThrows(BadRequestException.class, () -> {
            userService.findUserSendVerificationCode(requestDto2);
        });
        assertThrows(BadRequestException.class, () -> {
            userService.findUserSendVerificationCode(requestDto3);
        });

    }

    @Test
    void verifyId() {
        UserFindVerificationRequestDto userFindVerificationRequestDto1 = new UserFindVerificationRequestDto(
            "김선욱",
            "asd@example.com",
            "correct");
        UserFindVerificationRequestDto userFindVerificationRequestDto2 = new UserFindVerificationRequestDto(
            "김선욱",
            "asd@example.com",
            "wrong");
        UserFindVerificationRequestDto userFindVerificationRequestDto3 = new UserFindVerificationRequestDto(
            "notExistingOne",
            "asd@example.com",
            "wrong");
        UserFindVerificationRequestDto userFindVerificationRequestDto4 = new UserFindVerificationRequestDto(
            "김선욱",
            "notExistingOne",
            "wrong");

        User user = makeUserForTest();
        user.setVerificationCode("correct");

        when(userRepository.findByNameAndEmail(any(), any()))
            .thenReturn(Optional.empty());
        when(userRepository.findByNameAndEmail("김선욱", "asd@example.com"))
            .thenReturn(Optional.of(user));

        assertEquals("seonuk", userService.verifyId(userFindVerificationRequestDto1));
        assertThrows(BadRequestException.class, () -> {
            userService.verifyId(userFindVerificationRequestDto2);
        });
        assertThrows(BadRequestException.class, () -> {
            userService.verifyId(userFindVerificationRequestDto3);
        });
        assertThrows(BadRequestException.class, () -> {
            userService.verifyId(userFindVerificationRequestDto4);
        });
    }

    @Test
    void findUserPasswordById() {
        //given
        Optional<User> user = Optional.of(makeUserForTest());
        when(userRepository.findById("seonuk")).thenReturn(user);
        when(userRepository.findById("invalid")).thenReturn(null);

        UserFindPasswordRequestDto1 requestDto1 = new UserFindPasswordRequestDto1("seonuk");
        UserFindPasswordRequestDto1 requestDto2 = new UserFindPasswordRequestDto1("invalid");

        assertDoesNotThrow(() -> {
            userService.findUserPasswordById(requestDto1);
        });
        assertThrows(BadRequestException.class, () -> {
            userService.findUserPasswordById(requestDto2);
        });


    }

    @Test
    void verifyPassword() {
        UserFindPasswordRequestDto2 userFindPasswordRequestDto2_1 = new UserFindPasswordRequestDto2(
            "seonuk",
            "correct"
        );

        User user = makeUserForTest();
        user.setVerificationCode("correct");

        when(userRepository.findById(any()))
            .thenReturn(Optional.empty());
        when(userRepository.findById("seonuk"))
            .thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> {
            userService.verifyPassword(userFindPasswordRequestDto2_1);
        });

    }

    @Test
    void changeUserPassword() {
        UserFindPasswordRequestDto3 userFindPasswordRequestDto3_1 = new UserFindPasswordRequestDto3(
            "pw1",
            "pw1"
        );
        UserFindPasswordRequestDto3 userFindPasswordRequestDto3_2 = new UserFindPasswordRequestDto3(
            "pw1",
            "pw2"
        );

        //claims를 리턴해야 하는데...
    }

    private UserCreateRequestDto createDefaultUserCreateRequestDtoForTest() {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto();
        userCreateRequestDto.setId("seonuk");
        userCreateRequestDto.setEmail("asd@example.com");
        userCreateRequestDto.setGender(GENDER_TYPE.여.getApiValue());
        userCreateRequestDto.setState(STATE_TYPE.휴학.getApiValue());
        userCreateRequestDto.setRegYear("2020");
        userCreateRequestDto.setRegNum("20202020");
        userCreateRequestDto.setName("김선욱");
        userCreateRequestDto.setPassword1("mangp123");
        userCreateRequestDto.setPassword2("mangp123");

        return userCreateRequestDto;
    }

    private User makeUserForTest() {
        User user = User.create(
            "seonuk",
            "asd@example.com",
            GENDER_TYPE.여,
            STATE_TYPE.휴학,
            (short) 2020,
            "20202020",
            "김선욱",
            "encodedPassword"
        );
        return user;
    }
}