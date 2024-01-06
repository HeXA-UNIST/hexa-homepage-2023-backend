package pro.hexa.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailRequestDto {
    private String sendTo;
    private String Subject;
    private String Text;
}