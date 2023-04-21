package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.error.model.ewmExceptionModel;
import ru.practicum.error.ewmException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.dto.RequestChangeStatesDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestDtoMapper;
import ru.practicum.request.dto.RequestStatesDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestChangeStates;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<RequestDto> findUserRequests(Integer userId) throws ewmException {
        userRepository.findById(userId).orElseThrow(() ->
                new ewmException(new ewmExceptionModel("User id:" + userId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        List<Request> requestList = requestRepository.findRequestsByRequester(userId);
        return requestList.stream().map(RequestDtoMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public RequestDto addRequest(Integer userId, Integer eventId) throws ewmException {
        String requestState = NewRequestValidation(userId, eventId);
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
    public RequestDto cancelRequest(Integer userId, Integer requestId) throws ewmException {
        Request request = requestRepository.findRequestByRequesterAndId(userId, requestId);
        if (request == null) {
            throw new ewmException(new ewmExceptionModel("No parametrized request found", "Integrity constraint has been violated.", "CONFLICT",
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
    public RequestStatesDto changeRequestStates(Integer userId, Integer eventId, RequestChangeStatesDto changeStatesDto) throws ewmException {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getRequestModeration() || event.getParticipantLimit() != 0) {
            RequestChangeStates changeStates = RequestDtoMapper.toRequestChangeStates(changeStatesDto);
            String state = changeStates.getStatus();
            for (Integer requestId : changeStates.getRequestIds()) {
                Request request = requestRepository.findById(requestId).orElseThrow();
                if (!Objects.equals(request.getStatus(), "PENDING")) {
                    throw new ewmException(new ewmExceptionModel("Request status is not PENDING", "Integrity constraint has been violated.", "CONFLICT",
                            HttpStatus.NOT_FOUND));
                }
                if (Objects.equals(state, "CONFIRMED")) {
                    CheckIfLimitReached(eventId);
                }
                request.setStatus(state);
                requestRepository.save(request);
            }
        }
        List<Request> confirmedRequests = requestRepository.findRequestsByEventAndStatus(eventId, "CONFIRMED");
        List<Request> rejectedRequests = requestRepository.findRequestsByEventAndStatus(eventId, "REJECTED");
        return RequestDtoMapper.toRequestStatesDto(
                confirmedRequests.stream().map(RequestDtoMapper::toRequestDto).collect(Collectors.toList()),
                rejectedRequests.stream().map(RequestDtoMapper::toRequestDto).collect(Collectors.toList())
        );
    }

    // %%%%%%%%%% %%%%%%%%%% SUPPORTING
    String RequestValidation(Integer userId, Integer eventId) throws ewmException {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ewmException(new ewmExceptionModel("Event id:" + eventId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        if (!Objects.equals(event.getState(), "PUBLISHED")) {
            throw new ewmException(new ewmExceptionModel("Event id:" + eventId + " has not been published", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (Objects.equals(event.getInitiator(), userId)) {
            throw new ewmException(new ewmExceptionModel("Requester same as event initiator", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (requestRepository.findRequestByRequesterAndEvent(userId, eventId) != null) {
            throw new ewmException(new ewmExceptionModel("Duplicated request", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (event.getParticipantLimit() >= requestRepository.findRequestsByEvent(eventId).size()) {
            throw new ewmException(new ewmExceptionModel("ParticipantLimit reached", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (event.getRequestModeration()) {
            return "PENDING";
        } else {
            return "CONFIRMED";
        }
    }

    String NewRequestValidation(Integer userId, Integer eventId) throws ewmException {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ewmException(new ewmExceptionModel("Event id:" + eventId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        if (Objects.equals(event.getInitiator(), userId)) {
            throw new ewmException(new ewmExceptionModel("Requester same as event initiator", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (requestRepository.findRequestByRequesterAndEvent(userId, eventId) != null) {
            throw new ewmException(new ewmExceptionModel("Duplicated request", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (event.getParticipantLimit() <= requestRepository.findRequestsByEvent(eventId).size()) {
            throw new ewmException(new ewmExceptionModel("ParticipantLimit reached", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
        if (event.getRequestModeration()) {
            return "PENDING";
        } else {
            return "CONFIRMED";
        }
    }

    void CheckIfLimitReached(Integer eventId) throws ewmException {
        if (eventRepository.findById(eventId).get().getParticipantLimit() == requestRepository.findRequestsByEvent(eventId).size()) {
            for (Request request : requestRepository.findRequestsByEventAndStatus(eventId, "PENDING")) {
                request.setStatus("REJECTED");
                requestRepository.save(request);
            }
            throw new ewmException(new ewmExceptionModel("ParticipantLimit reached -All PENDING were REJECTED", "Integrity constraint has been violated.", "CONFLICT",
                    HttpStatus.CONFLICT));
        }
    }
}
