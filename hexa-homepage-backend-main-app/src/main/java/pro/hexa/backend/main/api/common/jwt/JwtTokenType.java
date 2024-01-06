package pro.hexa.backend.main.api.common.jwt;

import lombok.Getter;

public enum JwtTokenType {
    ACCESS_TOKEN("ACCESS_TOKEN"),
    REFRESH_TOKEN("REFRESH_TOKEN"),
    CHANGE_PW_TOKEN("CHANGE_PW_TOKEN")
    ;

    @Getter
    private final String value;

    JwtTokenType(String value) {
        this.value = value;
    }
}
