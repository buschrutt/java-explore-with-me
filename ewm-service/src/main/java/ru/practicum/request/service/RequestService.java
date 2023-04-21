package ru.practicum.request.service;

import ru.practicum.error.EwmException;
import ru.practicum.request.dto.RequestUpdateStatusDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestStatusDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> findUserRequests(Integer userId) throws EwmException;

    RequestDto addRequest(Integer userId, Integer eventId) throws EwmException;

    RequestDto cancelRequest(Integer userId, Integer requestId) throws EwmException;

    List<RequestDto> findEventRequests(Integer userId, Integer eventId) throws EwmException;

    RequestStatusDto updateRequestStatus(Integer userId, Integer eventId, RequestUpdateStatusDto changeStatesDto) throws EwmException;
}
