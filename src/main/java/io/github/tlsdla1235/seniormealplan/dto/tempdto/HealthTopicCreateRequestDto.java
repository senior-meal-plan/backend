package io.github.tlsdla1235.seniormealplan.dto.tempdto;

import io.github.tlsdla1235.seniormealplan.domain.enumPackage.TopicType;
import io.github.tlsdla1235.seniormealplan.domain.preference.HealthTopic;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HealthTopicCreateRequestDto {

    private TopicType topicType;
    private String name;
    private String description;

    // DTO를 Entity로 변환하는 메서드
    public HealthTopic toEntity() {
        return new HealthTopic(this.topicType, this.name, this.description);
    }
}