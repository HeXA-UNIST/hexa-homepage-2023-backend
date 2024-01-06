package pro.hexa.backend.main.api.domain.user.domain.login.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.model.AUTHORIZATION_TYPE;
import pro.hexa.backend.domain.user.model.GENDER_TYPE;
import pro.hexa.backend.domain.user.model.STATE_TYPE;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;
import pro.hexa.backend.main.api.common.jwt.Jwt;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserCreateRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserFindPasswordChangeRequestDto;
import pro.hexa.backend.main.api.domain.user.domain.login.dto.UserFindPasswordRequestDto;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public String signupUser(UserCreateRequestDto request) {
        boolean userExists = userRepository.existsById(request.getId());

        if (userExists) {
            throw new BadRequestException(BadRequestType.EXISTS_USER);
        }

        if (!request.getPassword1().equals(request.getPassword2())) {
            throw new BadRequestException(BadRequestType.INCORRECT_PASSWORD);
        }

        GENDER_TYPE genderType = GENDER_TYPE.findKeyBYApiValue(request.getGender());
        STATE_TYPE stateType = STATE_TYPE.findKeyBYApiValue(request.getState());
        short regYear = Short.parseShort(request.getRegYear());

        User user = User.create(request.getId(), request.getEmail(), genderType, stateType, regYear,
            request.getRegNum(), request.getName(), request.getPassword1(), AUTHORIZATION_TYPE.Member);

        userRepository.save(user);

        return user.getId();
    }

    public String findUserPasswordById(UserFindPasswordRequestDto request) {
        String userid = request.getId();

        User user = userRepository.findById(userid)
            .orElseThrow(() -> new BadRequestException(BadRequestType.CANNOT_FIND_USER));

        return user.getId();
    }

    @Transactional
    public void changeUserPassword(UserFindPasswordChangeRequestDto request, String token) {
        String password1 = request.getPassword1();
        String password2 = request.getPassword2();

        // 토큰 검증
        Claims claims = Jwt.validate(token, Jwt.jwtSecretKey);
        String userId = claims.get(Jwt.JWT_USER_ID, String.class);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BadRequestException(BadRequestType.CANNOT_FIND_USER));

        if (!password1.equals(password2)) {
            throw new BadRequestException(BadRequestType.INCORRECT_PASSWORD);
        }

        // 비밀번호 변경 로직
        user.changePassword(password1);
    }
}
