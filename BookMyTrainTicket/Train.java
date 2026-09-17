package BookMyTrainTicket;

/**
 * Train model class representing a train in the booking system
 */
public class Train {
    private int trainId;
    private String trainName;
    private String trainNumber;
    
    // Constructors
    public Train() {}
    
    public Train(int trainId, String trainName, String trainNumber) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.trainNumber = trainNumber;
    }
    
    public Train(String trainName, String trainNumber) {
        this.trainName = trainName;
        this.trainNumber = trainNumber;
    }
    
    // Getters and Setters
    public int getTrainId() {
        return trainId;
    }
    
    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }
    
    public String getTrainName() {
        return trainName;
    }
    
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }
    
    public String getTrainNumber() {
        return trainNumber;
    }
    
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
    
    @Override
    public String toString() {
        return trainName + " (" + trainNumber + ")";
    }
}