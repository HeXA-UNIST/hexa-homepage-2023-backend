package pro.hexa.backend.domain.project_member.model;

import pro.hexa.backend.domain.model.model.EnumModel;

public enum AUTHORIZATION_TYPE implements EnumModel {
    Member("member"),
    Graduate("graduate"),
    Pro("pro"),
    Admin("admin"),
    All("all");

    private final String value;

    AUTHORIZATION_TYPE(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
