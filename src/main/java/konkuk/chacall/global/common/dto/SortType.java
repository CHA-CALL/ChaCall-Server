package konkuk.chacall.global.common.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum SortType {

    LATEST("최신순"),   // PK desc
    OLDEST("오래된순"); // PK asc

    @JsonValue
    private final String description;

    SortType(String description) {
        this.description = description;
    }

    public static SortType fromNullable(SortType sortType) {
        return (sortType == null) ? LATEST : sortType;
    }
}
