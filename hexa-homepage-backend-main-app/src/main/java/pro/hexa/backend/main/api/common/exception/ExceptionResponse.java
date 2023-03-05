package pro.hexa.backend.main.api.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ExceptionResponse {

    private HttpStatus status;

    ExceptionResponse(HttpStatus status) {
        this.status = status;
    }

}
