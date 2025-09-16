package konkuk.chacall.global.common.dto;

@FunctionalInterface
public interface CursorExtractor<T> {
    Long extractCursor(T lastElement);
}
