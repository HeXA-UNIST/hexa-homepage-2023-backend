package pro.hexa.backend.main.api.common.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.MemberSubstitution.Substitution.Chain;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pro.hexa.backend.domain.user.domain.User;
import pro.hexa.backend.domain.user.repository.UserRepository;
import pro.hexa.backend.main.api.common.config.security.dto.CustomUserDetails;
import pro.hexa.backend.main.api.common.exception.AuthorizationExceptionType;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;
import pro.hexa.backend.main.api.common.jwt.Jwt;
import pro.hexa.backend.main.api.common.utils.FilterUtils;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final List<String> authenticationUnnecessaryRequests;
    // JwtAuthorizationFilter는 AuthenticationManager와 UserRepository를 인자를 받아 생성된다.
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        // 다음 변수는 AuthenticationUnnecessaryRequests이다.
        this.authenticationUnnecessaryRequests = Arrays.asList(WebSecurityConfig.AUTHENTICATION_UNNECESSARY_REQUESTS);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        // 전반적으로 doFilterInternal은 request, response, FilterChain을 인자로 받고, request와 response 사이에 filter를 intercept함.
        String servletPath = request.getServletPath();
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null || authenticationUnnecessaryRequests.contains(servletPath)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            Claims claims = Jwt.validate(accessToken, Jwt.jwtSecretKey);
            String userId = (String) claims.get(Jwt.JWT_USER_ID);
            if (userId == null) {
                return;
            }
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(BadRequestType.CANNOT_FIND_USER));
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            CustomAuthentication customAuthentication = new CustomAuthentication(customUserDetails, null);
            // SecurityContextHolder의 Authentication값을 CustomAuthentication값으로 세팅한다.
            SecurityContextHolder.getContext().setAuthentication(customAuthentication);
            chain.doFilter(request, response); // FilterChain에 doFilter로 request와 response 사이에 intercept를 한다.
        } catch (Exception e) {
            FilterUtils.responseError(response, HttpStatus.UNAUTHORIZED, AuthorizationExceptionType.UNKNOWN, e);
        }
    }
}
