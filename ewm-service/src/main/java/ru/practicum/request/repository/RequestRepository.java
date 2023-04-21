package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findRequestByStatus(String status);

    List<Request> findRequestsByEvent(Integer event);

    List<Request> findRequestsByEventAndStatus(Integer event, String status);

    List<Request> findRequestsByStatusAndIdIn(String status, List<Integer> RequestIds);

    List<Request> findRequestsByRequester(Integer userId);

    Request findRequestByRequesterAndEvent(Integer userId, Integer eventId);

    Request findRequestByRequesterAndId(Integer userId, Integer requestId);
}
