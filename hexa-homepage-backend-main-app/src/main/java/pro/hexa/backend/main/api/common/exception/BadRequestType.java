package pro.hexa.backend.main.api.common.exception;

public enum BadRequestType {
    EXISTS_USER("이미 존재하는 사용자 id입니다."),
    INCORRECT_PASSWORD("비밀번호가 일치하지 않습니다."),
    EXPIRED_TOKEN("만료된 토큰"),
    INVALID_TOKEN("잘못된 토큰"),
    CANNOT_FIND_USER("사용자를 찾을 수 없습니다."),
    INCORRECT_VERIFICATION_CODE("인증번호가 일치하지 않습니다."),
    NULL_VERIFICATION_CODE("인증번호를 발급받지 않은 유저입니다."),
    NULL_VALUE("필요한 값이 주어지지 않았습니다."),
    NULL_MODIFY_PROJECT_VALUES("프로젝트 수정 요청 시, 반드시 1개 값은 변경 되어야 합니다."),
    NULL_MODIFY_SEMINAR_VALUES("프로젝트 수정 요청 시, 반드시 1개 값은 변경 되어야 합니다."),
    INVALID_PAGE_NUM("조회 시 page는 반드시 1보다 커야합니다."),
    PROJECT_NOT_FOUND("존재하지 않는 프로젝트 id입니다."),
    SEMINAR_NOT_FOUND("존재하지 않는 seminar id입니다."),
    ATTACHMENT_NOT_EXIST("존재하지 않는 파일입니다."),
    NEWS_NOT_FOUND("뉴스 데이터를 찾을 수 없습니다."),
    INVALID_CREATE_DTO_TITLE("title이 NULL입니다"),
    INVALID_CREATE_DTO_START_DATE("startDate가 NULL입니다"),
    INVALID_CREATE_DTO_END_DATE("endDate가 NULL입니다"),
    INVALID_CREATE_DTO_PROJECTSTACKS("projectStacks이 NULL입니다"),
    INVALID_CREATE_DTO_STATE("state가 NULL입니다"),
    INVALID_CREATE_DTO_CONTENT("content가 NULL입니다"),
    INVALID_CREATE_DTO_THUMBNAIL("thumbnail이 NULL입니다");

    private final String message;

    BadRequestType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
