package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

import java.util.List;

@Getter
@Setter
@Builder
@PackagePrivate
public class RequestUpdateStatusDto {
    List<Integer> requestIds;
    String status;
}
