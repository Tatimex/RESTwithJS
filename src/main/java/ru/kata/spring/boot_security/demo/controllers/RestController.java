package ru.kata.spring.boot_security.demo.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserDto;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
        private static final Logger logger = LogManager.getLogger(RestController.class);

        private final UserService userService;
        private final RoleService roleService;

        public RestController(UserService userService, RoleService roleService) {
            this.userService = userService;
            this.roleService = roleService;
        }

        @GetMapping("/admin/users")
        public ResponseEntity<List<User>> getAllUsers() {
            logger.info("Fetching all users");
            List<User> users = userService.getAllUsers();
            if (users != null && !users.isEmpty()) {
                logger.info("Found {} users", users.size());
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                logger.warn("No users found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        @GetMapping("/admin/users/{id}")
        public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
            logger.info("Fetching user with id: {}", id);
            User user = userService.getUserById(id);
            if (user != null) {
                logger.info("User found: {}", user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                logger.warn("User with id: {} not found", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        @GetMapping("/user")
        public User getUser(@AuthenticationPrincipal User user) {
            logger.info("Fetching authenticated user: {}", user);
            return user;
        }

        @GetMapping("/users/new")
        public User newUser() {
            logger.info("Creating a new blank user with roles");
            User blankUser = new User();
            blankUser.setRoles(new HashSet<>(roleService.getAllRoles()));
            logger.debug("New blank user created with roles: {}", blankUser.getRoles());
            return blankUser;
        }

        @PostMapping("/admin/users")
        public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
            logger.info("Creating a new user with email: {}", userDto.getEmail());
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : userDto.getRolesNames()) {
                Role role = roleService.getByRoleName(roleName);
                logger.debug("Adding role: {} to the new user", role);
                roleSet.add(role);
            }
            User user = new User(userDto);
            user.setRoles(roleSet);
            userService.saveUser(user);
            User createdUser = userService.findByEmail(user.getEmail());
            logger.info("User created with id: {}", createdUser.getId());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }

        @PutMapping("/admin/users/{userId}")
        public ResponseEntity<User> updateUser(@PathVariable("userId") Long id,
                                               @RequestBody UserDto userDto) {
            logger.info("Updating user with id: {}", id);
            Set<Role> rolesSet = new HashSet<>();
            for (String roleName : userDto.getRolesNames()) {
                Role role = roleService.getByRoleName(roleName);
                logger.debug("Adding role: {} to the user", role);
                rolesSet.add(role);
            }
            User user = new User(userDto);
            user.setRoles(rolesSet);
            user.setId(id);
            userService.updateUser(user);
            User updatedUser = userService.getUserById(id);
            logger.info("User updated: {}", updatedUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

        @DeleteMapping("/admin/users/{userId}")
        public ResponseEntity<List<User>> delete(@PathVariable Long userId) {
            logger.info("Deleting user with id: {}", userId);
            userService.deleteUserById(userId);
            logger.info("User deleted. Fetching remaining users.");
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

