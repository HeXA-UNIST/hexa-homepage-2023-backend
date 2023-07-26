package pro.hexa.backend.main.api.common.exception;

public enum BadRequestType {
    EXISTS_USER("이미 존재하는 사용자 id입니다."),
    INCORRECT_PASSWORD("비밀번호가 일치하지 않습니다."),
    EXPIRED_TOKEN("만료된 토큰"),
    INVALID_TOKEN("잘못된 토큰"),
    CANNOT_FIND_USER("사용자를 찾을 수 없습니다."),
    INCORRECT_VERIFICATION_CODE("인증번호가 일치하지 않습니다."),
    NULL_VERIFICATION_CODE("인증번호를 발급받지 않은 유저입니다."),
    NULL_VALUE("필요한 값이 주어지지 않았습니다.");
    private final String message;

    BadRequestType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
