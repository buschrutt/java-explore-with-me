package ru.practicum.explore.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {

    @GetMapping("/stats")
    public void getFrameStats(@PathVariable Long userId) {

        //return userClient.findUserById(userId);
    }

    @PostMapping("/hit")
    public void postHitToStats() {

        //return userClient.addUser(userDto);
    }

}
