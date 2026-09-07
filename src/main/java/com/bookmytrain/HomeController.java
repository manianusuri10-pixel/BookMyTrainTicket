package com.bookmytrain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
@RestController
public class HomeController {
    @GetMapping("/api/health")
    public Map<String,String> health() { return Map.of("status","UP","application","BookMyTrainTicket"); }
}
