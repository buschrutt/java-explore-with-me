package ru.practicum.request.dto;

import ru.practicum.request.model.Request;

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

    public static RequestStatusDto toRequestStatesDto(List<RequestDto> confirmedRequestDtoList, List<RequestDto> rejectedRequestDtoList) {
        return RequestStatusDto.builder()
                .confirmedRequests(confirmedRequestDtoList)
                .rejectedRequests(rejectedRequestDtoList)
                .build();
    }

}
