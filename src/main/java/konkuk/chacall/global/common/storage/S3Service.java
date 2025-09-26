package konkuk.chacall.global.common.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void uploadPdf(String key, String contentType, byte[] bytes) {
        String ct = (contentType != null) ? contentType : URLConnection.guessContentTypeFromName(key);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("x-amz-meta-origin", "reservation-pdf");

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .metadata(metadata)
                .contentType(ct != null ? ct : "application/pdf")
                .build();

        s3Client.putObject(req, RequestBody.fromBytes(bytes));
    }

    public void delete(String key) {
        DeleteObjectRequest req = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(req);
    }
}