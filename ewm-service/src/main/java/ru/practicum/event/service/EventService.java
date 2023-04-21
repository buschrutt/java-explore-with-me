package ru.practicum.event.service;

import ru.practicum.error.ewmException;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.PatchEventDto;
import ru.practicum.event.dto.PostEventDto;
import ru.practicum.event.model.State;

import java.util.List;

public interface EventService {
    List<EventDto> findEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd,Boolean onlyAvailable, String sort, Integer from, Integer size, String ip);

    EventDto getEventById(Integer id, String ip);

    List<EventDto> findAllEvents(List<Integer> users, List<State> states, List<Integer> categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventDto updateEvent(Integer eventId, PostEventDto postEventDto);

    List<EventDto> findAllUserEvents(Integer userId, Integer from, Integer size);

    EventDto addEvent(Integer userId, PostEventDto postEventDto);

    EventDto findUserEvent(Integer userId, Integer eventId) throws ewmException;

    EventDto updateUserEvent(Integer userId, Integer eventId, PatchEventDto patchEventDto) throws ewmException;
}
