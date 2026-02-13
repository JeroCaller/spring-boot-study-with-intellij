package com.jerocaller.AuthTokenSecurity.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>참고자료</p>
 * <ul>
 *     <li>
 *         <a href="https://springdoc.org/faq.html#_how_do_i_add_authorization_header_in_requests">
 *             springdoc.org
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://luvstudy.tistory.com/291">
 *             springdoc swagger ui에 authorize 사용해 보기
 *         </a>
 *     </li>
 * </ul>
 */
@Configuration
public class SwaggerConfig {

    /**
     * <p>
     *     swagger에서 요청 헤더에 `Authorization: Bearer (JWT)`가
     *     자동으로 추가되도록 하는 설정
     * </p>
     *
     * @return
     */
    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "jwtAuth";
        SecurityScheme securityScheme = new SecurityScheme()
            .name(jwtSchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
        Components components = new Components()
            .addSecuritySchemes(jwtSchemeName, securityScheme);
        return new OpenAPI()
            .components(components);
    }
}
