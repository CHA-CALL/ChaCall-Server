package konkuk.chacall.global.common.storage;

import konkuk.chacall.global.common.storage.util.CdnUrlResolver;
import konkuk.chacall.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URLConnection;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3Config s3Config;

    private final CdnUrlResolver cdnUrlResolver;

    private static final int PRESIGNED_URL_EXPIRATION_MINUTES = 10;

    public void uploadPdf(String key, String contentType, byte[] bytes) {
        String ct = (contentType != null) ? contentType : URLConnection.guessContentTypeFromName(key);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("x-amz-meta-origin", "reservation-pdf");

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(s3Config.getBucket())
                .key(key)
                .metadata(metadata)
                .contentType(ct != null ? ct : "application/pdf")
                .build();

        s3Client.putObject(req, RequestBody.fromBytes(bytes));
    }

    public void delete(String key) {
        DeleteObjectRequest req = DeleteObjectRequest.builder()
                .bucket(s3Config.getBucket())
                .key(key)
                .build();
        s3Client.deleteObject(req);
    }

    public String generatePresignedUrl(String key) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Config.getBucket())
                .key(key)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(builder -> builder
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(PRESIGNED_URL_EXPIRATION_MINUTES))
        );

        return presignedRequest.url().toString();
    }

    public String getFileUrl(String key) {
        return cdnUrlResolver.resolve(key);
    }
}