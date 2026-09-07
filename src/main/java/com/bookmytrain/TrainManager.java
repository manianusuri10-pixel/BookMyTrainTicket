package com.bookmytrain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages train-related operations
 */
public class TrainManager {
    private DatabaseManager dbManager;
    
    public TrainManager() throws SQLException {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Get all trains
     */
    public List<Train> getAllTrains() throws SQLException {
        List<Train> trains = new ArrayList<>();
        String query = "SELECT train_id, train_name, train_number FROM trains ORDER BY train_name";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Train train = new Train(
                    rs.getInt("train_id"),
                    rs.getString("train_name"),
                    rs.getString("train_number")
                );
                trains.add(train);
            }
        }
        
        return trains;
    }
    
    /**
     * Add a new train
     */
    public boolean addTrain(String trainName, String trainNumber) throws SQLException {
        String query = "INSERT INTO trains (train_name, train_number) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, trainName);
            pstmt.setString(2, trainNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Update train information
     */
    public boolean updateTrain(int trainId, String trainName, String trainNumber) throws SQLException {
        String query = "UPDATE trains SET train_name = ?, train_number = ? WHERE train_id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, trainName);
            pstmt.setString(2, trainNumber);
            pstmt.setInt(3, trainId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Delete a train
     */
    public boolean deleteTrain(int trainId) throws SQLException {
        String query = "DELETE FROM trains WHERE train_id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Search trains by source and destination
     */
    public List<TrainSearchResult> searchTrains(String source, String destination) throws SQLException {
        List<TrainSearchResult> results = new ArrayList<>();
        
        String query = """
            SELECT DISTINCT t.train_id, t.train_name, t.train_number,
                   r.route_id, r.source_station, r.destination_station,
                   r.departure_time, r.arrival_time, r.price
            FROM trains t
            JOIN routes r ON t.train_id = r.train_id
            WHERE LOWER(r.source_station) LIKE LOWER(?) 
            AND LOWER(r.destination_station) LIKE LOWER(?)
            ORDER BY t.train_name
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, "%" + source + "%");
            pstmt.setString(2, "%" + destination + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Train train = new Train(
                        rs.getInt("train_id"),
                        rs.getString("train_name"),
                        rs.getString("train_number")
                    );
                    
                    Route route = new Route(
                        rs.getInt("route_id"),
                        rs.getInt("train_id"),
                        rs.getString("source_station"),
                        rs.getString("destination_station"),
                        rs.getTime("departure_time").toLocalTime(),
                        rs.getTime("arrival_time").toLocalTime(),
                        rs.getBigDecimal("price")
                    );
                    
                    int availableSeats = getAvailableSeatsCount(train.getTrainId(), route.getRouteId());
                    
                    TrainSearchResult result = new TrainSearchResult(train, route, availableSeats);
                    results.add(result);
                }
            }
        }
        
        return results;
    }
    
    /**
     * Get train by ID
     */
    public Train getTrainById(int trainId) throws SQLException {
        String query = "SELECT train_id, train_name, train_number FROM trains WHERE train_id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Train(
                        rs.getInt("train_id"),
                        rs.getString("train_name"),
                        rs.getString("train_number")
                    );
                }
            }
        }
        
        return null;
    }
    
    /**
     * Check if train number already exists
     */
    public boolean trainNumberExists(String trainNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM trains WHERE train_number = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, trainNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get available seats count for a train and route
     */
    private int getAvailableSeatsCount(int trainId, int routeId) throws SQLException {
        String query = """
            SELECT COUNT(*) FROM seats s
            JOIN compartments c ON s.compartment_id = c.compartment_id
            JOIN classes cl ON c.class_id = cl.class_id
            WHERE cl.train_id = ? AND s.is_available = TRUE
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Inner class to represent search results
     */
    public static class TrainSearchResult {
        private Train train;
        private Route route;
        private int availableSeats;
        
        public TrainSearchResult(Train train, Route route, int availableSeats) {
            this.train = train;
            this.route = route;
            this.availableSeats = availableSeats;
        }
        
        public Train getTrain() { return train; }
        public Route getRoute() { return route; }
        public int getAvailableSeats() { return availableSeats; }
        
        @Override
        public String toString() {
            return train.getTrainName() + " - " + route.getSourceStation() + 
                   " to " + route.getDestinationStation() + " (" + availableSeats + " seats available)";
        }
    }
}
