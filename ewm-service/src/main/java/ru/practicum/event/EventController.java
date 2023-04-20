package ru.practicum.event;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.error.ewmException;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.model.State;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public EventDto getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id, request.getRemoteAddr());
    }

    @GetMapping("/admin/events")
    public List<EventDto> findAllEvents(@RequestParam(value = "users", required = false) List<Integer> users,
                                        @RequestParam(value = "states", required = false) List<State> states,
                                        @RequestParam(value = "categories", required = false) List<Integer> categories,
                                        @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                        @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                        @Positive @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return eventService.findAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventDto updateEvent(@PathVariable Integer eventId,
                                @RequestBody EventDto eventDto) {
        return eventService.updateEvent(eventId, eventDto);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventDto> findAllUserEvents(@PathVariable Integer userId,
                                            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                            @Positive @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return eventService.findAllUserEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/events")
    public EventDto addEvent(@PathVariable Integer userId,
                             @RequestBody @Valid EventDto eventDto) {
        return eventService.addEvent(userId, eventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto findUserEvent(@PathVariable Integer userId,
                                  @PathVariable Integer eventId) throws ewmException {
        return eventService.findUserEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto updateUserEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @RequestBody @Valid EventDto eventDto) throws ewmException {
        return eventService.updateUserEvent(userId, eventId, eventDto);
    }
}
