package pro.hexa.backend.main.api.common.exception;

public enum AuthorizationExceptionType {
    LOGIN_FAILED("로그인 실패"),
    UNKNOWN("토큰을 확인해주세요");

    private final String message;

    AuthorizationExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
