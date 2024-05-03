package com.api.whatsapp.Swagger;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import springfox.documentation.builders.ApiInfoBuilder;
        import springfox.documentation.builders.PathSelectors;
        import springfox.documentation.builders.RequestHandlerSelectors;
        import springfox.documentation.service.ApiInfo;
        import springfox.documentation.service.ApiKey;
        import springfox.documentation.service.SecurityReference;
        import springfox.documentation.spi.DocumentationType;
        import springfox.documentation.spi.service.contexts.SecurityContext;
        import springfox.documentation.spring.web.plugins.Docket;
        import springfox.documentation.swagger2.annotations.EnableSwagger2;
        import java.util.Arrays;
        import java.util.List;

        @Configuration
        @EnableSwagger2
        public class SwaggerConfig {

        @Bean
        public Docket apiDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .securitySchemes(Arrays.asList(apiKey()))
        .securityContexts(Arrays.asList(securityContext()));

        }

        private ApiInfo apiInfo() {

        return new ApiInfoBuilder().title("API Title").version("1.0").description("API Description").build();

        }

        private ApiKey apiKey() {

        return new ApiKey("JWT", "Authorization", "header");

        }

        private SecurityContext securityContext() {

        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();

        }

        List<SecurityReference> defaultAuth() {

        springfox.documentation.service.AuthorizationScope[] authorizationScopes = new springfox.documentation.service.AuthorizationScope[1];
        authorizationScopes[0] = new springfox.documentation.service.AuthorizationScope("global", "accessEverything");
        return Arrays.asList(new springfox.documentation.service.SecurityReference("JWT", authorizationScopes));

        }

        }

