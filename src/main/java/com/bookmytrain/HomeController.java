package com.bookmytrain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/api/health")
    @ResponseBody
    public Map<String, Object> health() {
        boolean dbConnected = false;
        try {
            dbConnected = DatabaseManager.getInstance().isDbConnected();
        } catch (Exception ignored) {}

        return Map.of(
            "status", "UP",
            "application", "BookMyTrainTicket",
            "database", dbConnected ? "CONNECTED" : "UNAVAILABLE (Fallback active)"
        );
    }
}
