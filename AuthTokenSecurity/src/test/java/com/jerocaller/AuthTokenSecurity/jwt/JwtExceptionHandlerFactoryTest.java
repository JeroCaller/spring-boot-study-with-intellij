package com.jerocaller.AuthTokenSecurity.jwt;

import com.jerocaller.AuthTokenSecurity.config.JwtConfig;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import com.jerocaller.AuthTokenSecurity.jwt.impl.DefaultJwtExceptionHandler;
import com.jerocaller.AuthTokenSecurity.jwt.impl.ExpiredJwtExceptionHandler;
import com.jerocaller.AuthTokenSecurity.jwt.impl.JwtIllegalArgumentExceptionHandler;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import({
    JwtConfig.class,
    JwtExceptionHandlerFactory.class,
    DefaultJwtExceptionHandler.class,
    ExpiredJwtExceptionHandler.class,
    JwtIllegalArgumentExceptionHandler.class
})
@Slf4j
class JwtExceptionHandlerFactoryTest {

    @Autowired
    private JwtExceptionHandlerFactory factory;

    @Autowired
    private List<JwtExceptionHandler> handlers;

    @MockitoBean
    private HttpServletResponse response;

    @Test
    @DisplayName("핸들러 등록 테스트.")
    void areHandlersRegisteredInFactory() {
        assertThat(handlers.size()).isEqualTo(3);

        log.info("핸들러 등록 정보");

        for (JwtExceptionHandler handler : handlers) {
            Object handleResult = handler.handle(response);
            assertThat(handleResult instanceof RestResponse.DetailedRestResponse<?>).isTrue();

            RestResponse.DetailedRestResponse<?> detailedRestResponse =
                (RestResponse.DetailedRestResponse<?>) handleResult;
            String handlerName = handler.getClass().getSimpleName();
            log.info(
                "{} - {}: http status: {}, code: {}, message: {}",
                handler.getJwtExceptionClass().getSimpleName(),
                handlerName,
                detailedRestResponse.getHttpStatus(),
                detailedRestResponse.getCode(),
                detailedRestResponse.getMessage()
            );
        }

        log.info("======");

        assertThat(factory.getHandler(JwtException.class).getClass().getSimpleName())
            .isEqualTo(DefaultJwtExceptionHandler.class.getSimpleName());
    }
}