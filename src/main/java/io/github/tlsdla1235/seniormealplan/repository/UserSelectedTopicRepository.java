package io.github.tlsdla1235.seniormealplan.repository;

import io.github.tlsdla1235.seniormealplan.domain.preference.UserSelectedTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSelectedTopicRepository extends JpaRepository<UserSelectedTopic, Long> {
}
