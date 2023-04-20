package ru.practicum.event.dto;

import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

public class EventDtoMapper {
    public static EventDto toEventDto(Event event, Integer confirmed, UserDto initiator, LocationDto location, Integer views) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(confirmed)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate()) //event.getEventDate()
                .initiator(initiator)
                .location(location)
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static Event toEvent(EventDto eventDto, Integer locationId) {
        return Event.builder()
                .annotation(eventDto.getAnnotation())
                .category(eventDto.getCategory())
                .createdOn(LocalDateTime.now())
                .description(eventDto.getDescription())
                .eventDate(eventDto.getEventDate()) //eventDto.getEventDate()
                .location(locationId)
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .publishedOn(eventDto.getPublishedOn())
                .requestModeration(eventDto.getRequestModeration())
                .state(eventDto.getState())
                .title(eventDto.getTitle())
                .build();
    }

}
