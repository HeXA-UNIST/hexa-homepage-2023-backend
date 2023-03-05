package pro.hexa.backend.main.api.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import pro.hexa.backend.main.api.common.exception.AuthorizationExceptionResponse;
import pro.hexa.backend.main.api.common.exception.AuthorizationExceptionType;

@Slf4j
@UtilityClass
public final class FilterUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static void response(HttpServletResponse response, HttpStatus status, Object object) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(status.value());
        if (object != null) {
            response.getWriter().write(objectMapper.writeValueAsString(object));
        }
    }

    public static void responseError(HttpServletResponse response, HttpStatus status, AuthorizationExceptionType exceptionType,
        Exception e) throws IOException {
        log.error(e.getMessage(), e);
        AuthorizationExceptionResponse authorizationExceptionResponse = new AuthorizationExceptionResponse(exceptionType);
        response(response, status, authorizationExceptionResponse);
    }
}
