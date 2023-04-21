package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatisticsClient;
import ru.practicum.domain.HitDto;
import ru.practicum.error.EwmException;
import ru.practicum.error.model.EwmExceptionModel;
import ru.practicum.event.dto.*;
import ru.practicum.event.dto.EventDtoMapper;
import ru.practicum.event.dto.LocationDtoMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.enums.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.request.model.RequestStatuses;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.dto.UserDtoMapper;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final StatisticsClient statsClient;

    @Override
    public List<EventDto> findEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd,
                                     Boolean onlyAvailable, String sort, Integer from, Integer size, String ip) {
        LocalDateTime fromTime = rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime toTime = rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null;
        List<Event> eventList = eventRepository.findEventList(text, categories, paid, fromTime, toTime);
        List<EventDto> eventDtoList = new ArrayList<>();
        for (Event event : eventList) {
            if (!onlyAvailable || event.getParticipantLimit() < requestRepository.findRequestsByEvent(event.getId()).size()) {
                eventDtoList.add(getEventDtoFunc(event));
            }
        }
        if (Objects.equals(sort, "VIEWS")){
            eventDtoList.sort(Comparator.comparing(EventDto::getViews));
        }
        if (eventDtoList.size() > from) {
            int toIndex = Math.min(from + size, eventDtoList.size());
            eventDtoList = eventDtoList.subList(from, toIndex);
        } else {
            eventDtoList = Collections.emptyList();
        }
        return eventDtoList;
    }

    @Override
    public EventDto getEventById(Integer id, String ip) {
        Event event = eventRepository.findById(id).orElseThrow();
        statsClient.postHit(HitDto.builder().app("evm-service").uri("/events/" + id).ip(ip).timestamp(LocalDateTime.now().format(formatter)).build());
        // %%%%% %%%%% %%%%%
        return getEventDtoFunc(event);
    }

    @Override
    public List<EventDto> findAllEvents(List<Integer> users, List<EventState> states, List<Integer> categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime fromTime = rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime toTime = rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null;
        List<Event> eventList = eventRepository.findEventAdminList(users, states, categories, fromTime, toTime, pageable);
        return eventList.stream().map(this::getEventDtoFunc).collect(Collectors.toList());
    }

    @Override
    public EventDto updateEvent(Integer eventId, UpdateEventRequestDto requestDto) throws EwmException {
        Event event = eventRepository.findById(eventId).orElseThrow();
        ValidateUpdateEventDto(requestDto, event);
        return convertEventToUpdatedDto(event, requestDto);
    }

    @Override
    public List<EventDto> findAllUserEvents(Integer userId, Integer from, Integer size) {
        List<EventDto> eventDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);
        for (Event event : eventRepository.findAllByInitiator(userId, pageable)) {
            eventDtoList.add(getEventDtoFunc(event));
        }
        return eventDtoList;
    }

    @Override
    public EventDto addEvent(Integer userId, PostEventDto postEventDto) throws EwmException {
        Event event = EventDtoMapper.toEvent(postEventDto, getLocationId(postEventDto));
        ValidateAddEventDto(postEventDto);
        event.setInitiator(userId);
        return getEventDtoFunc(eventRepository.save(event));
    }

    @Override
    public EventDto findUserEvent(Integer userId, Integer eventId) throws EwmException {
        Event event = eventRepository.findEventByIdAndInitiator(eventId, userId);
        if (event == null) {
            throw new EwmException(new EwmExceptionModel("Event id:" + eventId + " was not found", "The required object was not found.", "NOT_FOUND",
                    HttpStatus.NOT_FOUND));
        }
        return getEventDtoFunc(eventRepository.save(event));
    }

    @Override
    public EventDto updateUserEvent(Integer userId, Integer eventId, UpdateEventRequestDto patchEventDto) throws EwmException {
        Event event = eventRepository.findEventByIdAndInitiator(eventId, userId);
        ValidateUpdateUseEventDto(patchEventDto, event);
        return convertEventToUpdatedDtoPatch(event, patchEventDto);
    }

    // %%%%%%%%%% %%%%%%%%%% SUPPORTING
    EventDto  getEventDtoFunc(Event event) {
        UserShortDto initiatorDto = UserDtoMapper.toUserShortDto(userRepository.findById(event.getInitiator()).orElseThrow());
        LocationDto locationDto = LocationDtoMapper.toLocationDto(locationRepository.findById(event.getLocation()).orElseThrow());
        CategoryDto catDto = CategoryDtoMapper.toCategoryDto(categoryRepository.findById(event.getCategory()).orElseThrow());
        Integer confirmed = requestRepository.findRequestByStatus(RequestStatuses.CONFIRMED.toString()).size();
        Integer views = statsClient.getStats(event.getCreatedOn().format(formatter), LocalDateTime.now().format(formatter), List.of("/events/" + event.getId()), true).size();
        return EventDtoMapper.toEventDto(event, confirmed, initiatorDto, locationDto, views, catDto);
    }

    EventDto convertEventToUpdatedDto(Event event, UpdateEventRequestDto requestDto) {
        Event eventWUpdate = EventDtoMapper.toEventFromPatch(requestDto, null);
        eventWUpdate.setCreatedOn(null);
        return getEventDtoFunc(eventRepository.save(updateEventWithNotNullFields (event, eventWUpdate)));
    }

    EventDto convertEventToUpdatedDtoPatch(Event event, UpdateEventRequestDto patchEventDto) {
        Event eventWUpdate = EventDtoMapper.toEventFromPatch(patchEventDto, null);
        eventWUpdate.setCreatedOn(null);
        return getEventDtoFunc(eventRepository.save(updateEventWithNotNullFields (event, eventWUpdate)));
    }

    Event updateEventWithNotNullFields(Event event, Event eventWUpdate) {
        if (eventWUpdate.getCategory() != null) {
            event.setCategory(eventWUpdate.getCategory());
        }
        if (eventWUpdate.getAnnotation() != null) {
            event.setAnnotation(eventWUpdate.getAnnotation());
        }
        if (eventWUpdate.getDescription() != null) {
            event.setDescription(eventWUpdate.getDescription());
        }
        if (eventWUpdate.getEventDate() != null) {
            event.setEventDate(eventWUpdate.getEventDate());
        }
        if (eventWUpdate.getLocation() != null) {
            event.setLocation(eventWUpdate.getLocation());
        }
        if (eventWUpdate.getPaid() != null) {
            event.setPaid(eventWUpdate.getPaid());
        }
        if (eventWUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(eventWUpdate.getParticipantLimit());
        }
        if (eventWUpdate.getRequestModeration() != null) {
            event.setRequestModeration(eventWUpdate.getRequestModeration());
        }
        if (eventWUpdate.getState() != null) {
            event.setState(eventWUpdate.getState());
        }
        if (eventWUpdate.getTitle() != null) {
            event.setTitle(eventWUpdate.getTitle());
        }
        return event;
    }

    Integer getLocationId(PostEventDto postEventDto) {
        Integer locationId;
        Location location = locationRepository.findLocationByLatAndLon(postEventDto.getLocation().getLat(), postEventDto.getLocation().getLon());
        if (location == null) {
            location = locationRepository.save(Location.builder().lat(postEventDto.getLocation().getLat()).lon(postEventDto.getLocation().getLon()).build());
        }
        locationId = location.getId();
        return locationId;
    }

    void ValidateUpdateEventDto(UpdateEventRequestDto requestDto, Event event) throws EwmException {
        if (requestDto.getEventDate() != null && requestDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new EwmException(new EwmExceptionModel("New Event date isBefore now", "Data Integrity Violation.", "CONFLICT", // 409
                    HttpStatus.CONFLICT));
        }
        if (requestDto.getStateAction() != null && Objects.equals(requestDto.getStateAction(), "PUBLISH_EVENT") && Objects.equals(event.getState(), "PUBLISHED")) {
            throw new EwmException(new EwmExceptionModel("Event is already PUBLISHED", "Data Integrity Violation.", "CONFLICT", // 409
                    HttpStatus.CONFLICT));
        }
        if (requestDto.getStateAction() != null && Objects.equals(requestDto.getStateAction(), "PUBLISH_EVENT") && Objects.equals(event.getState(), "CANCELED")) {
            throw new EwmException(new EwmExceptionModel("Event is CANCELED", "Data Integrity Violation.", "CONFLICT", // 409
                    HttpStatus.CONFLICT));
        }
        if (requestDto.getStateAction() != null && Objects.equals(requestDto.getStateAction(), "REJECT_EVENT") && Objects.equals(event.getState(), "PUBLISHED")) {
            throw new EwmException(new EwmExceptionModel("Event has PUBLISH & can't be rejected", "Data Integrity Violation.", "CONFLICT", // 409
                    HttpStatus.CONFLICT));
        }
    }

    void ValidateAddEventDto(PostEventDto postEventDto) throws EwmException {
        if (postEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new EwmException(new EwmExceptionModel("New Event date isBefore now = 1 hr", "Data Integrity Violation.", "CONFLICT", // 409
                    HttpStatus.CONFLICT));
        }
    }

    void ValidateUpdateUseEventDto(UpdateEventRequestDto patchEventDto, Event event) throws EwmException {
        if (patchEventDto.getEventDate() != null && patchEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new EwmException(new EwmExceptionModel("New Event date isBefore now = 1 hr", "Data Integrity Violation.", "CONFLICT", // 409
                    HttpStatus.CONFLICT));
        }
        if (Objects.equals(event.getState(), "PUBLISHED")) {
            throw new EwmException(new EwmExceptionModel("Can't update PUBLISHED event", "Data Integrity Violation.", "CONFLICT", // 409
                    HttpStatus.CONFLICT));
        }
    }
}
