package konkuk.chacall.domain.foodtruck.domain.value;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PhotoUrlList {

    private final List<String> urls;

    public static PhotoUrlList of(List<String> urls) {
        return new PhotoUrlList(urls == null ? List.of() : List.copyOf(urls));
    }

    public List<String> getUrls() {
        return Collections.unmodifiableList(urls);
    }

    public boolean isEmpty() {
        return urls == null || urls.isEmpty();
    }

    public int size() {
        return urls == null ? 0 : urls.size();
    }
}