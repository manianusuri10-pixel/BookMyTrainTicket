package BookMyTrainTicket;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Route model class representing a train route
 */
public class Route {
    private int routeId;
    private int trainId;
    private String sourceStation;
    private String destinationStation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private BigDecimal price;
    
    // Constructors
    public Route() {}
    
    public Route(int routeId, int trainId, String sourceStation, String destinationStation, 
                 LocalTime departureTime, LocalTime arrivalTime, BigDecimal price) {
        this.routeId = routeId;
        this.trainId = trainId;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
    }
    
    public Route(int trainId, String sourceStation, String destinationStation, 
                 LocalTime departureTime, LocalTime arrivalTime, BigDecimal price) {
        this.trainId = trainId;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
    }
    
    // Getters and Setters
    public int getRouteId() {
        return routeId;
    }
    
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
    
    public int getTrainId() {
        return trainId;
    }
    
    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }
    
    public String getSourceStation() {
        return sourceStation;
    }
    
    public void setSourceStation(String sourceStation) {
        this.sourceStation = sourceStation;
    }
    
    public String getDestinationStation() {
        return destinationStation;
    }
    
    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }
    
    public LocalTime getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }
    
    public LocalTime getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    @Override
    public String toString() {
        return sourceStation + " → " + destinationStation + 
               " (Dep: " + departureTime + ", Arr: " + arrivalTime + ", ₹" + price + ")";
    }
}
