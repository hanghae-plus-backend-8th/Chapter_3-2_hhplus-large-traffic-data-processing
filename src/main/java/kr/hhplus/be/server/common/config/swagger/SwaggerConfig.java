package kr.hhplus.be.server.common.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
        info = @Info(title = "항해플러스 백엔드 2주차 과제 - 서버 구축", version = "1.0",
                contact = @Contact(name = "Dev_sHu", email = "akgkfk3@naver.com")),
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 서버")
        }
)
public class SwaggerConfig {

    @Bean
    GroupedOpenApi user() {
        String[] pathsToMatch = {
                "/api/v1/**"
        };

        return GroupedOpenApi.builder()
                .group("user")
                .displayName("사용자")
                .pathsToMatch(pathsToMatch)
                .build();
    }
}
