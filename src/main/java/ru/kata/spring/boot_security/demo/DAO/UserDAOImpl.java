package ru.kata.spring.boot_security.demo.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = (Logger) LogManager.getLogger(UserDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        logger.debug("Fetching all users");
        List<User> allUsers = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        logger.debug("Number of users fetched: {}", allUsers.size());
        return allUsers;
    }

    @Override
    public void saveUser(User user) {
        logger.debug("Saving user: {}", user);
        try {
            entityManager.persist(user);
            logger.info("User saved successfully: {}", user);
        } catch (Exception e) {
            logger.error("Error occurred while saving user: {}", user, e);
        }
    }

    @Override
    public User getUserById(Long id) {
        logger.debug("Fetching user with id: {}", id);
        User user = entityManager.find(User.class, id);
        if (user == null) {
            logger.warn("User with id {} not found", id);
        } else {
            logger.debug("Fetched user: {}", user);
        }
        return user;
    }

    @Override
    public void deleteUserById(Long id) {
        logger.debug("Deleting user with id: {}", id);
        try {
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
                logger.info("User with id {} deleted successfully", id);
            } else {
                logger.warn("User with id {} not found for deletion", id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting user with id: {}", id, e);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        logger.debug("Fetching user with email: {}", email);
        try {
            User user = (User) entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
            logger.debug("Fetched user: {}", user);
            return user;
        } catch (NoResultException e) {
            logger.warn("User with email {} not found", email);
            return null;
        } catch (Exception e) {
            logger.error("Error occurred while fetching user with email: {}", email, e);
            return null;
        }
    }
}