package com.example.service;

import com.example.dao.UserDao;
import com.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(User user) {
        logger.info("Creating new user: {}", user);
        userDao.save(user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Getting user by id: {}", id);
        return userDao.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Getting all users");
        return userDao.findAll();
    }

    @Override
    public User updateUser(User user) {
        logger.info("Updating user: {}", user);
        userDao.update(user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        userDao.delete(id);
    }


}
