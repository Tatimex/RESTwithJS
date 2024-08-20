package ru.kata.spring.boot_security.demo.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DAO.RoleDAO;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LogManager.getLogger(RoleServiceImpl.class);

    private final RoleDAO roleDAO;

    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        logger.debug("Request to get all roles");
        List<Role> roles = roleDAO.getAllRoles();
        logger.debug("Number of roles retrieved: {}", roles.size());
        return roles;
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        logger.debug("Request to save role: {}", role);
        try {
            roleDAO.saveRole(role);
            logger.info("Role saved successfully: {}", role);
        } catch (Exception e) {
            logger.error("Error occurred while saving role: {}", role, e);
        }
    }

    @Override
    @Transactional
    public void deleteRoleById(Long id) {
        logger.debug("Request to delete role with id: {}", id);
        try {
            roleDAO.deleteRoleById(id);
            logger.info("Role with id {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting role with id: {}", id, e);
        }
    }

    @Override
    @Transactional
    public Role getRoleById(Long id) {
        logger.debug("Request to get role with id: {}", id);
        Role role = roleDAO.getRoleById(id);
        if (role == null) {
            logger.warn("Role with id {} not found", id);
        } else {
            logger.debug("Retrieved role: {}", role);
        }
        return role;
    }

    @Override
    @Transactional
    public Role getByRoleName(String roleName) {
        logger.debug("Request to get role with name: {}", roleName);
        return roleDAO.getByRoleName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
    }
}