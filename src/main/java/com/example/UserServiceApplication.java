package com.example;

import com.example.dao.UserDao;
import com.example.dao.UserDaoImpl;
import com.example.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class UserServiceApplication {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static UserDao userDao;

    public static void main(String[] args) {
        try {
            // Initialize Hibernate
            SessionFactory sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

            userDao = new UserDaoImpl(sessionFactory);

            boolean running = true;
            while (running) {
                displayMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        readUser();
                        break;
                    case 3:
                        updateUser();
                        break;
                    case 4:
                        deleteUser();
                        break;
                    case 5:
                        listAllUsers();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

            sessionFactory.close();
        } catch (Exception e) {
            logger.error("Application error: {}", e.getMessage());
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== User Management System ===");
        System.out.println("1. Create User");
        System.out.println("2. Read User");
        System.out.println("3. Update User");
        System.out.println("4. Delete User");
        System.out.println("5. List All Users");
        System.out.println("6. Exit");
    }

    private static void createUser() {
        System.out.println("\n=== Create User ===");
        User user = new User();

        System.out.print("Enter name: ");
        user.setName(scanner.nextLine());

        System.out.print("Enter email: ");
        user.setEmail(scanner.nextLine());

        user.setAge(getIntInput("Enter age: "));

        try {
            userDao.save(user);
            System.out.println("User created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private static void readUser() {
        System.out.println("\n=== Read User ===");
        Long id = (long) getIntInput("Enter user ID: ");

        try {
            User user = userDao.findById(id);
            if (user != null) {
                System.out.println(user);
            } else {
                System.out.println("User not found!");
            }
        } catch (Exception e) {
            System.out.println("Error reading user: " + e.getMessage());
        }
    }

    private static void updateUser() {
        System.out.println("\n=== Update User ===");
        Long id = (long) getIntInput("Enter user ID to update: ");

        try {
            User user = userDao.findById(id);
            if (user != null) {
                System.out.print("Enter new name (current: " + user.getName() + "): ");
                String name = scanner.nextLine();
                if (!name.isEmpty()) {
                    user.setName(name);
                }

                System.out.print("Enter new email (current: " + user.getEmail() + "): ");
                String email = scanner.nextLine();
                if (!email.isEmpty()) {
                    user.setEmail(email);
                }

                System.out.print("Enter new age (current: " + user.getAge() + "): ");
                String ageInput = scanner.nextLine();
                if (!ageInput.isEmpty()) {
                    user.setAge(Integer.parseInt(ageInput));
                }

                userDao.update(user);
                System.out.println("User updated successfully!");
            } else {
                System.out.println("User not found!");
            }
        } catch (Exception e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.println("\n=== Delete User ===");
        Long id = (long) getIntInput("Enter user ID to delete: ");

        try {
            userDao.delete(id);
            System.out.println("User deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void listAllUsers() {
        System.out.println("\n=== All Users ===");
        try {
            List<User> users = userDao.findAll();
            if (users.isEmpty()) {
                System.out.println("No users found!");
            } else {
                users.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error listing users: " + e.getMessage());
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}