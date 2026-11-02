package com.bookmytrain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingManager bookingManager;
    public BookingController() throws Exception { bookingManager=new BookingManager(); }

    @PostMapping public ResponseEntity<?> create(@RequestBody BookingRequest r) {
        try {
            BookingManager.BookingResult x=bookingManager.createBooking(r.userId(),r.seatId(),r.trainId(),r.routeId(),r.passengerName(),r.passengerAge());
            return ResponseEntity.ok(Map.of("success",x.isSuccess(),"message",x.getMessage(),"id",x.getId(),"status",x.getStatus()));
        } catch(Exception e){return ResponseEntity.internalServerError().body(Map.of("success",false,"message",message(e)));}
    }
    @GetMapping("/user/{userId}") public ResponseEntity<?> byUser(@PathVariable int userId) {
        try{return ResponseEntity.ok(bookingManager.getBookingsForUser(userId));}
        catch(Exception e){return ResponseEntity.internalServerError().body(Map.of("success",false,"message",message(e)));}
    }
    @PostMapping("/{bookingId}/cancel") public ResponseEntity<?> cancel(@PathVariable int bookingId) {
        try {boolean ok=bookingManager.cancelBooking(bookingId); return ResponseEntity.ok(Map.of("success",ok,"message",ok?"Booking cancelled":"Booking not found"));}
        catch(Exception e){return ResponseEntity.internalServerError().body(Map.of("success",false,"message",message(e)));}
    }
    private String message(Exception e){return e.getMessage()==null?"Server error":e.getMessage();}
    public record BookingRequest(int userId,int seatId,int trainId,int routeId,String passengerName,int passengerAge){}
}
