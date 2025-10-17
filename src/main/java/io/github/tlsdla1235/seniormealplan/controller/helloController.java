package io.github.tlsdla1235.seniormealplan.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloController {

    @PreAuthorize("hasRole('USER')")   // ← ROLE_USER만 접근 OK
    @GetMapping("/api/hello")
    public String hello() {
        return "hello world";
    }
}
