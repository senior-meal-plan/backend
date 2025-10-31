package io.github.tlsdla1235.seniormealplan.controller;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.dto.user.WhoAmIDto;
import io.github.tlsdla1235.seniormealplan.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class userController {
    private final UserService userService;

    @GetMapping("/temp")
    public ResponseEntity<WhoAmIDto> whoamI() {
        User u = new User();
        u.setUserId(1L);
        WhoAmIDto whoAmIDto = userService.whoAmI(u);
        return ResponseEntity.ok(whoAmIDto);
    }

}
