package com.bookmytrain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping({"/", "/index", "/home"})
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/search")
    public String search() {
        return "forward:/search.html";
    }

    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "forward:/register.html";
    }

    @GetMapping("/bookings")
    public String bookings() {
        return "forward:/bookings.html";
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
