package BookMyTrainTicket;

/**
 * Seat model class representing a seat in a train compartment
 */
public class Seat {
    private int seatId;
    private int compartmentId;
    private BerthType berthType;
    private String seatNumber;
    private boolean isAvailable;
    
    public enum BerthType {
        Lower, Middle, Upper, Side_Lower, Side_Upper
    }
    
    // Constructors
    public Seat() {}
    
    public Seat(int seatId, int compartmentId, BerthType berthType, String seatNumber, boolean isAvailable) {
        this.seatId = seatId;
        this.compartmentId = compartmentId;
        this.berthType = berthType;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
    }
    
    public Seat(int compartmentId, BerthType berthType, String seatNumber, boolean isAvailable) {
        this.compartmentId = compartmentId;
        this.berthType = berthType;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
    }
    
    // Getters and Setters
    public int getSeatId() {
        return seatId;
    }
    
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
    
    public int getCompartmentId() {
        return compartmentId;
    }
    
    public void setCompartmentId(int compartmentId) {
        this.compartmentId = compartmentId;
    }
    
    public BerthType getBerthType() {
        return berthType;
    }
    
    public void setBerthType(BerthType berthType) {
        this.berthType = berthType;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    public static BerthType parseBerthType(String dbValue) {
        switch (dbValue) {
            case "Lower": return BerthType.Lower;
            case "Middle": return BerthType.Middle;
            case "Upper": return BerthType.Upper;
            case "Side Lower": return BerthType.Side_Lower;
            case "Side Upper": return BerthType.Side_Upper;
            default: return BerthType.Lower;
        }
    }
    
    public String getBerthTypeDbValue() {
        switch (berthType) {
            case Lower: return "Lower";
            case Middle: return "Middle";
            case Upper: return "Upper";
            case Side_Lower: return "Side Lower";
            case Side_Upper: return "Side Upper";
            default: return "Lower";
        }
    }
    
    @Override
    public String toString() {
        return seatNumber + " (" + berthType + ")" + (isAvailable ? " - Available" : " - Occupied");
    }
}
