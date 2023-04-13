package ru.practicum.statistics.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statistics.stats.model.Hit;
import ru.practicum.statistics.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Integer> {
    @Query("SELECT new ru.practicum.statistics.stats.model.Stats(s.app, s.uri, COUNT(s)) " +
            "FROM Hit s WHERE s.uri IN :uris AND s.viewDate >= :from AND s.viewDate <= :to " +
            "GROUP BY s.app, s.uri ORDER BY COUNT(s) DESC")
    List<Stats> findStatsList(List<String> uris, LocalDateTime from, LocalDateTime to);

    @Query("SELECT new ru.practicum.statistics.stats.model.Stats(s.app, s.uri, COUNT(s)) " +
            "FROM Hit s WHERE s.viewDate >= :from AND s.viewDate <= :to " +
            "GROUP BY s.app, s.uri ORDER BY COUNT(s) DESC")
    List<Stats> findAllStatsList(LocalDateTime from, LocalDateTime to);

    @Query("SELECT new ru.practicum.statistics.stats.model.Stats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Hit s WHERE s.uri IN :uris AND s.viewDate >= :from AND s.viewDate <= :to " +
            "GROUP BY s.app, s.uri ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<Stats> findStatsListUniqueIp(List<String> uris, LocalDateTime from, LocalDateTime to);

    @Query("SELECT new ru.practicum.statistics.stats.model.Stats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Hit s WHERE s.viewDate >= :from AND s.viewDate <= :to " +
            "GROUP BY s.app, s.uri ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<Stats> findAllStatsListUniqueIp(LocalDateTime from, LocalDateTime to);
}
