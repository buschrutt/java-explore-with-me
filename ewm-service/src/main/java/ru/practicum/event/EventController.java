package ru.practicum.event;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.error.EwmException;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.UpdateEventRequestDto;
import ru.practicum.event.dto.PostEventDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
public class EventController {
    EventService eventService;
    HttpServletRequest request;

    @GetMapping("/events")
    public List<EventDto> findEvents(@RequestParam(value = "text", required = false) String text,
                                             @RequestParam(value = "categories", required = false) List<Integer> categories,
                                             @RequestParam(value = "paid", required = false) Boolean paid,
                                             @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                             @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
                                             @RequestParam(value = "sort", defaultValue = "EVENT_DATE") String sort,
                                             @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return eventService.findEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr());
    }

    @GetMapping("/events/{id}")
    public EventDto findEventById(@PathVariable Integer id) {
        return eventService.findEventById(id, request.getRemoteAddr());
    }

    @GetMapping("/admin/events")
    public List<EventDto> findAllEvents(@RequestParam(value = "users", required = false) List<Integer> users,
                                        @RequestParam(value = "states", required = false) List<String> states,
                                        @RequestParam(value = "categories", required = false) List<Integer> categories,
                                        @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                        @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                        @Positive @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return eventService.findAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventDto updateEvent(@PathVariable Integer eventId,
                                @RequestBody UpdateEventRequestDto updateEventRequestDto) throws EwmException {
        return eventService.updateEvent(eventId, updateEventRequestDto);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventDto> findAllUserEvents(@PathVariable Integer userId,
                                            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                            @Positive @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return eventService.findAllUserEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/events")
    public EventDto addEvent(@PathVariable @NotNull Integer userId,
                             @RequestBody @Valid PostEventDto postEventDto) throws EwmException {
        return eventService.addEvent(userId, postEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto findUserEvent(@PathVariable Integer userId,
                                  @PathVariable Integer eventId) throws EwmException {
        return eventService.findUserEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto updateUserEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @RequestBody @Valid UpdateEventRequestDto updateEventRequestDto) throws EwmException {
        return eventService.updateUserEvent(userId, eventId, updateEventRequestDto);
    }
}
