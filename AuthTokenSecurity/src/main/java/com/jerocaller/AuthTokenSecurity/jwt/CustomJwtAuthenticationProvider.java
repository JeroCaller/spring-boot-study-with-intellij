package com.jerocaller.AuthTokenSecurity.jwt;

import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import com.jerocaller.libs.spoonsuits.web.jwt.impl.DefaultJwtAuthenticationProviderImpl;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * <p>TODO - {@code @RequiredArgsConstructor} 적용 불가 문제</p>
 * <p>
 *     {@code @RequiredArgsConstructor}를 이 클래스에 적용할 수 없음. 메시지에 따르면,
 *     <code>Lombok은 기본 클래스에 디폴트 생성자가 필요합니다</code>라고 함. 즉, 상위 클래스에서
 *     기본 생성자가 있어야 했는데 상위 클래스에 현재 기본 생성자가 없는 관계로 해당 롬복 어노테이션이 적용안됨.
 *     추후 해당 라이브러리에서 수정 필요.
 * </p>
 */
@Primary
@Component
@Slf4j
public class CustomJwtAuthenticationProvider extends DefaultJwtAuthenticationProviderImpl {

    @Autowired
    public CustomJwtAuthenticationProvider(JwtProperties jwtProperties) {
        super(jwtProperties);
        log.info("자식 클래스 {}가 Spring bean으로 등록되었습니다.", this.getClass().getSimpleName());
    }

    @Override
    public boolean validateToken(String token) throws JwtException {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }
    }
}
