package ru.practicum.event.dto;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

public class EventDtoMapper {
    public static EventDto toEventDto(Event event, Integer confirmed, UserDto initiator, LocationDto location, Integer views, CategoryDto catDto) {
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
                .publishedOn(LocalDateTime.now())
                .requestModeration(postDto.getRequestModeration())
                .state("PENDING")
                .title(postDto.getTitle())
                .build();
    }

    public static Event toEventFromPatch(PatchEventDto patchEventDto, Integer locationId) {
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
                .state("PENDING")
                .title(patchEventDto.getTitle())
                .build();
    }

}
