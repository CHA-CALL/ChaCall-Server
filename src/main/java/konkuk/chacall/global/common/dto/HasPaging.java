package konkuk.chacall.global.common.dto;

public interface HasPaging {
    PagingRequest pagingRequest();

    default PagingRequest pagingOrDefault() {
        return pagingRequest() == null ? new PagingRequest(null, null) : pagingRequest();
    }
}
