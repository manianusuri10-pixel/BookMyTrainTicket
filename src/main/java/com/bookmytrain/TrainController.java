package com.bookmytrain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/trains")
public class TrainController {
    private final TrainManager trainManager;
    private final SeatAvailabilityManager seatManager;
    public TrainController() throws Exception { trainManager=new TrainManager(); seatManager=new SeatAvailabilityManager(); }

    @GetMapping public ResponseEntity<?> all() {
        try{return ResponseEntity.ok(trainManager.getAllTrains());}
        catch(Exception e){return ResponseEntity.internalServerError().body(Map.of("success",false,"message",message(e)));}
    }
    @GetMapping("/search") public ResponseEntity<?> search(@RequestParam String source,@RequestParam String destination) {
        try{return ResponseEntity.ok(trainManager.searchTrains(source,destination));}
        catch(Exception e){return ResponseEntity.internalServerError().body(Map.of("success",false,"message",message(e)));}
    }
    @GetMapping("/{trainId}/seats") public ResponseEntity<?> seats(@PathVariable int trainId) {
        try{return ResponseEntity.ok(seatManager.getSeatsForTrain(trainId));}
        catch(Exception e){return ResponseEntity.internalServerError().body(Map.of("success",false,"message",message(e)));}
    }
    private String message(Exception e){return e.getMessage()==null?"Server error":e.getMessage();}
}
