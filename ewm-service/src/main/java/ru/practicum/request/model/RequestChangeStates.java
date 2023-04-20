package ru.practicum.request.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

import java.util.List;

@Getter
@Setter
@Builder
@PackagePrivate
public class RequestChangeStates {
    List<Integer> requestIds;
    String status;
}
