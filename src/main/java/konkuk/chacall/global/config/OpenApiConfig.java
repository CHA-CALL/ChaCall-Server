package konkuk.chacall.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import konkuk.chacall.global.common.annotation.ExceptionDescription;
import konkuk.chacall.global.common.dto.ErrorResponse;
import konkuk.chacall.global.common.exception.code.ErrorCode;
import konkuk.chacall.global.common.swagger.ExampleHolder;
import konkuk.chacall.global.common.swagger.SwaggerResponseDescription;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@OpenAPIDefinition(
        info = @Info(
                title = "ChaCall 백엔드 API 명세서",
                description = "Springdoc을 이용한 ChaCall Swagger API 문서입니다.",
                version = "1.0.0"
        )
)
@Configuration
public class OpenApiConfig {
    private final String securitySchemaName = "JWT";

    @Value("${server.http-url}") private String httpUrl;

    @Value(("${server.profile}")) private String profile;

    @Bean
    public OpenAPI openAPI() {
        List<Server> serverList = switch (profile) {
            case "dev" -> List.of(
                    new Server().url(httpUrl).description("HTTP 개발 서버"),
                    new Server().url("http://localhost:8080").description("로컬 개발 서버")
            );
            default -> List.of(
                    new Server().url("http://localhost:8080").description("로컬 개발 서버")
            );
        };

        return new OpenAPI()
                .servers(serverList)
                .components(setComponents())
                .addSecurityItem(setSecurityItems());
    }
    private Components setComponents() {
        return new Components()
                .addSecuritySchemes(securitySchemaName, bearerAuth());
    }

    private SecurityScheme bearerAuth() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat(securitySchemaName)
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);
    }

    private SecurityRequirement setSecurityItems() {
        return new SecurityRequirement()
                .addList(securitySchemaName);
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {

            ExceptionDescription exceptionDescription = handlerMethod.getMethodAnnotation(
                    ExceptionDescription.class);

            // ExceptionDescription 어노테이션 단 메소드 적용
            if (exceptionDescription != null) {
                generateErrorCodeResponseExample(operation, exceptionDescription.value());
            }

            return operation;
        };
    }
    private void generateErrorCodeResponseExample(
            Operation operation, SwaggerResponseDescription type) {

        ApiResponses responses = operation.getResponses();

        Set<ErrorCode> errorCodeList = type.getErrorCodeList();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders =
                errorCodeList.stream()
                        .map(
                                errorCode -> ExampleHolder.builder()
                                        .holder(getSwaggerExample(errorCode))
                                        .code(errorCode.getHttpStatus().value())
                                        .name(errorCode.toString())
                                        .build()
                        ).collect(groupingBy(ExampleHolder::getCode));
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaggerExample(ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        Example example = new Example();
        example.description(errorCode.getMessage());
        example.setValue(errorResponse);
        return example;
    }

    private void addExamplesToResponses(
            ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();
                    v.forEach(
                            exampleHolder -> {
                                mediaType.addExamples(
                                        exampleHolder.getName(), exampleHolder.getHolder());
                            });
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setDescription("");
                    apiResponse.setContent(content);
                    responses.addApiResponse(status.toString(), apiResponse);
                });
    }
}
