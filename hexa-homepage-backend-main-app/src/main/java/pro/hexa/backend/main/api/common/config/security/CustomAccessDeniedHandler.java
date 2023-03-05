package pro.hexa.backend.main.api.common.config.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import pro.hexa.backend.main.api.common.exception.CommonExceptionResponse;
import pro.hexa.backend.main.api.common.utils.FilterUtils;

public class CustomAccessDeniedHandler implements
    AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException {
        CommonExceptionResponse commonExceptionResponse = new CommonExceptionResponse(HttpStatus.FORBIDDEN, accessDeniedException.getMessage());
        FilterUtils.response(response, HttpStatus.FORBIDDEN, commonExceptionResponse);
    }
}
