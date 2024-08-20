package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class DatabaseCleanupService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void truncateTables() {
        entityManager.createNativeQuery("TRUNCATE TABLE users CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE roles CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE user_roles CASCADE").executeUpdate();
    }
}
