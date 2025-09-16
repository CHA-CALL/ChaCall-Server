package konkuk.chacall.global.common.dto;

public interface HasPaging {
    CursorPagingRequest pagingRequest();

    default CursorPagingRequest pagingOrDefault() {
        return pagingRequest() == null ? new CursorPagingRequest(null, null) : pagingRequest();
    }
}
