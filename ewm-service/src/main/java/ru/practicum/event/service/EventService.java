package ru.practicum.event.service;

import ru.practicum.error.EwmException;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.UpdateEventRequestDto;
import ru.practicum.event.dto.PostEventDto;

import java.util.List;

public interface EventService {
    List<EventDto> findEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd,Boolean onlyAvailable, String sort, Integer from, Integer size, String ip);

    EventDto findEventById(Integer id, String ip);

    List<EventDto> findAllEvents(List<Integer> users, List<String> states, List<Integer> categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventDto updateEvent(Integer eventId, UpdateEventRequestDto updateEventRequestDto) throws EwmException;

    List<EventDto> findAllUserEvents(Integer userId, Integer from, Integer size);

    EventDto addEvent(Integer userId, PostEventDto postEventDto) throws EwmException;

    EventDto findUserEvent(Integer userId, Integer eventId) throws EwmException;

    EventDto updateUserEvent(Integer userId, Integer eventId, UpdateEventRequestDto patchEventDto) throws EwmException;
}
