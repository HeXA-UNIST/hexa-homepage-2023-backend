package pro.hexa.backend.domain.user.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import pro.hexa.backend.domain.model.model.EnumModel;

public enum STATE_TYPE implements EnumModel {
    재학("재학", 0),
    휴학("휴학", 1),
    졸업("졸업", 2);

    private final String value;
    @Getter
    private final int apiValue;

    STATE_TYPE(String value, int apiValue) {
        this.value = value;
        this.apiValue = apiValue;
    }

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static STATE_TYPE findKeyBYApiValue(int apiValue) {
        Map<Integer, STATE_TYPE> stateTypeMap = Arrays.stream(STATE_TYPE.values())
            .collect(Collectors.toMap(STATE_TYPE::getApiValue, Function.identity()));
        return stateTypeMap.get(apiValue);
    }
}
