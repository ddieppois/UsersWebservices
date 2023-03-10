package com.ddieppois.users.controllers;

import com.ddieppois.users.exceptions.UserNotFoundException;
import com.ddieppois.users.models.Users;
import com.ddieppois.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {

    @Autowired
    private UsersRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<Iterable<Users>> getAllUsers() {
        log.info("Returning all users");
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/getActive")
    public ResponseEntity<Iterable<Users>> getActiveUsers() {
        log.info("Returning all active users");
        return ResponseEntity.ok(userRepository.findAllByActive(true));
    }

    @GetMapping("/getInactive")
    public ResponseEntity<Iterable<Users>> getInactiveUsers() {
        log.info("Returning all inactive users");
        return ResponseEntity.ok(userRepository.findAllByActive(false));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Users> getUser(@PathVariable String id) throws UserNotFoundException {
        Optional<Users> user = userRepository.findById(Long.parseLong(id));
        if (user.isPresent()) {
            String result = String.format("User %s %s retrieved by id with email %s", user.get().getFirstName(), user.get().getLastName(), user.get().getEmail());
            log.info(result);
            return ResponseEntity.ok(user.get());
        }
        throw new UserNotFoundException(Long.parseLong(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String firstName,
                                             @RequestParam String lastName,
                                             @RequestParam String email) {
        userRepository.save(new Users(firstName, lastName, email));
        String result = String.format("User %s %s created with email %s", firstName, lastName, email);
        log.info(result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateUser(@PathVariable String id) throws UserNotFoundException {
        Optional<Users> user = userRepository.findById(Long.parseLong(id));
        if (user.isPresent()) {
            user.get().setActive(false);
            userRepository.save(user.get());
            String result = String.format("User %s %s deactivated with email %s", user.get().getFirstName(), user.get().getLastName(), user.get().getEmail());
            log.info(result);
            return ResponseEntity.ok(result);
        }
        throw new UserNotFoundException(Long.parseLong(id));
    }

    @GetMapping("/activate/{id}")
    public ResponseEntity<String> activateUser(@PathVariable String id) throws UserNotFoundException {
        Optional<Users> user = userRepository.findById(Long.parseLong(id));
        if (user.isPresent()) {
            user.get().setActive(true);
            userRepository.save(user.get());
            String result = String.format("User %s %s activated with email %s", user.get().getFirstName(), user.get().getLastName(), user.get().getEmail());
            log.info(result);
            return ResponseEntity.ok(result);
        }
        throw new UserNotFoundException(Long.parseLong(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id,
                                             @RequestParam String firstName,
                                             @RequestParam String lastName,
                                             @RequestParam String email) throws UserNotFoundException {
        Optional<Users> user = userRepository.findById(Long.parseLong(id));
        if (user.isPresent()) {
            user.get().setFirstName(firstName);
            user.get().setLastName(lastName);
            user.get().setEmail(email);
            userRepository.save(user.get());
            String result = String.format("User %s %s updated with email %s", firstName, lastName, email);
            log.info(result);
            return ResponseEntity.ok(result);
        }
        throw new UserNotFoundException(Long.parseLong(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id,
                                             @RequestParam String firstName,
                                             @RequestParam String lastName,
                                             @RequestParam String email) throws UserNotFoundException {
        Optional<Users> user = userRepository.findById(Long.parseLong(id));
        if (user.isPresent()) {
            userRepository.delete(user.get());
            String result = String.format("User %s %s deleted with email %s", firstName, lastName, email);
            log.info(result);
            return ResponseEntity.ok(result);
        }
        throw new UserNotFoundException(Long.parseLong(id));
    }
}
