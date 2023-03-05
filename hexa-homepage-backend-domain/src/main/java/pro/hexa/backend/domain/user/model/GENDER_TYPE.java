package pro.hexa.backend.domain.user.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import pro.hexa.backend.domain.model.model.EnumModel;

public enum GENDER_TYPE implements EnumModel {
    남("male", 0),
    여("female", 1),
    기타("etc.", 2);

    private final String value;
    @Getter
    private final int apiValue;

    GENDER_TYPE(String value, int apiValue) {
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

    public static GENDER_TYPE findKeyBYApiValue(int apiValue){
        Map<Integer, GENDER_TYPE> genderTypeMap = Arrays.stream(GENDER_TYPE.values())
            .collect(Collectors.toMap(GENDER_TYPE::getApiValue, Function.identity()));
        return genderTypeMap.get(apiValue);
    }
}
