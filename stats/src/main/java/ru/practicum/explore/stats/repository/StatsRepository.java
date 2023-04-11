package ru.practicum.explore.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.stats.model.Stats;

public interface StatsRepository extends JpaRepository<Stats, Integer> {
}
