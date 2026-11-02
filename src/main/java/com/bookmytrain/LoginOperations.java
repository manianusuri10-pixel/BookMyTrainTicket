package com.bookmytrain;

import java.sql.*;

/**
 * Handles user authentication and login operations
 */
public class LoginOperations {
    private DatabaseManager dbManager;
    
    public LoginOperations() throws SQLException {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Authenticate user with username and password
     */
    public User authenticateUser(String username, String password) throws SQLException {
        String query = "SELECT user_id, username, password, email, role FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    
                    // In a real application, you would hash passwords
                    if (password.equals(storedPassword)) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setEmail(rs.getString("email"));
                        user.setRole(User.UserRole.valueOf(rs.getString("role")));
                        
                        return user;
                    }
                }
            }
        }
        
        return null; // Authentication failed
    }
    
    /**
     * Register a new user
     */
    public boolean registerUser(String username, String password, String email, User.UserRole role) throws SQLException {
        // Check if username already exists
        if (userExists(username)) {
            return false;
        }
        
        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In real app, hash the password
            pstmt.setString(3, email);
            pstmt.setString(4, role.name());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Check if username already exists
     */
    private boolean userExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Update user password
     */
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        String query = "UPDATE users SET password = ? WHERE user_id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, newPassword); // In real app, hash the password
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT user_id, username, password, email, role FROM users WHERE user_id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(User.UserRole.valueOf(rs.getString("role")));
                    
                    return user;
                }
            }
        }
        
        return null;
    }
}
