package konkuk.chacall.global.common.dto;

import lombok.Getter;

@Getter
public enum SortType implements EnumValue{

    NEWEST("최신순"),   // PK desc
    OLDEST("오래된순"); // PK asc

    private final String value;

    SortType(String description) {
        this.value = description;
    }

    public static SortType fromNullable(SortType sortType) {
        return (sortType == null) ? NEWEST : sortType;
    }
}
