package io.github.tlsdla1235.seniormealplan.service.user;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.preference.AiManagementGoal;
import io.github.tlsdla1235.seniormealplan.domain.preference.UserSelectedTopic;
import io.github.tlsdla1235.seniormealplan.dto.user.UserTopicDto;
import io.github.tlsdla1235.seniormealplan.repository.AiManagementGoalRepository;
import io.github.tlsdla1235.seniormealplan.repository.UserSelectedTopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTopicService {
    private final UserSelectedTopicRepository userSelectedTopicRepository;
    private final AiManagementGoalService aiManagementGoalService;

    public List<UserTopicDto> getUserTopicsFromUser(User user) {
        List<UserSelectedTopic> userSelectedTopics = userSelectedTopicRepository.findAllByUserWithHealthTopic(user);
        List<UserTopicDto> userTopicDtos = userSelectedTopics.stream().map(ut->UserTopicDto.fromUser(ut.getHealthTopic())).toList();
        log.info("사용자 id :{} 에 대한 ai가 관리해주는 Topics: {}",user.getUserId(),userTopicDtos);
        List<UserTopicDto> aiTopicDtos = aiManagementGoalService.getAiSelectedTopics(user);

        List<UserTopicDto> combinedList = Stream.concat(userTopicDtos.stream(), aiTopicDtos.stream())
                .collect(Collectors.toList());
        log.info("사용자 id: {}의 통합 Topics ({}개): {}", user.getUserId(), combinedList.size(), combinedList);
        return combinedList;
    }

}
