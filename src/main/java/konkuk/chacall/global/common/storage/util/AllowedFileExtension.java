package konkuk.chacall.global.common.storage.util;

import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.code.ErrorCode;

import java.util.Arrays;
import java.util.List;

public enum AllowedFileExtension {
    PNG("png"),
    JPG("jpg"),
    JPEG("jpeg"),
    SVG("svg"),
    WEBP("webp"),
    PDF("pdf")
    ;

    private final String value;

    AllowedFileExtension(String value) {
        this.value = value;
    }

    public static void checkAllowedExtension(List<String> extensions) {
        for (String extension : extensions) {
            Arrays.stream(AllowedFileExtension.values())
                    .filter(ext -> ext.value.equalsIgnoreCase(extension))
                    .findAny()
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.INVALID_FILE_EXTENSION,
                            new IllegalArgumentException("허용되지 않은 파일 확장자: " + extension)
                    ));
        }
    }
}
