package pro.hexa.backend.main.api.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthorizationExceptionResponse extends CommonExceptionResponse {

    private final AuthorizationExceptionType errCode;

    public AuthorizationExceptionResponse(AuthorizationExceptionType errCode) {
        super(HttpStatus.UNAUTHORIZED, errCode.getMessage());
        this.errCode = errCode;
    }
}
