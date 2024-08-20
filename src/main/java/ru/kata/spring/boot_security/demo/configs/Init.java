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
import java.util.HashSet;


@Component
public class Init {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final DatabaseCleanupService databaseCleanupService;

    public Init(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, DatabaseCleanupService databaseCleanupService) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.databaseCleanupService = databaseCleanupService;
    }

    private static final Logger logger = LogManager.getLogger(RoleServiceImpl.class);

    @PostConstruct
    public void init() {

        databaseCleanupService.truncateTables();


        Role adminRole = new Role(null, "ROLE_ADMIN");
        Role userRole = new Role(null, "ROLE_USER");
        roleService.saveRole(adminRole);
        roleService.saveRole(userRole);


        User admin = new User("Admin", 30, "admin@example.com","adminpass", new HashSet<>(Arrays.asList(adminRole)));
        User user = new User("User", 25, "user@example.com", "userpass", new HashSet<>(Arrays.asList(userRole)));

        userService.saveUser(admin, new String[]{"ROLE_ADMIN"});
        userService.saveUser(user, new String[]{"ROLE_USER"});

        logger.info("Initial roles and users have been created");
    }
}
