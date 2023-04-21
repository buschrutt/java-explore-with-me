package ru.practicum.request.dto;

import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestState;
import ru.practicum.request.model.RequestChangeStates;

import java.time.LocalDateTime;
import java.util.List;

public class RequestDtoMapper {

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester())
                .event(request.getEvent())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }

    public static RequestChangeStates toRequestChangeStates(RequestChangeStatesDto requestChangeStatesDto) {
        return RequestChangeStates.builder()
                .requestIds(requestChangeStatesDto.getRequestIds())
                .status(requestChangeStatesDto.getStatus())
                .build();
    }

    public static RequestStatesDto toRequestStatesDto(List<RequestDto> confirmedRequestDtoList, List<RequestDto> rejectedRequestDtoList) {
        return RequestStatesDto.builder()
                .confirmedRequestsDto(confirmedRequestDtoList)
                .rejectedRequestsDto(rejectedRequestDtoList)
                .build();
    }

}
