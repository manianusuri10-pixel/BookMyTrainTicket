package BookMyTrainTicket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages waitlist operations
 */
public class WaitlistManager {
    private DatabaseManager dbManager;
    
    public WaitlistManager() throws SQLException {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Add user to waitlist
     */
    public int addToWaitlist(int userId, int trainId, int routeId) throws SQLException {
        // Get next position
        int position = getNextWaitlistPosition(trainId, routeId);
        
        String query = "INSERT INTO waitlist (user_id, train_id, route_id, position) VALUES (?, ?, ?, ?)";
        
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
     * Get next waitlist position
     */
    private int getNextWaitlistPosition(int trainId, int routeId) throws SQLException {
        String query = "SELECT COALESCE(MAX(position), 0) + 1 FROM waitlist WHERE train_id = ? AND route_id = ? AND status = 'Waiting'";
        
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
     * Get waitlist position for a waitlist ID
     */
    public int getWaitlistPosition(int waitlistId) throws SQLException {
        String query = "SELECT position FROM waitlist WHERE waitlist_id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, waitlistId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Promote first person from waitlist
     */
    public boolean promoteFromWaitlist(int trainId, int routeId) throws SQLException {
        // Get first person in waitlist
        String selectQuery = """
            SELECT waitlist_id, user_id FROM waitlist 
            WHERE train_id = ? AND route_id = ? AND status = 'Waiting' 
            ORDER BY position LIMIT 1
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, routeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int waitlistId = rs.getInt("waitlist_id");
                    int userId = rs.getInt("user_id");
                    
                    // Update waitlist status
                    String updateQuery = "UPDATE waitlist SET status = 'Promoted' WHERE waitlist_id = ?";
                    try (PreparedStatement updateStmt = DatabaseManager.getConnection().prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, waitlistId);
                        updateStmt.executeUpdate();
                    }
                    
                    // Update positions for remaining waitlist
                    updateWaitlistPositions(trainId, routeId);
                    
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Update waitlist positions after promotion
     */
    private void updateWaitlistPositions(int trainId, int routeId) throws SQLException {
        String query = "UPDATE waitlist SET position = position - 1 WHERE train_id = ? AND route_id = ? AND status = 'Waiting' AND position > 1";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, routeId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Get waitlist for a train and route
     */
    public List<WaitlistEntry> getWaitlist(int trainId, int routeId) throws SQLException {
        List<WaitlistEntry> waitlist = new ArrayList<>();
        
        String query = """
            SELECT w.waitlist_id, w.user_id, w.position, w.request_time, w.status,
                   u.username, u.email
            FROM waitlist w
            JOIN users u ON w.user_id = u.user_id
            WHERE w.train_id = ? AND w.route_id = ?
            ORDER BY w.position
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            pstmt.setInt(2, routeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    WaitlistEntry entry = new WaitlistEntry();
                    entry.setWaitlistId(rs.getInt("waitlist_id"));
                    entry.setUserId(rs.getInt("user_id"));
                    entry.setPosition(rs.getInt("position"));
                    entry.setRequestTime(rs.getTimestamp("request_time").toLocalDateTime());
                    entry.setStatus(rs.getString("status"));
                    entry.setUsername(rs.getString("username"));
                    entry.setEmail(rs.getString("email"));
                    
                    waitlist.add(entry);
                }
            }
        }
        
        return waitlist;
    }
    
    /**
     * Remove from waitlist
     */
    public boolean removeFromWaitlist(int waitlistId) throws SQLException {
        String query = "DELETE FROM waitlist WHERE waitlist_id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, waitlistId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Inner class for waitlist entries
     */
    public static class WaitlistEntry {
        private int waitlistId;
        private int userId;
        private int position;
        private java.time.LocalDateTime requestTime;
        private String status;
        private String username;
        private String email;
        
        // Getters and setters
        public int getWaitlistId() { return waitlistId; }
        public void setWaitlistId(int waitlistId) { this.waitlistId = waitlistId; }
        
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
            return "Position " + position + ": " + username + " (" + status + ")";
        }
    }
}
