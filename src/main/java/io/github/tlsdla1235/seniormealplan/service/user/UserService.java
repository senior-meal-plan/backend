package io.github.tlsdla1235.seniormealplan.service.user;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.dto.user.UserTopicDto;
import io.github.tlsdla1235.seniormealplan.dto.user.WhoAmIDto;
import io.github.tlsdla1235.seniormealplan.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTopicService userTopicService;


    public WhoAmIDto whoAmI(User user) {
        user = userRepository.findById(user.getUserId()).orElseThrow(EntityNotFoundException::new);
        List<UserTopicDto> userTopic = userTopicService.getUserTopicsFromUser(user);
        WhoAmIDto whoAmI = WhoAmIDto.from(user, userTopic);

        log.info("사용자 id:{}에 대한 WhoAmI함수 호출 결과 :{}", user.getUserId(), whoAmI);

        return whoAmI;
    }
}
