package ru.practicum.event.dto;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventDtoMapper {
    public static EventDto toEventDto(Event event, Integer confirmed, UserShortDto initiator, LocationDto location, Integer views, CategoryDto catDto) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(catDto)
                .confirmedRequests(confirmed)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
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

    public static Event toEvent(PostEventDto postDto, Integer locationId) {
        return Event.builder()
                .annotation(postDto.getAnnotation())
                .category(postDto.getCategory())
                .createdOn(LocalDateTime.now())
                .description(postDto.getDescription())
                .eventDate(postDto.getEventDate())
                .location(locationId)
                .paid(postDto.getPaid())
                .participantLimit(postDto.getParticipantLimit())
                .state("PENDING")
                .requestModeration(postDto.getRequestModeration())
                .title(postDto.getTitle())
                .build();
    }

    public static Event toEventFromPatch(UpdateEventRequestDto patchEventDto, Integer locationId) {
        String state = "PENDING";
        if (Objects.equals(patchEventDto.getStateAction(), "PUBLISH_EVENT")) {
            state = "PUBLISHED";
        } else if (Objects.equals(patchEventDto.getStateAction(), "REJECT_EVENT") || Objects.equals(patchEventDto.getStateAction(), "CANCEL_REVIEW")) {
            state = "CANCELED";
        }
        return Event.builder()
                .annotation(patchEventDto.getAnnotation())
                .category(patchEventDto.getCategory())
                .createdOn(LocalDateTime.now())
                .description(patchEventDto.getDescription())
                .eventDate(patchEventDto.getEventDate())
                .location(locationId)
                .paid(patchEventDto.getPaid())
                .participantLimit(patchEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(patchEventDto.getRequestModeration())
                .state(state)
                .title(patchEventDto.getTitle())
                .build();
    }

}
