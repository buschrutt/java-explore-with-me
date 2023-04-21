package ru.practicum.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.error.EwmException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
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
    public UserDto addUser(@Valid @RequestBody UserDto userDto) throws EwmException {
        return userService.addUser(userDto);
    }

    // %%%%%%%%%% %%%%%%%%%% admin processing func %%%%%%%%%% %%%%%%%%%%
    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }
}
