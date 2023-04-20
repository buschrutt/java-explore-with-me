package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.client.StatisticsClient;
import ru.practicum.domain.HitDto;
import ru.practicum.error.ewmException;
import ru.practicum.error.model.ewmExceptionModel;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventDtoMapper;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.dto.LocationDtoMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.request.model.RequestState;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoMapper;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final StatisticsClient statsClient;

    @Override
    public List<EventDto> findEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd,
                                     Boolean onlyAvailable, String sort, Integer from, Integer size, String ip) {
        List<Event> eventList = eventRepository.findEventList(text, categories, paid, LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter));
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
    public List<EventDto> findAllEvents(List<Integer> users, List<State> states, List<Integer> categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> eventList = eventRepository.findEventAdminList(users, states, categories,
                LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter), pageable);
        List<EventDto> eventDtoList = new ArrayList<>();
        for (Event event : eventList) {
            eventDtoList.add(getEventDtoFunc(event));
        }
        return eventDtoList;
    }

    @Override
    public EventDto updateEvent(Integer eventId, EventDto eventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return convertEventToUpdatedDto(event, eventDto);
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
    public EventDto addEvent(Integer userId, EventDto eventDto) {
        Integer locationId = getLocationId(eventDto);
        Event event = EventDtoMapper.toEvent(eventDto, locationId);
        event.setInitiator(userId);
        return getEventDtoFunc(eventRepository.save(event));
    }

    @Override
    public EventDto findUserEvent(Integer userId, Integer eventId) throws ewmException {
        Event event = eventRepository.findEventByIdAndInitiator(eventId, userId);
        if (event == null) {
            throw new ewmException(new ewmExceptionModel("Event id:" + eventId + " was not found", "The required object was not found.", "NOT_FOUND",
                    HttpStatus.NOT_FOUND));
        }
        return getEventDtoFunc(eventRepository.save(event));
    }

    @Override
    public EventDto updateUserEvent(Integer userId, Integer eventId, EventDto eventDto) throws ewmException {
        Event event = eventRepository.findEventByIdAndInitiator(eventId, userId);
        if (event == null) {
            throw new ewmException(new ewmExceptionModel("Event id:" + eventId + " was not found", "The required object was not found.", "NOT_FOUND",
                    HttpStatus.NOT_FOUND));
        }
        return convertEventToUpdatedDto(event, eventDto);
    }

    // %%%%%%%%%% %%%%%%%%%% SUPPORTING
    EventDto  getEventDtoFunc(Event event) {
        UserDto initiatorDto = UserDtoMapper.toUserDto(userRepository.findById(event.getInitiator()).orElseThrow());
        LocationDto locationDto = LocationDtoMapper.toLocationDto(locationRepository.findById(event.getLocation()).orElseThrow());
        Integer confirmed = requestRepository.findRequestByStatus(RequestState.CONFIRMED.toString()).size();
        Integer views = statsClient.getStats(event.getCreatedOn().format(formatter), LocalDateTime.now().format(formatter), List.of("/events/" + event.getId()), true).size();
        return EventDtoMapper.toEventDto(event, confirmed, initiatorDto, locationDto, views);
    }

    EventDto convertEventToUpdatedDto(Event event, EventDto eventDto) {
        Event eventWUpdate = EventDtoMapper.toEvent(eventDto, getLocationId(eventDto));
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

    Integer getLocationId(EventDto eventDto) {
        Integer locationId;
        Location location = locationRepository.findLocationByLatAndLon(eventDto.getLocation().getLat(), eventDto.getLocation().getLon());
        if (location == null) {
            location = locationRepository.save(Location.builder().lat(eventDto.getLocation().getLat()).lon(eventDto.getLocation().getLon()).build());
        }
        locationId = location.getId();
        return locationId;
    }
}
