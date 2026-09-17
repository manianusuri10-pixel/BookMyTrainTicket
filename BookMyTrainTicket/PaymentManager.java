package BookMyTrainTicket;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PaymentManager handles payment processing, confirmation, and receipt generation
 * Includes dummy payment gateway simulation with success/failure scenarios
 */
public class PaymentManager {
    
    public enum PaymentMethod {
        CREDIT_CARD("Credit Card"),
        DEBIT_CARD("Debit Card"), 
        UPI("UPI"),
        NET_BANKING("Net Banking"),
        WALLET("Wallet");
        
        private final String displayName;
        
        PaymentMethod(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    public enum PaymentStatus {
        PENDING("Pending"),
        SUCCESS("Success"),
        FAILED("Failed"),
        REFUNDED("Refunded");
        
        private final String displayName;
        
        PaymentStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    public static class PaymentRequest {
        private int bookingId;
        private BigDecimal amount;
        private PaymentMethod method;
        private String cardNumber;
        private String cardHolderName;
        private String expiryDate;
        private String cvv;
        private String upiId;
        
        public PaymentRequest(int bookingId, BigDecimal amount2, PaymentMethod method) {
            this.bookingId = bookingId;
            this.amount = amount2;
            this.method = method;
        }
        
        // Getters and setters
        public int getBookingId() { return bookingId; }
        public BigDecimal getAmount() { return amount; }
        public PaymentMethod getMethod() { return method; }
        public String getCardNumber() { return cardNumber; }
        public String getCardHolderName() { return cardHolderName; }
        public String getExpiryDate() { return expiryDate; }
        public String getCvv() { return cvv; }
        public String getUpiId() { return upiId; }
        
        public void setCardDetails(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
            this.cardNumber = cardNumber;
            this.cardHolderName = cardHolderName;
            this.expiryDate = expiryDate;
            this.cvv = cvv;
        }
        
        public void setUpiId(String upiId) {
            this.upiId = upiId;
        }
    }
    
    public static class PaymentResult {
        private boolean success;
        private String transactionId;
        private String message;
        private PaymentStatus status;
        private int paymentId;
        
        public PaymentResult(boolean success, String transactionId, String message, PaymentStatus status) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
            this.status = status;
        }
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public String getTransactionId() { return transactionId; }
        public String getMessage() { return message; }
        public PaymentStatus getStatus() { return status; }
        public int getPaymentId() { return paymentId; }
        public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    }
    
    public static class PaymentReceipt {
        private int paymentId;
        private int bookingId;
        private String pnrNumber;
        private String passengerName;
        private String trainName;
        private String trainNumber;
        private String sourceStation;
        private String destinationStation;
        private String departureTime;
        private String arrivalTime;
        private String seatNumber;
        private String berthType;
        private String classType;
        private double amount;
        private PaymentMethod paymentMethod;
        private String transactionId;
        private Date paymentTime;
        private PaymentStatus status;
        
        // Getters and setters
        public int getPaymentId() { return paymentId; }
        public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
        
        public int getBookingId() { return bookingId; }
        public void setBookingId(int bookingId) { this.bookingId = bookingId; }
        
        public String getPnrNumber() { return pnrNumber; }
        public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }
        
        public String getPassengerName() { return passengerName; }
        public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
        
        public String getTrainName() { return trainName; }
        public void setTrainName(String trainName) { this.trainName = trainName; }
        
        public String getTrainNumber() { return trainNumber; }
        public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
        
        public String getSourceStation() { return sourceStation; }
        public void setSourceStation(String sourceStation) { this.sourceStation = sourceStation; }
        
        public String getDestinationStation() { return destinationStation; }
        public void setDestinationStation(String destinationStation) { this.destinationStation = destinationStation; }
        
        public String getDepartureTime() { return departureTime; }
        public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
        
        public String getArrivalTime() { return arrivalTime; }
        public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
        
        public String getSeatNumber() { return seatNumber; }
        public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
        
        public String getBerthType() { return berthType; }
        public void setBerthType(String berthType) { this.berthType = berthType; }
        
        public String getClassType() { return classType; }
        public void setClassType(String classType) { this.classType = classType; }
        
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        
        public PaymentMethod getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
        
        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
        
        public Date getPaymentTime() { return paymentTime; }
        public void setPaymentTime(Date paymentTime) { this.paymentTime = paymentTime; }
        
        public PaymentStatus getStatus() { return status; }
        public void setStatus(PaymentStatus status) { this.status = status; }
    }
    
    private Connection connection;
    private Random random;
    
    public PaymentManager() throws SQLException {
        this.connection = DatabaseManager.getConnection();
        this.random = new Random();
    }
    
    /**
     * Process payment through dummy payment gateway
     */
    public PaymentResult processPayment(PaymentRequest request) throws SQLException {
        // Simulate payment gateway processing delay
        try {
            Thread.sleep(2000); // 2 second delay for realism
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Dummy payment gateway simulation
        PaymentResult gatewayResponse = simulatePaymentGateway(request);
        
        // Create payment record in database
        int paymentId = createPaymentRecord(request, gatewayResponse);
        gatewayResponse.setPaymentId(paymentId);
        
        // Handle success/failure scenarios
        if (gatewayResponse.isSuccess()) {
            // Update booking status to confirmed
            updateBookingStatus(request.getBookingId(), "Confirmed");
        } else {
            // Handle payment failure - rollback booking if needed
            handlePaymentFailure(request.getBookingId());
        }
        
        return gatewayResponse;
    }
    
    /**
     * Simulate dummy payment gateway with random success/failure
     */
    private PaymentResult simulatePaymentGateway(PaymentRequest request) {
        // 90% success rate for simulation
        boolean success = random.nextDouble() > 0.1;
        
        String transactionId = generateTransactionId();
        PaymentStatus status;
        String message;
        
        if (success) {
            status = PaymentStatus.SUCCESS;
            message = "Payment processed successfully";
            
            // Additional validation based on payment method
            if (request.getMethod() == PaymentMethod.CREDIT_CARD || 
                request.getMethod() == PaymentMethod.DEBIT_CARD) {
                
                if (request.getCardNumber() == null || request.getCardNumber().length() < 16) {
                    success = false;
                    status = PaymentStatus.FAILED;
                    message = "Invalid card number";
                } else if (request.getCvv() == null || request.getCvv().length() != 3) {
                    success = false;
                    status = PaymentStatus.FAILED;
                    message = "Invalid CVV";
                }
            } else if (request.getMethod() == PaymentMethod.UPI) {
                if (request.getUpiId() == null || !request.getUpiId().contains("@")) {
                    success = false;
                    status = PaymentStatus.FAILED;
                    message = "Invalid UPI ID";
                }
            }
        } else {
            status = PaymentStatus.FAILED;
            String[] failureReasons = {
                "Insufficient funds",
                "Card expired",
                "Transaction declined by bank",
                "Network timeout",
                "Invalid credentials"
            };
            message = failureReasons[random.nextInt(failureReasons.length)];
        }
        
        return new PaymentResult(success, transactionId, message, status);
    }
    
    /**
     * Generate unique transaction ID
     */
    private String generateTransactionId() {
        long timestamp = System.currentTimeMillis();
        int randomNum = 1000 + random.nextInt(9000);
        return "TXN" + timestamp + randomNum;
    }
    
    /**
     * Create payment record in database
     */
    private int createPaymentRecord(PaymentRequest request, PaymentResult result) throws SQLException {
        String sql = "INSERT INTO payments (booking_id, amount, status, payment_method, transaction_id, payment_time) VALUES (?, ?, ?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, request.getBookingId());
            stmt.setBigDecimal(2, request.getAmount());
            stmt.setString(3, result.getStatus().toString());
            stmt.setString(4, request.getMethod().toString());
            stmt.setString(5, result.getTransactionId());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to create payment record");
        }
    }
    
    /**
     * Update booking status after payment
     */
    private void updateBookingStatus(int bookingId, String status) throws SQLException {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Handle payment failure - implement rollback logic
     */
    private void handlePaymentFailure(int bookingId) throws SQLException {
        // Start transaction for rollback
        connection.setAutoCommit(false);
        
        try {
            // Get booking details
            String getBookingSql = "SELECT seat_id FROM bookings WHERE booking_id = ?";
            int seatId = -1;
            
            try (PreparedStatement stmt = connection.prepareStatement(getBookingSql)) {
                stmt.setInt(1, bookingId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    seatId = rs.getInt("seat_id");
                }
            }
            
            // Mark seat as available again
            if (seatId != -1) {
                String updateSeatSql = "UPDATE seats SET is_available = TRUE WHERE seat_id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateSeatSql)) {
                    stmt.setInt(1, seatId);
                    stmt.executeUpdate();
                }
            }
            
            // Update booking status to cancelled
            updateBookingStatus(bookingId, "Cancelled");
            
            // Commit transaction
            connection.commit();
            
        } catch (SQLException e) {
            // Rollback on error
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    /**
     * Generate payment receipt with full booking details
     */
    public PaymentReceipt generateReceipt(int paymentId) throws SQLException {
        String sql = """
            SELECT 
                p.payment_id, p.booking_id, p.amount, p.payment_method, 
                p.transaction_id, p.payment_time, p.status as payment_status,
                b.pnr_number, b.passenger_name,
                t.train_name, t.train_number,
                r.source_station, r.destination_station, r.departure_time, r.arrival_time,
                s.seat_number, s.berth_type,
                cl.class_type
            FROM payments p
            JOIN bookings b ON p.booking_id = b.booking_id
            JOIN trains t ON b.train_id = t.train_id
            JOIN routes r ON b.route_id = r.route_id
            LEFT JOIN seats s ON b.seat_id = s.seat_id
            LEFT JOIN compartments comp ON s.compartment_id = comp.compartment_id
            LEFT JOIN classes cl ON comp.class_id = cl.class_id
            WHERE p.payment_id = ?
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                PaymentReceipt receipt = new PaymentReceipt();
                receipt.setPaymentId(rs.getInt("payment_id"));
                receipt.setBookingId(rs.getInt("booking_id"));
                receipt.setPnrNumber(rs.getString("pnr_number"));
                receipt.setPassengerName(rs.getString("passenger_name"));
                receipt.setTrainName(rs.getString("train_name"));
                receipt.setTrainNumber(rs.getString("train_number"));
                receipt.setSourceStation(rs.getString("source_station"));
                receipt.setDestinationStation(rs.getString("destination_station"));
                receipt.setDepartureTime(rs.getString("departure_time"));
                receipt.setArrivalTime(rs.getString("arrival_time"));
                receipt.setSeatNumber(rs.getString("seat_number"));
                receipt.setBerthType(rs.getString("berth_type"));
                receipt.setClassType(rs.getString("class_type"));
                receipt.setAmount(rs.getDouble("amount"));
                receipt.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method").replace(" ", "_").toUpperCase()));
                receipt.setTransactionId(rs.getString("transaction_id"));
                receipt.setPaymentTime(rs.getTimestamp("payment_time"));
                receipt.setStatus(PaymentStatus.valueOf(rs.getString("payment_status").toUpperCase()));
                
                return receipt;
            }
            
            throw new SQLException("Payment receipt not found for payment ID: " + paymentId);
        }
    }
    
    /**
     * Generate printable receipt text
     */
    public String generatePrintableReceipt(PaymentReceipt receipt) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        
        sb.append("=".repeat(60)).append("\n");
        sb.append("                    BOOKMYTICKET                      \n");
        sb.append("                 PAYMENT RECEIPT                     \n");
        sb.append("=".repeat(60)).append("\n\n");
        
        sb.append("Receipt No      : ").append(receipt.getPaymentId()).append("\n");
        sb.append("PNR Number      : ").append(receipt.getPnrNumber()).append("\n");
        sb.append("Transaction ID  : ").append(receipt.getTransactionId()).append("\n");
        sb.append("Payment Time    : ").append(dateFormat.format(receipt.getPaymentTime())).append("\n");
        sb.append("Payment Status  : ").append(receipt.getStatus().getDisplayName()).append("\n\n");
        
        sb.append("PASSENGER DETAILS\n");
        sb.append("-".repeat(30)).append("\n");
        sb.append("Name            : ").append(receipt.getPassengerName()).append("\n\n");
        
        sb.append("JOURNEY DETAILS\n");
        sb.append("-".repeat(30)).append("\n");
        sb.append("Train           : ").append(receipt.getTrainName()).append(" (").append(receipt.getTrainNumber()).append(")\n");
        sb.append("From            : ").append(receipt.getSourceStation()).append("\n");
        sb.append("To              : ").append(receipt.getDestinationStation()).append("\n");
        sb.append("Departure       : ").append(receipt.getDepartureTime()).append("\n");
        sb.append("Arrival         : ").append(receipt.getArrivalTime()).append("\n");
        
        if (receipt.getSeatNumber() != null) {
            sb.append("Seat            : ").append(receipt.getSeatNumber());
            if (receipt.getBerthType() != null) {
                sb.append(" (").append(receipt.getBerthType()).append(")");
            }
            sb.append("\n");
        }
        
        if (receipt.getClassType() != null) {
            sb.append("Class           : ").append(receipt.getClassType()).append("\n");
        }
        
        sb.append("\nPAYMENT DETAILS\n");
        sb.append("-".repeat(30)).append("\n");
        sb.append("Amount          : ₹").append(String.format("%.2f", receipt.getAmount())).append("\n");
        sb.append("Payment Method  : ").append(receipt.getPaymentMethod().getDisplayName()).append("\n\n");
        
        sb.append("IMPORTANT INFORMATION\n");
        sb.append("-".repeat(30)).append("\n");
        sb.append("• Please carry a valid ID proof during journey\n");
        sb.append("• Tickets are non-transferable\n");
        sb.append("• Report at station 30 minutes before departure\n");
        sb.append("• Keep this receipt for future reference\n\n");
        
        sb.append("Thank you for choosing BookMyTicket!\n");
        sb.append("=".repeat(60)).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Refund payment (for cancellations)
     */
    public PaymentResult refundPayment(int paymentId, String reason) throws SQLException {
        // Simulate refund processing
        String refundTransactionId = "REF" + generateTransactionId().substring(3);
        
        // Update payment status
        String sql = "UPDATE payments SET status = 'Refunded' WHERE payment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            int updated = stmt.executeUpdate();
            
            if (updated > 0) {
                return new PaymentResult(true, refundTransactionId, 
                    "Refund processed successfully. Amount will be credited within 5-7 business days.", 
                    PaymentStatus.REFUNDED);
            } else {
                return new PaymentResult(false, null, "Payment not found", PaymentStatus.FAILED);
            }
        }
    }
}