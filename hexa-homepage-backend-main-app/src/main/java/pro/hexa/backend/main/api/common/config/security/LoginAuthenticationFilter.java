package pro.hexa.backend.main.api.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.common.auth.domain.RefreshToken;
import pro.hexa.backend.main.api.common.auth.repository.RefreshTokenRedisRepository;
import pro.hexa.backend.main.api.common.config.security.dto.LoginTokenDto;
import pro.hexa.backend.main.api.common.config.security.dto.LoginUserDto;
import pro.hexa.backend.main.api.common.exception.AuthorizationExceptionType;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;
import pro.hexa.backend.main.api.common.jwt.Jwt;
import pro.hexa.backend.main.api.common.utils.FilterUtils;

@RequiredArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginUserDto loginUserDto = objectMapper.readValue(request.getInputStream(), LoginUserDto.class);

            String userId = loginUserDto.getUserId();
            String password = loginUserDto.getPassword();

            if (userId == null) {
                userId = "";
            }

            if (password == null) {
                password = "";
            }

            userId = userId.trim();

            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(userId, password);

            setDetails(request, authRequest);

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    //TODO: 로그인 성공했을 때랑 실패했을 떄 함수 정의
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException {
        String userId = ((UserDetails) authResult.getPrincipal()).getUsername();
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BadRequestException(BadRequestType.CANNOT_FIND_USER));

        String accessToken = Jwt.generateAccessToken(user.getId());
        String refreshToken = Jwt.generateRefreshToken();
        RefreshToken refreshTokenEntity = RefreshToken.create(accessToken, refreshToken);
        refreshTokenRedisRepository.save(refreshTokenEntity);

        LoginTokenDto loginTokenDto = new LoginTokenDto(accessToken, refreshToken);

        FilterUtils.response(response, HttpStatus.OK, loginTokenDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException {
        FilterUtils.responseError(response, HttpStatus.BAD_REQUEST, AuthorizationExceptionType.LOGIN_FAILED, failed);
    }

}
