package pro.hexa.backend.domain.project.model;

import pro.hexa.backend.domain.model.model.EnumModel;

public enum STATE_TYPE implements EnumModel {
    승인중("승인중"),
    모집중("모집중"),
    진행중("진행중"),
    진행완료("진행완료");

    private final String value;

    STATE_TYPE(String value) {
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
