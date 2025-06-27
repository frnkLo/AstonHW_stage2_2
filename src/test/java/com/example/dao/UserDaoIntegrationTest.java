package com.example.dao;

import com.example.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;
    private static UserDao userDao;

    @BeforeAll
    static void setUp() {
        // Configure Hibernate with test container settings
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgresContainer.getUsername());
        configuration.setProperty("hibernate.connection.password", postgresContainer.getPassword());
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
        userDao = new UserDaoImpl(sessionFactory);
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void save_ShouldPersistUser() {
        // Arrange
        User user = createTestUser("Ivan", "ivan@example.com", 30);

        // Act
        userDao.save(user);

        // Assert
        assertNotNull(user.getId());
        User foundUser = userDao.findById(user.getId());
        assertNotNull(foundUser);
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getAge(), foundUser.getAge());
    }

    @Test
    void findById_ShouldReturnUser() {
        // Arrange
        User user = createTestUser("Ivan", "ivan@example.com", 25);
        userDao.save(user);

        // Act
        User foundUser = userDao.findById(user.getId());

        // Assert
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        User user1 = createTestUser("Ivan", "ivan@example.com", 30);
        User user2 = createTestUser("Gleb", "gleb@example.com", 25);
        userDao.save(user1);
        userDao.save(user2);

        // Act
        List<User> users = userDao.findAll();

        // Assert
        assertNotNull(users);
        assertTrue(users.size() >= 2);
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("ivan@example.com")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("gleb@example.com")));
    }

    @Test
    void update_ShouldModifyUser() {
        // Arrange
        User user = createTestUser("Ivan", "ivan@example.com", 30);
        userDao.save(user);

        // Act
        user.setName("Ivan Updated");
        user.setAge(31);
        userDao.update(user);

        // Assert
        User updatedUser = userDao.findById(user.getId());
        assertNotNull(updatedUser);
        assertEquals("Ivan Updated", updatedUser.getName());
        assertEquals(31, updatedUser.getAge());
    }

    @Test
    void delete_ShouldRemoveUser() {
        // Arrange
        User user = createTestUser("Ivan Doe", "ivan@example.com", 30);
        userDao.save(user);

        // Act
        userDao.delete(user.getId());

        // Assert
        User deletedUser = userDao.findById(user.getId());
        assertNull(deletedUser);
    }

    private User createTestUser(String name, String email, Integer age) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        return user;
    }
}
