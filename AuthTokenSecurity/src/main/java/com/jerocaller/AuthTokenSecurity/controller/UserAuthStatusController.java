package com.jerocaller.AuthTokenSecurity.controller;

import com.jerocaller.AuthTokenSecurity.business.UserAuthStatusService;
import com.jerocaller.AuthTokenSecurity.data.dto.response.RestResponse;
import com.jerocaller.AuthTokenSecurity.data.dto.response.UserAuthStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     Spring Security에서 인증 정보 저장 및 읽기 기능에 대한 테스트를 위한 컨트롤러.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/auth/status")
public class UserAuthStatusController {
    private final UserAuthStatusService userAuthStatusService;

    /**
     * <p>
     *     <code>StatusWithContextService</code>로 테스트.
     * </p>
     *
     * @return
     */
    @GetMapping("/security/context")
    public ResponseEntity<RestResponse.DetailedRestResponse<UserAuthStatusResponse>> getUserStatus() {
        UserAuthStatusResponse userAuthStatusResponse = userAuthStatusService.getUserAuthStatus();
        return RestResponse.success(userAuthStatusResponse);
    }

    /**
     * <p>
     *     현재 인증된 사용자 정보(유저네임)를 잘 가져오는지 테스트.
     * </p>
     * <p>
     *     <code>StatusWithAuthAnnoService</code>로 테스트.
     * </p>
     * @param username
     * @return
     */
    @GetMapping("/annotation/principal")
    public ResponseEntity<RestResponse.DetailedRestResponse<UserAuthStatusResponse>> getUserStatus(
        @AuthenticationPrincipal String username
    ) {
        UserAuthStatusResponse userAuthStatusResponse = userAuthStatusService
            .getUserAuthStatus(username);
        return RestResponse.success(userAuthStatusResponse);
    }

    /**
     * <p>
     *     <code>StatusWithContextService</code>로 테스트.
     * </p>
     * <p>
     *     {@code @PreAuthorize}와 같은 메서드 단위의 security를 사용하고자 할 경우,
     *     먼저 {@code @Configuration}이 적용된 설정 클래스에 {@code EnableMethodSecurity} 어노테이션을
     *     등록해야 한다.
     * </p>
     * <p>
     *     메서드 수준에서의 인증 방식을 이용할 때 인증에 실패하면
     *     <code>
     *         org.springframework.security.authorization.AuthorizationDeniedException: Access Denied
     *     </code>
     *     라는 예외가 발생함. 메서드 수준 인증에서는 <code>requestMatcher</code>와 같은 필터 기반 인증이
     *     아니기에, 이러한 예외를 처리하려면 {@code @~ControllerAdvice} 수준에서 처리해야함.
     * </p>
     *
     * @return
     */
    @GetMapping("/annotation/preauth")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RestResponse.DetailedRestResponse<UserAuthStatusResponse>> getUserStatusByPreAuth() {
        UserAuthStatusResponse userAuthStatusResponse = userAuthStatusService.getUserAuthStatus();
        return RestResponse.success(userAuthStatusResponse);
    }
}
