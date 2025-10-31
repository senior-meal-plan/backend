package io.github.tlsdla1235.seniormealplan.service.user;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.preference.AiManagementGoal;
import io.github.tlsdla1235.seniormealplan.dto.user.UserTopicDto;
import io.github.tlsdla1235.seniormealplan.repository.AiManagementGoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiManagementGoalService {
    private final AiManagementGoalRepository aiManagementGoalRepository;

    public List<UserTopicDto> getAiSelectedTopics(User user) {
        List<AiManagementGoal> aiGoal = aiManagementGoalRepository.findAllByUserWithHealthTopic(user);
        List<UserTopicDto> userTopicDtos = aiGoal.stream().map(ag->UserTopicDto.fromAi(ag.getHealthTopic())).toList();

        log.info("사용자 id :{} 에 대한 ai가 관리해주는 Topics: {}",user.getUserId(),userTopicDtos);
        return userTopicDtos;
    }

}
