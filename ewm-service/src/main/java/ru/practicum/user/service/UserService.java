package ru.practicum.user.service;

import ru.practicum.error.EwmException;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findUsersByIds(List<Integer> userIds, Integer from, Integer size);

    UserDto addUser(UserDto userDto) throws EwmException;

    void deleteUser(Integer userId);
}
