package ru.practicum.request;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.error.ewmException;
import ru.practicum.request.dto.RequestChangeStatesDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestStatesDto;
import ru.practicum.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
public class RequestController {
    RequestService requestService;

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> findUserRequests(@PathVariable Integer userId) throws ewmException {
        return requestService.findUserRequests(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable @Positive Integer userId,
                                 @RequestParam(value = "eventId") @Positive Integer eventId) throws ewmException {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Integer userId,
                                    @PathVariable Integer requestId) throws ewmException {
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> findEventRequests(@PathVariable Integer userId,
                                              @PathVariable Integer eventId) throws ewmException {
        return requestService.findEventRequests(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public RequestStatesDto changeRequestStates(@PathVariable Integer userId,
                                                @PathVariable Integer eventId,
                                                @RequestBody RequestChangeStatesDto changeStatesDto) throws ewmException {
        return requestService.changeRequestStates(userId, eventId, changeStatesDto);
    }
}
