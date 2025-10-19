package konkuk.chacall.global.common.dto;

public interface HasPaging {
    CursorPagingRequest cursorPagingRequest();

    default CursorPagingRequest pagingOrDefault(SortType sort) {
        CursorPagingRequest req = cursorPagingRequest();

        if (req == null) {
            return (sort == SortType.OLDEST)
                    ? new CursorPagingRequest(0L, 20)
                    : new CursorPagingRequest(Long.MAX_VALUE, 20);
        }

        if (req.cursor() == null) {
            int size = (req.size() == null) ? 20 : req.size();

            return (sort == SortType.OLDEST)
                    ? new CursorPagingRequest(0L, size)
                    : new CursorPagingRequest(Long.MAX_VALUE, size);
        }

        if (req.size() == null) {
            return new CursorPagingRequest(req.cursor(), 20);
        }

        return req; // 이미 커서 존재
    }
}
