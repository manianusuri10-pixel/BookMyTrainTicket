package com.bookmytrain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final LoginOperations loginOperations;
    public AuthController() throws Exception { loginOperations = new LoginOperations(); }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginRequest r) {
        try {
            User u = loginOperations.authenticateUser(r.username(), r.password());
            if (u == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success",false,"message","Invalid username or password"));
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("success",true); m.put("message","Login successful"); m.put("userId",u.getUserId());
            m.put("username",u.getUsername()); m.put("email",u.getEmail()==null?"":u.getEmail()); m.put("role",u.getRole().name());
            return ResponseEntity.ok(m);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("success",false,"message",message(e)));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@RequestBody RegisterRequest r) {
        try {
            User.UserRole role = (r.role()==null || r.role().isBlank()) ? User.UserRole.Regular : User.UserRole.valueOf(r.role());
            boolean ok = loginOperations.registerUser(r.username(),r.password(),r.email(),role);
            if(!ok) return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("success",false,"message","Username already exists"));
            return ResponseEntity.ok(Map.of("success",true,"message","Registration successful"));
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success",false,"message","Invalid role"));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("success",false,"message",message(e)));
        }
    }
    private String message(Exception e){return e.getMessage()==null?"Server error":e.getMessage();}
    public record LoginRequest(String username,String password){}
    public record RegisterRequest(String username,String password,String email,String role){}
}
