package kz.bitlab.G118security.controller;

import kz.bitlab.G118security.dto.UserCreate;
import kz.bitlab.G118security.dto.UserUpdate;
import kz.bitlab.G118security.dto.UserView;
import kz.bitlab.G118security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserView> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public UserView createUser(@RequestBody UserCreate userCreate) {
        return userService.createUser(userCreate);
    }

    @PutMapping
    public UserView updateUser(@RequestBody UserUpdate userUpdate) {
        return userService.changePassword(userUpdate);
    }
}