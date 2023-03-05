package pro.hexa.backend.main.api.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonExceptionResponse extends ExceptionResponse{
    private final String message;

    public CommonExceptionResponse(HttpStatus status, String message){
        super(status);
        this.message=message;
    }
}
