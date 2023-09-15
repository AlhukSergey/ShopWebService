package by.teachmeskills.shopwebservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(security = {@SecurityRequirement(name = "Bearer Authentication")})
public class OpenApiConfig {

    /**
     * Group beans
     */
    @Bean
    public GroupedOpenApi publicCategoryApi() {
        return GroupedOpenApi.builder()
                .group("categories")
                .pathsToMatch("/**/category/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicProductApi() {
        return GroupedOpenApi.builder()
                .group("products")
                .pathsToMatch("/**/product/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicImageApi() {
        return GroupedOpenApi.builder()
                .group("images")
                .pathsToMatch("/**/image/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicUserApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .pathsToMatch("/**/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicOrderApi() {
        return GroupedOpenApi.builder()
                .group("orders")
                .pathsToMatch("/**/order/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicShopCartApi() {
        return GroupedOpenApi.builder()
                .group("shopCarts")
                .pathsToMatch("/**/shopCart/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi(
            @Value("${application.title}") String appTitle,
            @Value("${application.description}") String appDescription,
            @Value("${application.version}") String appVersion,
            @Value("${application.license-name}") String licenseName,
            @Value("${application.license-address}") String licenseAddress,
            @Value("${application.developer-name}") String developerName,
            @Value("${application.developer-email}") String developerEmail,
            @Value("${application.dev-server-description}") String devServerDescription,
            @Value("${application.dev-server-address}") String devServerAddress,
            @Value("${application.beta-server-description}") String betaServerDescription,
            @Value("${application.beta-server-address}") String betaServerAddress,
            @Value("${application.security-schema}") String securitySchema
    ) {
        return new OpenAPI()
                .info(new Info()
                        .title(appTitle)
                        .version(appVersion)
                        .description(appDescription)
                        .license(new License().name(licenseName)
                                .url(licenseAddress))
                        //Contact information about organization of exposed API
                        .contact(new Contact().name(developerName)
                                .email(developerEmail)))
                .servers(List.of(new Server().url(devServerAddress)
                                .description(devServerDescription),
                        new Server().url(betaServerAddress)
                                .description(betaServerDescription)))
                .components(new Components()
                        .addSecuritySchemes(securitySchema, createSecurityScheme()));

    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme().name("Shop")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}

