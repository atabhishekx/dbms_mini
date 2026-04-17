package com.deliverysystem.service;

import com.deliverysystem.dao.UserDAO;
import com.deliverysystem.model.User;
import com.deliverysystem.model.User.UserRole;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for User operations.
 * Handles business logic for user management.
 */
public class UserService {
    private final UserDAO userDAO;

    /**
     * Constructor
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register a new user
     */
    public User register(String username, String password, String fullName, 
                      String email, String phone, String address, UserRole role) throws SQLException {
        
        // Validate input
        validateUserInput(username, password, fullName, email, phone);
        
        // Check if username already exists
        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email already exists
        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create new user
        User user = new User(0, username, password, fullName, email, phone, address, role);
        int userId = userDAO.insert(user);
        
        if (userId > 0) {
            user.setUserId(userId);
            return user;
        }
        
        throw new SQLException("Failed to register user");
    }

    /**
     * Authenticate user
     */
    public User authenticate(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        return userDAO.authenticate(username, password);
    }

    /**
     * Update user profile
     */
    public boolean updateProfile(User user) throws SQLException {
        validateUserInput(user.getUsername(), user.getPassword(), user.getFullName(), 
                         user.getEmail(), user.getPhone());
        
        return userDAO.update(user);
    }

    /**
     * Get user by ID
     */
    public User getUserById(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return userDAO.findById(userId);
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    /**
     * Get users by role
     */
    public List<User> getUsersByRole(UserRole role) throws SQLException {
        return userDAO.findByRole(role);
    }

    /**
     * Deactivate user account
     */
    public boolean deactivateUser(int userId) throws SQLException {
        return userDAO.updateStatus(userId, false);
    }

    /**
     * Activate user account
     */
    public boolean activateUser(int userId) throws SQLException {
        return userDAO.updateStatus(userId, true);
    }

    /**
     * Validate user input
     */
    private void validateUserInput(String username, String password, String fullName, 
                                   String email, String phone) throws IllegalArgumentException {
        
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        
        if (phone == null || phone.length() < 10) {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }
}