package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DAO.UserDAO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


private final UserDAO userDAO;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO userDAO, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    @Transactional
    public List<User> getAllUsers() {
        logger.debug("Request to get all users");
        List<User> users = userDAO.getAllUsers();
        logger.debug("Number of users retrieved: {}", users.size());
        return users;
    }

@Override
@Transactional
public User saveUser(User user, String[] roles) {
    logger.debug("Request to save user: {}", user);

    try {
        if (user.getId() != null) {
            User oldUser = getUserById(user.getId());

            if (oldUser != null && (user.getPassword() == null || user.getPassword().isEmpty())) {
                user.setPassword(oldUser.getPassword());
                logger.info("Password unchanged for user: {}", user.getId());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                logger.info("Password changed and encoded for user: {}", user.getId());
            }
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.info("New user created, password encoded");
        }

        Set<Role> roleSet = new HashSet<>();
        for (String roleName : roles) {
            Role role = roleService.getByRoleName(roleName);
            roleSet.add(role);
            logger.debug("Role added: {}", roleName);
        }
        user.setRoles(roleSet);
        userDAO.saveUser(user);
        logger.info("User saved successfully: {}", user);
        return user;
    } catch (Exception e) {
        logger.error("Error occurred while saving user: {}", user, e);
        throw e; // Rethrow the exception after logging it
    }
}

    @Override
    @Transactional
    public User getUserById(Long id) {
        logger.debug("Request to get user by id: {}", id);
        User user = userDAO.getUserById(id);
        if (user == null) {
            logger.warn("User with id {} not found", id);
        } else {
            logger.debug("Retrieved user: {}", user);
        }
        return user;
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        logger.debug("Request to delete user by id: {}", id);
        try {
            userDAO.deleteUserById(id);
            logger.info("User with id {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting user with id: {}", id, e);
            throw e; // Rethrow the exception after logging it
        }
    }

    @Override
    public User getUserByEmail(String email) {
        logger.debug("Request to get user by email: {}", email);
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            logger.warn("User with email {} not found", email);
        } else {
            logger.debug("Retrieved user: {}", user);
        }
        return user;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Request to load user by username (email): {}", email);
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            logger.warn("User with email {} not found", email);
            throw new UsernameNotFoundException("User is unknown");
        }
        logger.info("User found: {}", user);
        return user;
    }
}