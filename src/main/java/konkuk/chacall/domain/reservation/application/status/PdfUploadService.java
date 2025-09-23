package konkuk.chacall.domain.reservation.application.status;

import konkuk.chacall.global.common.annotation.HelperService;
import konkuk.chacall.global.common.storage.util.CdnUrlResolver;
import konkuk.chacall.global.common.storage.util.PdfGenerator;
import konkuk.chacall.global.common.storage.util.PdfTemplateRenderer;
import konkuk.chacall.global.common.storage.S3Uploader;
import konkuk.chacall.global.common.storage.util.KeyUtils;
import lombok.RequiredArgsConstructor;

@HelperService
@RequiredArgsConstructor
public class PdfUploadService {

    private final PdfTemplateRenderer templateRenderer;
    private final PdfGenerator pdfGenerator;
    private final S3Uploader s3Uploader;
    private final CdnUrlResolver cdnUrlResolver;

    /**
     * Reservation을 기반으로 HTML 렌더링 → PDF 생성 → S3 업로드 → CDN URL 반환
     */
    public String renderAndUpload(konkuk.chacall.domain.reservation.domain.model.Reservation reservation) {
        // 1) HTML 렌더링
        String html = templateRenderer.render(reservation);

        // 2) PDF 생성
        byte[] pdfBytes = pdfGenerator.generate(html);

        // 3) 업로드 Key (예: /reservations/{reservationId}/estimate-<epoch>.pdf)
        String objectKey = KeyUtils.buildReservationPdfKey(reservation.getReservationId());

        // 4) S3 업로드
        s3Uploader.uploadPdf(objectKey, "application/pdf", pdfBytes);

        // 5) CDN URL 생성
        return cdnUrlResolver.resolve(objectKey);
    }

    /**
     * 현재 저장된 pdfUrl이 존재하면 S3 객체 삭제 (추후에 필요하면 사용)
     */
    public void deleteIfExists(String currentPdfUrl) {
        if (currentPdfUrl == null || currentPdfUrl.isBlank()) return;
        String key = cdnUrlResolver.extractKeyFromUrl(currentPdfUrl);
        if (key != null && !key.isBlank()) {
            s3Uploader.delete(key);
        }
    }
}