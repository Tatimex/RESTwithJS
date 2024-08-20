package ru.kata.spring.boot_security.demo.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDAOImpl implements RoleDAO{

    private static final Logger logger = (Logger) LogManager.getLogger(RoleDAOImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAllRoles() {
        logger.debug("Fetching all roles");
        List<Role> allRoles = entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
        logger.debug("Number of roles fetched: {}", allRoles.size());
        return allRoles;
    }

    @Override
    public void saveRole(Role role) {
        logger.debug("Saving or updating role: {}", role);
        try {
            Session session = entityManager.unwrap(Session.class);
            session.saveOrUpdate(role);
            logger.info("Role saved or updated successfully: {}", role);
        } catch (Exception e) {
            logger.error("Error occurred while saving or updating role: {}", role, e);
        }
    }

    @Override
    public void deleteRoleById(Long id) {
        logger.debug("Deleting role with id: {}", id);
        try {
            Role role = entityManager.find(Role.class, id);
            if (role != null) {
                entityManager.remove(role);
                logger.info("Role with id {} deleted successfully", id);
            } else {
                logger.warn("Role with id {} not found for deletion", id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting role with id: {}", id, e);
        }
    }

    @Override
    public Role getRoleById(Long id) {
        logger.debug("Fetching role with id: {}", id);
        Role role = entityManager.find(Role.class, id);
        if (role == null) {
            logger.warn("Role with id {} not found", id);
        } else {
            logger.debug("Fetched role: {}", role);
        }
        return role;
    }


    @Override
    public Optional<Role> getByRoleName(String roleName) {
        logger.debug("Fetching role with name: {}", roleName);
        try {
            Role role = entityManager.createQuery("SELECT r FROM Role r WHERE r.role = :roleName", Role.class)
                    .setParameter("roleName", roleName)
                    .getSingleResult();
            logger.debug("Fetched role: {}", role);
            return Optional.of(role);
        } catch (NoResultException e) {
            logger.warn("Role with name '{}' not found", roleName);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error occurred while fetching role with name: {}", roleName, e);
            return Optional.empty();
        }
    }

}
