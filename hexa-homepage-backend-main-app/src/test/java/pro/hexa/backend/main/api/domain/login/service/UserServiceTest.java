package pro.hexa.backend.main.api.domain.login.service;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserCreateRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserFindPasswordRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.login.service.UserService;
import pro.hexa.backend.service.EmailService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
    void findUserPasswordById() {
        //given
        Optional<User> user = Optional.of(makeUserForTest());
        when(userRepository.findById("seonuk")).thenReturn(user);
        when(userRepository.findById("invalid")).thenReturn(Optional.empty());

        UserFindPasswordRequestDto requestDto1 = new UserFindPasswordRequestDto("seonuk");
        UserFindPasswordRequestDto requestDto2 = new UserFindPasswordRequestDto("invalid");

        assertDoesNotThrow(() -> {
            userService.findUserPasswordById(requestDto1);
        });
        assertThrows(BadRequestException.class, () -> {
            userService.findUserPasswordById(requestDto2);
        });


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
        return User.create(
            "seonuk",
            "asd@example.com",
            GENDER_TYPE.여,
            STATE_TYPE.휴학,
            (short) 2020,
            "20202020",
            "김선욱",
            "encodedPassword",
            AUTHORIZATION_TYPE.Member
        );
    }
}
