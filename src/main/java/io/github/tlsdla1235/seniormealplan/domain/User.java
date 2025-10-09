package io.github.tlsdla1235.seniormealplan.domain;


import io.github.tlsdla1235.seniormealplan.domain.enumPackage.UserGenderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name", nullable = false) // nullable=false는 NOT NULL 제약조건
    private String userName;

    @Column(name = "created_at", updatable = false) // updatable=false는 한번 생성되면 수정되지 않음
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING) // Enum 타입을 문자열 자체로 DB에 저장
    @Column(name = "user_gender")
    private UserGenderType userGender;

    @Column(name = "user_height")
    private BigDecimal userHeight; // 정밀한 소수점 계산을 위해 BigDecimal 사용

    @Column(name = "user_weight")
    private BigDecimal userWeight;

    @Column(name = "user_last_login")
    private LocalDateTime userLastLogin;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

