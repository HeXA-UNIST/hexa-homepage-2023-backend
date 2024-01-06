package pro.hexa.backend.main.api.common.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class OpenApiConfiguration {
    public static final String REFRESH_AUTHORIZATION_STRING = "Refresh-" + AUTHORIZATION;

    @Bean
    public OpenAPI openAPI(
        @Value("${version}") String version
    ) {
        Info info = new Info()
            .title("HeXA API")
            .version(version);

        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name(AUTHORIZATION);

        SecurityScheme refreshSecurityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name(REFRESH_AUTHORIZATION_STRING);

        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(securityScheme.getName(), securityScheme)
                .addSecuritySchemes(refreshSecurityScheme.getName(), refreshSecurityScheme))
            .info(info);
    }
}
