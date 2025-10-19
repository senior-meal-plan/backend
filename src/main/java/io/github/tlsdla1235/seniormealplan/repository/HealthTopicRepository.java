package io.github.tlsdla1235.seniormealplan.repository;

import io.github.tlsdla1235.seniormealplan.domain.preference.HealthTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthTopicRepository extends JpaRepository<HealthTopic, Integer> {
}
