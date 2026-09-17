package BookMyTrainTicket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages seat availability and operations
 */
public class SeatAvailabilityManager {
    public SeatAvailabilityManager() throws SQLException {
        DatabaseManager.getInstance();
    }
    
    /**
     * Get all seats for a specific train
     */
    public List<SeatWithDetails> getSeatsForTrain(int trainId) throws SQLException {
        List<SeatWithDetails> seats = new ArrayList<>();
        
        String query = """
            SELECT s.seat_id, s.compartment_id, s.berth_type, s.seat_number, s.is_available,
                   c.compartment_name, cl.class_type
            FROM seats s
            JOIN compartments c ON s.compartment_id = c.compartment_id
            JOIN classes cl ON c.class_id = cl.class_id
            WHERE cl.train_id = ?
            ORDER BY cl.class_type, c.compartment_name, s.seat_number
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SeatWithDetails seat = new SeatWithDetails();
                    seat.setSeatId(rs.getInt("seat_id"));
                    seat.setCompartmentId(rs.getInt("compartment_id"));
                    seat.setBerthType(Seat.parseBerthType(rs.getString("berth_type")));
                    seat.setSeatNumber(rs.getString("seat_number"));
                    seat.setAvailable(rs.getBoolean("is_available"));
                    seat.setCompartmentName(rs.getString("compartment_name"));
                    seat.setClassType(rs.getString("class_type"));
                    
                    seats.add(seat);
                }
            }
        }
        
        return seats;
    }
    
    /**
     * Get available seats for a specific train and route
     */
    public List<SeatWithDetails> getAvailableSeats(int trainId, int routeId) throws SQLException {
        List<SeatWithDetails> availableSeats = new ArrayList<>();
        
        String query = """
            SELECT s.seat_id, s.compartment_id, s.berth_type, s.seat_number, s.is_available,
                   c.compartment_name, cl.class_type
            FROM seats s
            JOIN compartments c ON s.compartment_id = c.compartment_id
            JOIN classes cl ON c.class_id = cl.class_id
            WHERE cl.train_id = ? AND s.is_available = TRUE
            ORDER BY cl.class_type, c.compartment_name, s.seat_number
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SeatWithDetails seat = new SeatWithDetails();
                    seat.setSeatId(rs.getInt("seat_id"));
                    seat.setCompartmentId(rs.getInt("compartment_id"));
                    seat.setBerthType(Seat.parseBerthType(rs.getString("berth_type")));
                    seat.setSeatNumber(rs.getString("seat_number"));
                    seat.setAvailable(rs.getBoolean("is_available"));
                    seat.setCompartmentName(rs.getString("compartment_name"));
                    seat.setClassType(rs.getString("class_type"));
                    
                    availableSeats.add(seat);
                }
            }
        }
        
        return availableSeats;
    }
    
    /**
     * Update seat availability
     */
    public boolean updateSeatAvailability(int seatId, boolean isAvailable) throws SQLException {
        String query = "UPDATE seats SET is_available = ? WHERE seat_id = ?";
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setBoolean(1, isAvailable);
            pstmt.setInt(2, seatId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Get seat by ID
     */
    public SeatWithDetails getSeatById(int seatId) throws SQLException {
        String query = """
            SELECT s.seat_id, s.compartment_id, s.berth_type, s.seat_number, s.is_available,
                   c.compartment_name, cl.class_type
            FROM seats s
            JOIN compartments c ON s.compartment_id = c.compartment_id
            JOIN classes cl ON c.class_id = cl.class_id
            WHERE s.seat_id = ?
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, seatId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    SeatWithDetails seat = new SeatWithDetails();
                    seat.setSeatId(rs.getInt("seat_id"));
                    seat.setCompartmentId(rs.getInt("compartment_id"));
                    seat.setBerthType(Seat.parseBerthType(rs.getString("berth_type")));
                    seat.setSeatNumber(rs.getString("seat_number"));
                    seat.setAvailable(rs.getBoolean("is_available"));
                    seat.setCompartmentName(rs.getString("compartment_name"));
                    seat.setClassType(rs.getString("class_type"));
                    
                    return seat;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get recommended seats for user based on their role
     */
    public List<SeatWithDetails> getRecommendedSeats(int trainId, User.UserRole userRole) throws SQLException {
        List<SeatWithDetails> allAvailableSeats = getAvailableSeats(trainId, 0);
        List<SeatWithDetails> recommendedSeats = new ArrayList<>();
        
        for (SeatWithDetails seat : allAvailableSeats) {
            if (isSeatRecommendedForUser(seat, userRole)) {
                recommendedSeats.add(seat);
            }
        }
        
        // If no recommended seats, return all available seats
        return recommendedSeats.isEmpty() ? allAvailableSeats : recommendedSeats;
    }
    
    /**
     * Check if seat is recommended for a specific user type
     */
    private boolean isSeatRecommendedForUser(SeatWithDetails seat, User.UserRole userRole) {
        switch (userRole) {
            case Senior:
            case DifferentlyAbled:
                // Prefer lower berths for seniors and differently abled
                return seat.getBerthType() == Seat.BerthType.Lower || 
                       seat.getBerthType() == Seat.BerthType.Side_Lower;
            case Regular:
            case Admin:
            default:
                return true; // All seats are suitable
        }
    }
    
    /**
     * Get seats grouped by compartment for a train
     */
    public List<CompartmentSeats> getSeatsGroupedByCompartment(int trainId) throws SQLException {
        List<CompartmentSeats> compartmentSeatsList = new ArrayList<>();
        
        String compartmentQuery = """
            SELECT DISTINCT c.compartment_id, c.compartment_name, cl.class_type
            FROM compartments c
            JOIN classes cl ON c.class_id = cl.class_id
            WHERE cl.train_id = ?
            ORDER BY cl.class_type, c.compartment_name
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(compartmentQuery)) {
            pstmt.setInt(1, trainId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int compartmentId = rs.getInt("compartment_id");
                    String compartmentName = rs.getString("compartment_name");
                    String classType = rs.getString("class_type");
                    
                    List<SeatWithDetails> seats = getSeatsForCompartment(compartmentId);
                    
                    CompartmentSeats compartmentSeats = new CompartmentSeats(
                        compartmentId, compartmentName, classType, seats
                    );
                    compartmentSeatsList.add(compartmentSeats);
                }
            }
        }
        
        return compartmentSeatsList;
    }
    
    /**
     * Get seats for a specific compartment
     */
    private List<SeatWithDetails> getSeatsForCompartment(int compartmentId) throws SQLException {
        List<SeatWithDetails> seats = new ArrayList<>();
        
        String query = """
            SELECT s.seat_id, s.compartment_id, s.berth_type, s.seat_number, s.is_available,
                   c.compartment_name, cl.class_type
            FROM seats s
            JOIN compartments c ON s.compartment_id = c.compartment_id
            JOIN classes cl ON c.class_id = cl.class_id
            WHERE s.compartment_id = ?
            ORDER BY s.seat_number
            """;
        
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, compartmentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SeatWithDetails seat = new SeatWithDetails();
                    seat.setSeatId(rs.getInt("seat_id"));
                    seat.setCompartmentId(rs.getInt("compartment_id"));
                    seat.setBerthType(Seat.parseBerthType(rs.getString("berth_type")));
                    seat.setSeatNumber(rs.getString("seat_number"));
                    seat.setAvailable(rs.getBoolean("is_available"));
                    seat.setCompartmentName(rs.getString("compartment_name"));
                    seat.setClassType(rs.getString("class_type"));
                    
                    seats.add(seat);
                }
            }
        }
        
        return seats;
    }
    
    /**
     * Inner class to represent seat with additional details
     */
    public static class SeatWithDetails extends Seat {
        private String compartmentName;
        private String classType;
        
        public String getCompartmentName() { return compartmentName; }
        public void setCompartmentName(String compartmentName) { this.compartmentName = compartmentName; }
        
        public String getClassType() { return classType; }
        public void setClassType(String classType) { this.classType = classType; }
        
        @Override
        public String toString() {
            return getSeatNumber() + " (" + getBerthType() + ") - " + 
                   classType + " - " + compartmentName + 
                   (isAvailable() ? " [Available]" : " [Occupied]");
        }
    }
    
    /**
     * Inner class to represent compartment with its seats
     */
    public static class CompartmentSeats {
        private int compartmentId;
        private String compartmentName;
        private String classType;
        private List<SeatWithDetails> seats;
        
        public CompartmentSeats(int compartmentId, String compartmentName, String classType, List<SeatWithDetails> seats) {
            this.compartmentId = compartmentId;
            this.compartmentName = compartmentName;
            this.classType = classType;
            this.seats = seats;
        }
        
        public int getCompartmentId() { return compartmentId; }
        public String getCompartmentName() { return compartmentName; }
        public String getClassType() { return classType; }
        public List<SeatWithDetails> getSeats() { return seats; }
        
        public int getAvailableSeatsCount() {
            return (int) seats.stream().filter(SeatWithDetails::isAvailable).count();
        }
        
        public int getTotalSeatsCount() {
            return seats.size();
        }
        
        @Override
        public String toString() {
            return classType + " - " + compartmentName + 
                   " (" + getAvailableSeatsCount() + "/" + getTotalSeatsCount() + " available)";
        }
    }
}
