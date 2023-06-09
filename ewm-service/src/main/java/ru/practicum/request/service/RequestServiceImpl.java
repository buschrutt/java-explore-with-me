package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.error.model.EwmExceptionModel;
import ru.practicum.error.EwmException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.dto.RequestUpdateStatusDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestDtoMapper;
import ru.practicum.request.dto.RequestStatusDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<RequestDto> findUserRequests(Integer userId) throws EwmException {
        userRepository.findById(userId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("User id:" + userId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        List<Request> requestList = requestRepository.findRequestsByRequester(userId);
        return requestList.stream().map(RequestDtoMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public RequestDto addRequest(Integer userId, Integer eventId) throws EwmException {
        if (!Objects.equals(eventRepository.findById(eventId).orElseThrow().getState(), "PUBLISHED")) {
            throw new EwmException(new EwmExceptionModel("The event was not PUBLISHED", "The required object was not found.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        String requestState = newRequestValidation(userId, eventId);
        Request request = Request.builder()
                .requester(userId)
                .event(eventId)
                .created(LocalDateTime.now())
                .status(requestState)
                .build();
        Request request0 = requestRepository.save(request);
        return RequestDtoMapper.toRequestDto(request0);
    }

    @Override
    public RequestDto cancelRequest(Integer userId, Integer requestId) throws EwmException {
        Request request = requestRepository.findRequestByRequesterAndId(userId, requestId);
        if (request == null) {
            throw new EwmException(new EwmExceptionModel("No parametrized request found", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.NOT_FOUND));
        }
        request.setStatus("CANCELED");
        return RequestDtoMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> findEventRequests(Integer userId, Integer eventId) {
        List<Request> requestList = requestRepository.findRequestsByEvent(eventId);
        return requestList.stream().map(RequestDtoMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public RequestStatusDto updateRequestStatus(Integer userId, Integer eventId, RequestUpdateStatusDto updateStatusDto) throws EwmException {
        List<Integer> requestIds = updateStatusDto.getRequestIds();
        String newStatus = updateStatusDto.getStatus();
        Event event = eventRepository.findById(eventId).orElseThrow();
        for (Integer requestId : requestIds) {
            Request request = requestRepository.findById(requestId).orElseThrow();
            updateRequestValidation(eventId, updateStatusDto, event, request);
            if (Objects.equals(newStatus, "CONFIRMED") && event.getParticipantLimit() != 0) {
                checkIfLimitReached(event, eventId);
            }
            request.setStatus(newStatus);
            requestRepository.save(request);
        }
        List<Request> confirmedRequests = requestRepository.findRequestsByStatusAndIdIn("CONFIRMED", requestIds);
        List<Request> rejectedRequests = requestRepository.findRequestsByStatusAndIdIn("REJECTED", requestIds);
        return RequestDtoMapper.toRequestStatesDto(
                confirmedRequests.stream().map(RequestDtoMapper::toRequestDto).collect(Collectors.toList()),
                rejectedRequests.stream().map(RequestDtoMapper::toRequestDto).collect(Collectors.toList())
        );
    }

    // %%%%%%%%%% %%%%%%%%%% SUPPORTING
    String newRequestValidation(Integer userId, Integer eventId) throws EwmException {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("Event id:" + eventId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        if (Objects.equals(event.getInitiator(), userId)) {
            throw new EwmException(new EwmExceptionModel("Requester same as event initiator", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (requestRepository.findRequestByRequesterAndEvent(userId, eventId) != null) {
            throw new EwmException(new EwmExceptionModel("Duplicated request", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (!event.getRequestModeration() && event.getParticipantLimit() <= requestRepository.findRequestsByEventAndStatus(eventId, "CONFIRMED").size()) {
            throw new EwmException(new EwmExceptionModel("ParticipantLimit reached0", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (event.getRequestModeration()) {
            return "PENDING";
        } else {
            return "CONFIRMED";
        }
    }

    void updateRequestValidation(Integer eventId, RequestUpdateStatusDto updateStatusDto, Event event, Request request) throws EwmException {
        if (!Objects.equals(request.getStatus(), "PENDING")) {
            throw new EwmException(new EwmExceptionModel("Request status is not PENDING", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (event.getParticipantLimit() <= requestRepository.findRequestsByEventAndStatus(eventId, "CONFIRMED").size()) {
            throw new EwmException(new EwmExceptionModel("ParticipantLimit reached1", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (Objects.equals(request.getStatus(), "CONFIRMED") && Objects.equals(updateStatusDto.getStatus(), "REJECTED")) {
            throw new EwmException(new EwmExceptionModel("Can't reject a request that already CONFIRMED", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
    }

    void checkIfLimitReached(Event event, Integer eventId) throws EwmException {
        if (event.getParticipantLimit() <= requestRepository.findRequestsByEventAndStatus(eventId, "CONFIRMED").size()) {
            for (Request request : requestRepository.findRequestsByEventAndStatus(eventId, "PENDING")) {
                request.setStatus("REJECTED");
                requestRepository.save(request);
            }
            throw new EwmException(new EwmExceptionModel("ParticipantLimit reached -All PENDING were REJECTED", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
    }
}
