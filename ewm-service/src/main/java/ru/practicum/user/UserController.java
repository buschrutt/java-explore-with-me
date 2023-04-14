package ru.practicum.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.error.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // %%%%%%%%%% %%%%%%%%%% admin processing func %%%%%%%%%% %%%%%%%%%%
    @GetMapping("/admin/users")
    public List<UserDto> findFilteredUsers(@RequestParam(value = "ids") List<Integer> userIds,
                                           @PositiveOrZero
                                           @RequestParam(value = "from", required = false, defaultValue = "0")
                                           Integer from,
                                           @Positive
                                           @RequestParam(value = "size", required = false, defaultValue = "10")
                                           Integer size) {
        return userService.findUsersByIds(userIds, from, size);
    }

    // %%%%%%%%%% %%%%%%%%%% admin processing func %%%%%%%%%% %%%%%%%%%%
    @PostMapping("/admin/users")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        return userService.addUser(userDto);
    }

    // %%%%%%%%%% %%%%%%%%%% admin processing func %%%%%%%%%% %%%%%%%%%%
    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) throws NotFoundException {
        userService.deleteUser(userId);
    }
}