package ru.practicum.request.service;

import ru.practicum.error.ewmException;
import ru.practicum.request.dto.RequestChangeStatesDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestStatesDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> findUserRequests(Integer userId) throws ewmException;

    RequestDto addRequest(Integer userId, Integer eventId) throws ewmException;

    RequestDto cancelRequest(Integer userId, Integer requestId) throws ewmException;

    List<RequestDto> findEventRequests(Integer userId, Integer eventId) throws ewmException;

    RequestStatesDto changeRequestStates(Integer userId, Integer eventId, RequestChangeStatesDto changeStatesDto) throws ewmException;
}
