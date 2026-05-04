package com.jerocaller.AuthTokenSecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>각종 설정들을 하는 곳</p>
 * <p>별도의 모듈로 분리하여 작성하기엔 그 코드가 짧은 설정들을 모아 등록한다.</p>
 */
@Configuration
public class BasicConfig {

    /**
     * <p>
     *     org.springframework.http.converter.HttpMessageConversionException: Type definition error: [simple type, class java.time.LocalDateTime]
     *     에러 해결을 위해 JavaTimeModule 객체를 등록함.
     * </p>
     * <p>참고자료들</p>
     * <ul>
     *     <li>
     *         <a href="https://woo-chang.tistory.com/75">https://woo-chang.tistory.com/75</a>
     *     </li>
     *     <li>
     *         <a href="https://pasudo123.tistory.com/362">https://pasudo123.tistory.com/362</a>
     *     </li>
     * </ul>
     *
     * @return
     */
    @Bean
    public ObjectMapper getDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // 날짜를 JSON에서 문자열로 표시하도록 한다.
        // 이 코드가 없다면 JSON 응답에서 날짜가 지저분한 리스트 형태로 출력된다.
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
