package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> findUsersByIds(List<Integer> userIds, Integer from, Integer size) {
        List<UserDto> userDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from, size);
        List<User> userList = userRepository.findUsersByIdInOrderByIdDesc(userIds, pageable);
        userList.forEach(user -> userDtoList.add(UserDtoMapper.toUserDto(user)));
        return userDtoList;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserDtoMapper.toUser(userDto);
        return UserDtoMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.delete(userRepository.findById(userId).orElseThrow());
    }
}
