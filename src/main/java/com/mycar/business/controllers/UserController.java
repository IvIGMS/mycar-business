package com.mycar.business.controllers;

import com.mycar.business.controllers.dto.user.UserDTO;
import com.mycar.business.controllers.dto.user.UserRegisterDTO;
import com.mycar.business.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRegisterDTO userRegisterDTO){
        UserDTO userResult = userService.createUser(userRegisterDTO);
        return ResponseEntity.ok().body(userResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        UserDTO userResult = userService.getUserById(id);
        return ResponseEntity.ok().body(userResult);
    }

    @PatchMapping("/activate/{id}")
    // fixme: useless for now, it need a refactor and wrong Response, It need a badRequest to.
    public ResponseEntity<String> activateUser(@PathVariable Long id){
        String result = userService.activateUser(id);
        return ResponseEntity.ok().body(result);
    }
}
