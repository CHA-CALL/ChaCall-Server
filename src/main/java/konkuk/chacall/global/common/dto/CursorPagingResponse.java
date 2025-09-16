package konkuk.chacall.global.common.dto;

import java.util.List;

public record CursorPagingResponse<T>(
        List<T> content,
        Long lastCursor,
        boolean hasNext
) {
    public static <T> CursorPagingResponse<T> of(
            List<T> content,
            CursorExtractor<T> extractor,
            boolean hasNext
    ) {
        Long lastCursor = (content == null || content.isEmpty()) ? null : extractor.extractCursor(content.get(content.size() - 1));
        return new CursorPagingResponse<>(content, lastCursor, hasNext);
    }
}