package com.example.service;

import com.example.dao.UserDao;
import com.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        // Arrange
        User user = new User();
        user.setName("Ivan");
        user.setEmail("ivan@example.com");
        user.setAge(30);

        // Act
        User result = userService.createUser(user);

        // Assert
        assertNotNull(result);
        verify(userDao, times(1)).save(user);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setName("Ivan");
        when(userDao.findById(userId)).thenReturn(expectedUser);

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(
                createTestUser(1L, "Ivan", "ivan@example.com", 30),
                createTestUser(2L, "Gleb", "gleb@example.com", 25)
        );
        when(userDao.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userDao, times(1)).findAll();
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser() {
        // Arrange
        User user = createTestUser(1L, "Ivan", "ivan@example.com", 30);

        // Act
        User result = userService.updateUser(user);

        // Assert
        assertNotNull(result);
        verify(userDao, times(1)).update(user);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userDao, times(1)).delete(userId);
    }

    private User createTestUser(Long id, String name, String email, Integer age) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        return user;
    }
}
