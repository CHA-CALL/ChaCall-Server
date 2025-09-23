package konkuk.chacall.global.common.storage.util;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class PdfGenerator {

    @Value("${app.pdf.font-path}")
    private Resource fontResource;

    public byte[] generate(String html) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             InputStream in = fontResource.getInputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();

            builder.useFont(
                    () -> in,
                    "NotoSansKR",
                    400,
                    BaseRendererBuilder.FontStyle.NORMAL,
                    true
            );

            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("PDF 생성 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("PDF 렌더링 실패", e);
        }
    }
}