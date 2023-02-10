package com.sample.freeboard.domain.user.controller;

import com.sample.freeboard.domain.user.dto.UserDTO;
import com.sample.freeboard.domain.user.dto.UserRequest;
import com.sample.freeboard.domain.user.service.UserService;
import com.sample.freeboard.global.annotation.AnyoneCallable;
import com.sample.freeboard.global.dto.response.ListResponse;
import com.sample.freeboard.domain.recommend.dto.Recommends;
import com.sample.freeboard.domain.recommend.service.RecommendService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("sample/user")
public class UserController {

    private final UserService userService;
    private final RecommendService recommendService;

    @GetMapping("{userId}/recommend")
    @AnyoneCallable
    public ListResponse<Recommends> getRecommendHistory(@PathVariable("userId") long userId) {
        return recommendService.getRecommendHistory(userId);
    }

    @PostMapping("")
    @AnyoneCallable
    public UserDTO joinUser(@RequestBody UserRequest userRequest) {
        return userService.joinUser(userRequest);
    }
}
