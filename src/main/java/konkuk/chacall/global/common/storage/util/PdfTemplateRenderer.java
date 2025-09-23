package konkuk.chacall.global.common.storage.util;

import jakarta.annotation.PostConstruct;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Component
@RequiredArgsConstructor
public class PdfTemplateRenderer {

    @Value("${app.pdf.template}")
    private String templateName;

    private TemplateEngine engine;

    @PostConstruct
    void init() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        SpringTemplateEngine e = new SpringTemplateEngine();
        e.setTemplateResolver(resolver);
        this.engine = e;
    }

    public String render(Reservation reservation) {
        Context ctx = new Context();
        ctx.setVariable("reservation", reservation);
        ctx.setVariable("info", reservation.getReservationInfo());
        ctx.setVariable("foodTruck", reservation.getFoodTruck());
        ctx.setVariable("member", reservation.getMember());
        // 필요 변수 추가 가능 (로고, 회사정보 등)

        return engine.process(templateName, ctx);
    }
}