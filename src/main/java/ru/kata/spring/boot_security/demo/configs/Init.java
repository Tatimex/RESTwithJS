package ru.kata.spring.boot_security.demo.configs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.DatabaseCleanupService;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;


@Component
public class Init {

    private final UserService userService;
    private final RoleService roleService;

    public Init(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void postConstruct() {
        User admin = new User();
        admin.setEmail("admin@admin.com");
        admin.setName("Pupa");
        admin.setSurname("Pupuna");
        admin.setAge("34");
        admin.setPassword("user");


        User user = new User();
        user.setEmail("user@user.com");
        user.setName("Fufu");
        user.setSurname("Fufin");
        user.setAge("18");
        user.setPassword("user");


        Role role = new Role(1L, "ROLE_ADMIN");
        Role role2 = new Role(2L, "ROLE_USER");

        roleService.saveRole(role);
        roleService.saveRole(role2);

        admin.setRoles(Collections.singleton(role));
        user.setRoles(Collections.singleton(role2));

        userService.saveUser(admin);
        userService.saveUser(user);
    }

}
