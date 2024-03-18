package com.sunmi.httpstatus.Swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Http-Status 테스트 API 명세서",
        description = "HTTP 상태코드 테스트 API 명세서",
        version = "v1"))
@Configuration
public class SwaggerConfig {
}
