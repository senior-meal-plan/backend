package io.github.tlsdla1235.seniormealplan.service.user;

import io.github.tlsdla1235.seniormealplan.config.JwtService;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.enumPackage.Role;
import io.github.tlsdla1235.seniormealplan.dto.auth.AuthResponse;
import io.github.tlsdla1235.seniormealplan.dto.auth.LoginRequest;
import io.github.tlsdla1235.seniormealplan.dto.auth.RegisterRequest;
import io.github.tlsdla1235.seniormealplan.repository.UserRepository;
import lombok.Getter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthService(UserRepository r, PasswordEncoder e,
                       AuthenticationManager m, JwtService j) {
        this.repo = r; this.encoder = e; this.authManager = m; this.jwt = j;
    }

    public void register(RegisterRequest req) {
        // userInputId 중복 체크
        if (repo.existsByUserInputId(req.userInputId())) {
            throw new IllegalArgumentException("이미 가입된 사용자 ID");
        }
        var u = new io.github.tlsdla1235.seniormealplan.domain.User();
        u.setUserInputId(req.userInputId());
        u.setPassword(encoder.encode(req.password()));
        u.setUserName(req.userName());
        u.setRole(Role.USER);                 // ★ 클라이언트 전달값 사용
        u.setUserGender(req.userGender());     // ★ 클라이언트 전달값 사용
        u.setUserHeight(req.userHeight());
        u.setUserWeight(req.userWeight());
        u.setAge(req.userAge());
        repo.save(u); // createdAt은 @PrePersist가 채움
    }

    public AuthResponse login(LoginRequest req) {
        // 인증 시도 (ID/패스워드)
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.userInputId(), req.password())
        );

        // 토큰 발급
        User u = repo.findByUserInputId(req.userInputId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        String token = jwt.generateToken(
                String.valueOf(u.getUserId()),
                Map.of(
                        "uid",  u.getUserId(),
                        "uin",  u.getUserInputId(),
                        "role", u.getRole().name()
                )
        );
        return new AuthResponse(token);
    }



}
