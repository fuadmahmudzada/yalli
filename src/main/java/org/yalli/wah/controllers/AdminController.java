package org.yalli.wah.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yalli.wah.dtos.RegisterDto;
import org.yalli.wah.dtos.UserDto;
import org.yalli.wah.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody RegisterDto registerDto) {
        UserDto createdUser = userService.createUser(registerDto);
        return ResponseEntity.ok(createdUser);
    }
}