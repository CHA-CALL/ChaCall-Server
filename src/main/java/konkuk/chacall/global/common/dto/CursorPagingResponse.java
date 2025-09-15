package konkuk.chacall.global.common.dto;

import java.util.List;

public record CursorPagingResponse<T>(
        List<T> content,
        int pageSize,
        Long lastCursor,
        boolean hasNext
) {
    public static <T> CursorPagingResponse<T> of(List<T> content, int pageSize, Long lastCursor, boolean hasNext) {
        return new CursorPagingResponse<>(content, pageSize, lastCursor, hasNext);
    }
}