package BookMyTrainTicket;

/**
 * User model class representing a user in the train booking system
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    
    public enum UserRole {
        Admin, Regular, Senior, DifferentlyAbled
    }
    
    // Constructors
    public User() {}
    
    public User(int userId, String username, String password, String email, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    
    public User(String username, String password, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public boolean isAdmin() {
        return role == UserRole.Admin;
    }
    
    public boolean isSenior() {
        return role == UserRole.Senior;
    }
    
    public boolean isDifferentlyAbled() {
        return role == UserRole.DifferentlyAbled;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}