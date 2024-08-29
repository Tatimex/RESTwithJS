package ru.kata.spring.boot_security.demo.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DAO.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LogManager.getLogger(RoleServiceImpl.class);

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> getAllRoles() {
        logger.info("Fetching all roles");
        List<Role> roles = roleDao.findAll();
        if (roles != null && !roles.isEmpty()) {
            logger.info("Found {} roles", roles.size());
        } else {
            logger.warn("No roles found");
        }
        return roles;
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        logger.info("Saving role: {}", role);
        try {
            roleDao.save(role);
            logger.info("Role saved successfully");
        } catch (Exception e) {
            logger.error("Error saving role: {}", role, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteRoleById(Long id) {
        logger.info("Deleting role with id: {}", id);
        try {
            roleDao.deleteById(id);
            logger.info("Role with id {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting role with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Role getRoleById(Long id) {
        logger.info("Fetching role with id: {}", id);
        Optional<Role> optional = roleDao.findById(id);
        if (optional.isPresent()) {
            Role role = optional.get();
            logger.info("Role found: {}", role);
            return role;
        } else {
            logger.warn("Role with id {} not found", id);
            return null;
        }
    }

    @Override
    public Role getByRoleName(String roleName) {
        logger.info("Fetching role with name: {}", roleName);
        Role role = roleDao.findByRole(roleName);
        if (role != null) {
            logger.info("Role found: {}", role);
        } else {
            logger.warn("Role with name {} not found", roleName);
        }
        return role;
    }
}

