package konkuk.chacall.global.common.dto;

public interface HasPaging {
    CursorPagingRequest cursorPagingRequest();

    default CursorPagingRequest pagingOrDefault() {
        return cursorPagingRequest() == null ? new CursorPagingRequest(null, null) : cursorPagingRequest();
    }
}
