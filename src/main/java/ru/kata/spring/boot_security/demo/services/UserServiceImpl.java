package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DAO.UserDao;
import ru.kata.spring.boot_security.demo.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userDao.findAll();
        if (users != null && !users.isEmpty()) {
            logger.info("Found {} users", users.size());
        } else {
            logger.warn("No users found");
        }
        return users;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        logger.info("Attempting to save user: {}", user.getEmail());
        if (userDao.findByEmail(user.getEmail()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            try {
                userDao.save(user);
                logger.info("User {} saved successfully", user.getEmail());
            } catch (Exception e) {
                logger.error("Error saving user: {}", user.getEmail(), e);
                throw e;
            }
        } else {
            logger.warn("Duplicate email detected for user: {}", user.getEmail());
            try {
                throw new Exception("Duplicate email!");
            } catch (Exception e) {
                logger.error("Exception thrown for duplicate email: {}", user.getEmail(), e);
            }
        }
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        logger.info("Attempting to update user: {}", user.getEmail());
        if (user.getId() == null) {
            logger.error("User ID is null for user: {}", user.getEmail());
            try {
                throw new Exception("User not have ID!");
            } catch (Exception e) {
                logger.error("Exception thrown: User not have ID for user: {}", user.getEmail(), e);
            }
        } else {
            User oldUser = getUserById(user.getId());
            if (user.getPassword().equals("") || user.getPassword() == null) {
                user.setPassword(oldUser.getPassword());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            try {
                userDao.save(user);
                logger.info("User {} updated successfully", user.getEmail());
            } catch (Exception e) {
                logger.error("Error updating user: {}", user.getEmail(), e);
                throw e;
            }
        }
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        Optional<User> optional = userDao.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            logger.info("User found: {}", user.getEmail());
            return user;
        } else {
            logger.warn("User with ID {} not found", id);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        logger.info("Attempting to delete user by ID: {}", id);
        User user = getUserById(id);
        if (user == null) {
            logger.error("User with ID {} not found", id);
            try {
                throw new Exception("There is no user with ID = " + id + " in Database");
            } catch (Exception e) {
                logger.error("Exception thrown: User not found for ID {}", id, e);
            }
        } else {
            try {
                userDao.deleteById(id);
                logger.info("User with ID {} deleted successfully", id);
            } catch (Exception e) {
                logger.error("Error deleting user with ID {}", id, e);
                throw e;
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        User user = userDao.findByEmail(email);
        if (user != null) {
            logger.info("User found by email: {}", email);
        } else {
            logger.warn("User with email {} not found", email);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", email);
        User user = userDao.findByEmail(email);
        if (user != null) {
            logger.info("User loaded by username: {}", email);
            return user;
        } else {
            logger.error("User with username {} not found", email);
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
    }
}
