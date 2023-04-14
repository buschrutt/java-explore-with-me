package ru.practicum.user.service;

import ru.practicum.error.NotFoundException;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findUsersByIds(List<Integer> userIds, Integer from, Integer size);

    UserDto addUser(UserDto userDto);

    void deleteUser(Integer userId) throws NotFoundException;
}
