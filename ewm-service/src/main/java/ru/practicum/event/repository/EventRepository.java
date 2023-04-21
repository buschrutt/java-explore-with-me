package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT DISTINCT e FROM Event e " +
            "WHERE (e.annotation LIKE COALESCE(:text, e.annotation) OR e.description LIKE COALESCE(:text, e.description)) " +
            "AND (:categories IS NULL OR e.category IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND e.eventDate >= COALESCE(:rangeStart, e.eventDate) " +
            "AND e.eventDate <= COALESCE(:rangeEnd, e.eventDate) " +
            "ORDER BY e.eventDate DESC")
    List<Event> findEventList(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category IN :categories) " +
            "AND (e.eventDate >= COALESCE(:rangeStart, e.eventDate)) " +
            "AND (e.eventDate <= COALESCE(:rangeEnd, e.eventDate)) " +
            "ORDER BY e.eventDate DESC")
    List<Event> findEventAdminList(List<Integer> users, List<State> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findAllByInitiator(Integer user, Pageable pageable);

    List<Event> findAllByIdIn(List<Integer> Ids);

    Event findEventByIdAndInitiator(Integer id, Integer initiator);
}
