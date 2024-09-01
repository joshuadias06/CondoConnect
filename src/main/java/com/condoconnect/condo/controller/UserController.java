package com.condoconnect.condo.controller;

import com.condoconnect.condo.dto.UserDto;
import com.condoconnect.condo.service.Token;
import com.condoconnect.condo.service.UserService;
import com.condoconnect.condo.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }
    @PostMapping
    public ResponseEntity<User> createdUser(@Valid @RequestBody User user){
        return ResponseEntity.status(201).body(userService.createUser(user));
    }
    @PutMapping
    public ResponseEntity<User> updatedUser(@Valid @RequestBody User user){
        return ResponseEntity.status(200).body(userService.updatedUser(user));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@Valid @RequestBody UserDto user){
        Token token = userService.generatedToken(user);
        if (token != null){
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(403).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException exception){

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @Component
    public class DadosIniciais implements CommandLineRunner {

        private final UserService userService;

        @Autowired
        public DadosIniciais(UserService userService) {
            this.userService = userService;
        }

        @Override
        public void run(String... args) {
            if (userService.getAllUsers().isEmpty()) {
                User user = new User();
                user.setName("ADM-CondoConnect");
                user.setEmail("admin@condo.com");
                user.setPassword("condoconnect123");
                userService.createUser(user);
            }
        }
    }


}