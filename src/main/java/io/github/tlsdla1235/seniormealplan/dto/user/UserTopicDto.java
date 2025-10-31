package io.github.tlsdla1235.seniormealplan.dto.user;

import io.github.tlsdla1235.seniormealplan.domain.enumPackage.TopicType;
import io.github.tlsdla1235.seniormealplan.domain.preference.HealthTopic;

public record UserTopicDto(
        Long topicId,
        TopicType topicType,
        String name,
        String description,
        String source
) {
    public static UserTopicDto fromUser(HealthTopic healthTopic) {
        return new UserTopicDto(
                healthTopic.getTopicId(),
                healthTopic.getTopicType(),
                healthTopic.getName(),
                healthTopic.getDescription(),
                "user"
        );
    }

    public static UserTopicDto fromAi(HealthTopic healthTopic) {
        return new UserTopicDto(
                healthTopic.getTopicId(),
                healthTopic.getTopicType(),
                healthTopic.getName(),
                healthTopic.getDescription(),
                "ai"
        );
    }
}
