package com.bookmytrain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages RAC (Reservation Against Cancellation) queue operations
 */
public class RACQueue {
    private DatabaseManager dbManager;
    
    public RACQueue() throws SQLException {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Add user to RAC
     */
    public int addToRAC(int userId, int trainId, int routeId) throws SQLException {
        // Get next position
        int position = getNextRACPosition(trainId, routeId);
        
        String query = "INSERT INTO rac (user_id, train_id, route_id, position) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, trainId);
            pstmt.setInt(3, routeId);
            pstmt.setInt(4, position);
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Get next RAC position
     */
    private int getNextRACPosition(int trainId, int routeId) throws SQLException {
        String query = "SELECT COALESCE(MAX(position), 0) + 1 FROM rac WHERE train_id = ? AND route_id = ? AND status = 'RAC'";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, routeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 1;
    }
    
    /**
     * Get RAC count for a train and route
     */
    public int getRACCount(int trainId, int routeId) throws SQLException {
        String query = "SELECT COUNT(*) FROM rac WHERE train_id = ? AND route_id = ? AND status = 'RAC'";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, routeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Promote first person from RAC
     */
    public boolean promoteFromRAC(int trainId, int routeId) throws SQLException {
        // Get first person in RAC
        String selectQuery = """
            SELECT rac_id, user_id FROM rac 
            WHERE train_id = ? AND route_id = ? AND status = 'RAC' 
            ORDER BY position LIMIT 1
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, routeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int racId = rs.getInt("rac_id");
                    int userId = rs.getInt("user_id");
                    
                    // Update RAC status
                    String updateQuery = "UPDATE rac SET status = 'Promoted' WHERE rac_id = ?";
                    try (PreparedStatement updateStmt = DatabaseManager.getConnection().prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, racId);
                        updateStmt.executeUpdate();
                    }
                    
                    // Update positions for remaining RAC
                    updateRACPositions(trainId, routeId);
                    
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Update RAC positions after promotion
     */
    private void updateRACPositions(int trainId, int routeId) throws SQLException {
        String query = "UPDATE rac SET position = position - 1 WHERE train_id = ? AND route_id = ? AND status = 'RAC' AND position > 1";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, routeId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Get RAC list for a train and route
     */
    public List<RACEntry> getRACList(int trainId, int routeId) throws SQLException {
        List<RACEntry> racList = new ArrayList<>();
        
        String query = """
            SELECT r.rac_id, r.user_id, r.position, r.request_time, r.status,
                   u.username, u.email
            FROM rac r
            JOIN users u ON r.user_id = u.user_id
            WHERE r.train_id = ? AND r.route_id = ?
            ORDER BY r.position
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, routeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RACEntry entry = new RACEntry();
                    entry.setRacId(rs.getInt("rac_id"));
                    entry.setUserId(rs.getInt("user_id"));
                    entry.setPosition(rs.getInt("position"));
                    entry.setRequestTime(rs.getTimestamp("request_time").toLocalDateTime());
                    entry.setStatus(rs.getString("status"));
                    entry.setUsername(rs.getString("username"));
                    entry.setEmail(rs.getString("email"));
                    
                    racList.add(entry);
                }
            }
        }
        
        return racList;
    }
    
    /**
     * Remove from RAC
     */
    public boolean removeFromRAC(int racId) throws SQLException {
        String query = "DELETE FROM rac WHERE rac_id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, racId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Inner class for RAC entries
     */
    public static class RACEntry {
        private int racId;
        private int userId;
        private int position;
        private java.time.LocalDateTime requestTime;
        private String status;
        private String username;
        private String email;
        
        // Getters and setters
        public int getRacId() { return racId; }
        public void setRacId(int racId) { this.racId = racId; }
        
        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }
        
        public int getPosition() { return position; }
        public void setPosition(int position) { this.position = position; }
        
        public java.time.LocalDateTime getRequestTime() { return requestTime; }
        public void setRequestTime(java.time.LocalDateTime requestTime) { this.requestTime = requestTime; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        @Override
        public String toString() {
            return "RAC Position " + position + ": " + username + " (" + status + ")";
        }
    }
}
