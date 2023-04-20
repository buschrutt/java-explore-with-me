package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;
import ru.practicum.request.model.Request;

import java.util.List;

@Getter
@Setter
@Builder
@PackagePrivate
public class RequestStatesDto {
    List<RequestDto> confirmedRequestsDto;
    List<RequestDto> rejectedRequestsDto;
}
