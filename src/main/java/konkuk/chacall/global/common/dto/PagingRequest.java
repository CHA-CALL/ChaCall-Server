package konkuk.chacall.global.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "커서 기반 페이징 요청 DTO")
public record PagingRequest(
        @Schema(description = "마지막으로 조회된 데이터의 ID (다음 페이지 요청 시 사용)",
                example = "120",
                nullable = true)
        Long cursor,

        @Schema(description = "한 페이지에 조회할 개수",
                defaultValue = "20",
                minimum = "1")
        @Min(value = 1, message = "size 는 1 이상이어야 합니다.")
        Integer size
) {
    private static final int DEFAULT_SIZE = 20;

    public long getCursorOrDefault() {
        return (this.cursor == null) ? Long.MAX_VALUE : this.cursor;
    }

    public int getSizeOrDefault() {
        return (this.size == null) ? DEFAULT_SIZE : this.size;
    }
}
