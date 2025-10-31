package io.github.tlsdla1235.seniormealplan.exception;


import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("리소스를 찾을 수 없습니다: {}", ex.getMessage());

        // 클라이언트에게 반환할 에러 메시지
        Map<String, String> errorResponse = Map.of(
                "status", "404",
                "message", "요청한 리소스를 찾을 수 없습니다."
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * IllegalStateException을 처리 (HTTP 409 Conflict 또는 400 Bad Request)
     * 리포트 상태가 COMPLETE가 아닐 때 발생시킨 예외를 처리합니다.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        log.warn("잘못된 상태 접근: {}", ex.getMessage());

        Map<String, String> errorResponse = Map.of(
                "status", "409", // 상태 충돌(Conflict)
                "message", ex.getMessage() // 서비스에서 던진 메시지 ("아직 처리 중이거나...")
        );

        // 409 Conflict가 의미상 적절해 보입니다. (400 Bad Request도 가능)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    /**
     * 기타 모든 서버 내부 예외 처리 (HTTP 500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        log.error("처리되지 않은 예외 발생", ex); // 500 에러는 스택 트레이스를 남겨야 합니다.

        Map<String, String> errorResponse = Map.of(
                "status", "500",
                "message", "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
