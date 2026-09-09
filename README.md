# BookMyTicket - Java Swing Train Booking System

A comprehensive train booking application built exclusively in Java with Swing GUI and MySQL database integration.

## Features

### Core Functionality
- **User Authentication**: Login/Register with role-based access control
- **Train Search**: Search trains by source and destination stations
- **Seat Management**: Interactive seat selection with visual seat map
- **Booking System**: Complete booking workflow with passenger details
- **RAC Queue**: Reservation Against Cancellation queue management
- **Waitlist Management**: Automatic waitlist handling when trains are full
- **Payment Processing**: Basic payment tracking system

### User Roles
- **Regular Users**: Standard booking privileges
- **Senior Citizens**: Priority lower berth recommendations
- **Differently Abled**: Priority lower berth recommendations
- **Admin**: Full system management capabilities

### GUI Features
- **Professional Swing Interface**: Clean, user-friendly design
- **Tabbed Navigation**: Easy access to different features
- **Real-time Updates**: Dynamic content refresh
- **Responsive Design**: Adaptable layout for different screen sizes

## Technical Architecture

### Database Schema
- **MySQL Database**: Comprehensive relational schema
- **Tables**: users, trains, routes, classes, compartments, seats, bookings, payments, waitlist, rac
- **Foreign Key Relationships**: Maintains data integrity
- **Enum Types**: Structured role and status management

### Java Components

#### Core Models
- `User.java` - User entity with role management
- `Train.java` - Train information model
- `Route.java` - Route details with pricing
- `Seat.java` - Seat management with berth types

#### Business Logic
- `LoginOperations.java` - Authentication and user management
- `TrainManager.java` - Train operations and search
- `BookingManager.java` - Complete booking workflow
- `SeatAvailabilityManager.java` - Seat allocation and recommendations
- `WaitlistManager.java` - Waitlist queue operations
- `RACQueue.java` - RAC queue management

#### Database Layer
- `DatabaseManager.java` - Connection management and schema initialization

#### GUI Application
- `BookMyTicketApp.java` - Main Swing application with complete UI

## Setup Instructions

### Prerequisites
1. **Java Development Kit (JDK)**: Version 8 or higher
2. **MySQL Server**: Version 5.7 or higher
3. **MySQL Connector/J**: JDBC driver for MySQL

### Database Setup
1. Install and start MySQL server
2. Create a database named `train_booking` (automatically created by application)
3. Update database credentials in `DatabaseManager.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/train_booking";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_password";
   ```

### Application Setup
1. Download MySQL Connector/J JAR file
2. Place it in the project directory as `mysql-connector-java-8.0.33.jar`
3. Compile and run using the provided runner:
   ```bash
   javac RunApp.java
   java RunApp
   ```

### Alternative Compilation
```bash
# Manual compilation (with MySQL connector in classpath)
javac -cp mysql-connector-java-8.0.33.jar:. *.java

# Run the application
java -cp mysql-connector-java-8.0.33.jar:. BookMyTicketApp
```

## Usage Guide

### First Time Setup
1. Run the application
2. Register a new user account
3. Login with your credentials
4. Default admin account: username=`admin`, password=`admin123`

### Booking a Ticket
1. Go to "Search Trains" tab
2. Enter source and destination stations
3. Click "Search Trains"
4. Select a train from results
5. Choose your preferred seat (recommended seats highlighted)
6. Enter passenger details
7. Confirm booking

### Managing Bookings
1. Go to "My Bookings" tab
2. View all your bookings with details
3. Check booking status (Confirmed, RAC, Waiting, Cancelled)

### Admin Functions
1. Login as admin user
2. Access "Admin Panel" tab
3. Manage trains, routes, and users

## Database Schema Details

### Key Tables
- **users**: User accounts with role-based permissions
- **trains**: Train master data
- **routes**: Source-destination mapping with schedules
- **seats**: Hierarchical seat structure (Train → Class → Compartment → Seat)
- **bookings**: Reservation records with passenger details
- **waitlist/rac**: Queue management for full trains

### Sample Data
The application includes sample data:
- 3 trains (Rajdhani Express, Shatabdi Express, Duronto Express)
- Multiple routes and classes
- Seat allocation across compartments
- Admin and test user accounts

## Features in Detail

### Seat Recommendation System
- **Senior Citizens**: Automatically recommends lower berths
- **Differently Abled**: Priority access to lower berths
- **Visual Highlighting**: Recommended seats are color-coded
- **Availability Check**: Real-time seat availability

### Queue Management
- **RAC (Reservation Against Cancellation)**: Limited positions (max 10)
- **Waitlist**: Unlimited positions with automatic progression
- **Auto-promotion**: Cancellations trigger automatic seat allocation

### User Experience
- **Intuitive Navigation**: Tab-based interface
- **Real-time Feedback**: Instant booking confirmations
- **Error Handling**: Comprehensive error messages
- **Professional Design**: Clean, modern Swing interface

## Troubleshooting

### Common Issues
1. **Database Connection Failed**
   - Check MySQL server is running
   - Verify database credentials
   - Ensure MySQL Connector/J is in classpath

2. **Compilation Errors**
   - Ensure JDK is installed and in PATH
   - Check MySQL connector JAR file location

3. **GUI Not Displaying**
   - Verify Java Swing is supported
   - Check display settings

### MySQL Setup
```sql
-- Create database manually if needed
CREATE DATABASE train_booking;
USE train_booking;

-- The application will create all tables automatically
```

## Technical Notes

### Performance Considerations
- Connection pooling for database operations
- Efficient query design for large datasets
- Optimized seat allocation algorithms

### Security Features
- Basic password authentication (production: implement hashing)
- Role-based access control
- SQL injection prevention through prepared statements

### Extensibility
- Modular design for easy feature additions
- Clear separation of concerns
- Comprehensive error handling

## Development

### Adding New Features
1. Create model classes for new entities
2. Add database operations to managers
3. Extend GUI components as needed
4. Update schema in `DatabaseManager.java`

### Code Structure
- **Models**: Data entities and enums
- **Managers**: Business logic and database operations
- **GUI**: Swing interface components
- **Utils**: Helper classes and utilities

This Java application provides a complete train booking solution with professional GUI and robust backend functionality, all implemented exclusively in Java without any external frontend technologies.

✓ Complete Java Application Created
Core Java Files:

BookMyTicketApp.java - Main Swing GUI application
DatabaseManager.java - MySQL database management
User.java, Train.java, Route.java, Seat.java - Model classes
LoginOperations.java - Authentication system
TrainManager.java - Train search and management
BookingManager.java - Complete booking workflow
SeatAvailabilityManager.java - Seat allocation with user-specific recommendations
WaitlistManager.java - Waitlist queue management
RACQueue.java - RAC (Reservation Against Cancellation) system
RunApp.java - Compilation and execution helper
✓ Key Features Implemented
User System:

Role-based access (Admin, Regular, Senior, DifferentlyAbled)
Professional login/register interface
Seat recommendations based on user type (lower berths for seniors/disabled)
Booking System:

Train search by source/destination
Interactive seat selection with visual highlighting
Complete booking workflow with passenger details
RAC queue (max 10 positions) and unlimited waitlist
Automatic promotion when seats become available
GUI Features:

Professional Swing interface with tabbed navigation
Real-time seat availability display
My Bookings section with detailed history
Admin panel for system management
Database Integration:

Complete MySQL schema with sample data
Automatic database and table creation
Comprehensive relationships between all entities
✓ Technical Highlights
Pure Java Implementation - No other frontend technologies used
Professional GUI - Clean, responsive Swing interface
Advanced Queue Management - RAC and waitlist with automatic progression
Role-Based Recommendations - Smart seat suggestions based on user type
Transaction Management - Proper database transactions for booking integrity
Sample Data - Includes trains, routes, and test users (admin/admin123)


THe SQL commands to Create Database are:
-- BookMyTicket Train Booking System - Complete Database Setup
-- MySQL Database Schema and Sample Data

-- Create the database
CREATE DATABASE IF NOT EXISTS train_booking;
USE train_booking;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS waitlist;
DROP TABLE IF EXISTS rac;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS seats;
DROP TABLE IF EXISTS compartments;
DROP TABLE IF EXISTS classes;
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS trains;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    role ENUM('Admin', 'Regular', 'Senior', 'DifferentlyAbled') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create trains table
CREATE TABLE trains (
    train_id INT AUTO_INCREMENT PRIMARY KEY,
    train_name VARCHAR(100) NOT NULL,
    train_number VARCHAR(20) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create routes table
CREATE TABLE routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    train_id INT NOT NULL,
    source_station VARCHAR(100) NOT NULL,
    destination_station VARCHAR(100) NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    distance_km INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (train_id) REFERENCES trains(train_id) ON DELETE CASCADE
);

-- Create classes table (AC 1st, AC 2nd, AC 3rd, Sleeper, etc.)
CREATE TABLE classes (
    class_id INT AUTO_INCREMENT PRIMARY KEY,
    train_id INT NOT NULL,
    class_type VARCHAR(50) NOT NULL,
    base_price_multiplier DECIMAL(3,2) DEFAULT 1.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (train_id) REFERENCES trains(train_id) ON DELETE CASCADE
);

-- Create compartments table
CREATE TABLE compartments (
    compartment_id INT AUTO_INCREMENT PRIMARY KEY,
    class_id INT NOT NULL,
    compartment_name VARCHAR(50) NOT NULL,
    total_seats INT DEFAULT 24,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE CASCADE
);

-- Create seats table
CREATE TABLE seats (
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    compartment_id INT NOT NULL,
    berth_type ENUM('Lower', 'Middle', 'Upper', 'Side Lower', 'Side Upper') NOT NULL,
    seat_number VARCHAR(20) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (compartment_id) REFERENCES compartments(compartment_id) ON DELETE CASCADE,
    UNIQUE KEY unique_seat_per_compartment (compartment_id, seat_number)
);

-- Create bookings table
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    seat_id INT,
    train_id INT NOT NULL,
    route_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_age INT NOT NULL,
    booking_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('Confirmed', 'Cancelled', 'RAC', 'Waiting') DEFAULT 'Confirmed',
    pnr_number VARCHAR(20) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seats(seat_id) ON DELETE SET NULL,
    FOREIGN KEY (train_id) REFERENCES trains(train_id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);

-- Create payments table
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status ENUM('Success', 'Failed', 'Pending') DEFAULT 'Pending',
    payment_method VARCHAR(50),
    transaction_id VARCHAR(100),
    payment_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);

-- Create waitlist table
CREATE TABLE waitlist (
    waitlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    train_id INT NOT NULL,
    route_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_age INT NOT NULL,
    request_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('Waiting', 'Promoted') DEFAULT 'Waiting',
    position INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (train_id) REFERENCES trains(train_id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);

-- Create RAC (Reservation Against Cancellation) table
CREATE TABLE rac (
    rac_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    train_id INT NOT NULL,
    route_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_age INT NOT NULL,
    request_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('RAC', 'Promoted') DEFAULT 'RAC',
    position INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (train_id) REFERENCES trains(train_id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_train_route ON bookings(train_id, route_id);
CREATE INDEX idx_seats_compartment ON seats(compartment_id);
CREATE INDEX idx_seats_available ON seats(is_available);
CREATE INDEX idx_routes_train ON routes(train_id);
CREATE INDEX idx_routes_stations ON routes(source_station, destination_station);
CREATE INDEX idx_waitlist_train_route ON waitlist(train_id, route_id, position);
CREATE INDEX idx_rac_train_route ON rac(train_id, route_id, position);

-- Insert sample users
INSERT INTO users (username, password, email, role) VALUES
('admin', 'admin123', 'admin@bookmyticket.com', 'Admin'),
('john_doe', 'password123', 'john.doe@email.com', 'Regular'),
('senior_citizen', 'senior123', 'senior@email.com', 'Senior'),
('special_user', 'special123', 'special@email.com', 'DifferentlyAbled'),
('test_user', 'test123', 'test@email.com', 'Regular');

-- Insert sample trains
INSERT INTO trains (train_name, train_number) VALUES
('Rajdhani Express', '12301'),
('Shatabdi Express', '12002'),
('Duronto Express', '12259'),
('Gatimaan Express', '12049'),
('Vande Bharat Express', '22439');

-- Insert sample routes
INSERT INTO routes (train_id, source_station, destination_station, departure_time, arrival_time, price, distance_km) VALUES
(1, 'New Delhi', 'Mumbai Central', '16:55:00', '08:35:00', 1500.00, 1384),
(1, 'New Delhi', 'Ahmedabad', '16:55:00', '05:15:00', 1200.00, 934),
(2, 'New Delhi', 'Chandigarh', '17:20:00', '21:00:00', 800.00, 245),
(2, 'New Delhi', 'Amritsar', '17:20:00', '23:30:00', 950.00, 449),
(3, 'Mumbai Central', 'Pune', '06:00:00', '09:30:00', 600.00, 192),
(3, 'Mumbai Central', 'Nagpur', '06:00:00', '17:45:00', 900.00, 825),
(4, 'New Delhi', 'Agra Cantonment', '08:10:00', '09:50:00', 750.00, 188),
(5, 'New Delhi', 'Varanasi', '06:00:00', '14:00:00', 1100.00, 765);

-- Insert sample classes
INSERT INTO classes (train_id, class_type, base_price_multiplier) VALUES
(1, 'AC 1st Class', 3.00),
(1, 'AC 2 Tier', 2.00),
(1, 'AC 3 Tier', 1.50),
(2, 'AC Chair Car', 1.20),
(2, 'Executive Chair Car', 1.80),
(3, 'AC 3 Tier', 1.50),
(3, 'Sleeper', 1.00),
(4, 'AC Chair Car', 1.20),
(4, 'Executive Chair Car', 1.80),
(5, 'AC Chair Car', 1.20),
(5, 'Executive Chair Car', 1.80);

-- Insert sample compartments
INSERT INTO compartments (class_id, compartment_name, total_seats) VALUES
-- Rajdhani Express compartments
(1, 'H1', 18),  -- AC 1st Class
(1, 'H2', 18),
(2, 'A1', 46),  -- AC 2 Tier
(2, 'A2', 46),
(2, 'A3', 46),
(3, 'B1', 64),  -- AC 3 Tier
(3, 'B2', 64),
(3, 'B3', 64),

-- Shatabdi Express compartments
(4, 'CC1', 78), -- AC Chair Car
(4, 'CC2', 78),
(5, 'EC1', 52), -- Executive Chair Car

-- Duronto Express compartments
(6, 'B1', 64),  -- AC 3 Tier
(6, 'B2', 64),
(7, 'S1', 72),  -- Sleeper
(7, 'S2', 72),

-- Gatimaan Express compartments
(8, 'CC1', 78),  -- AC Chair Car
(9, 'EC1', 52),  -- Executive Chair Car

-- Vande Bharat Express compartments
(10, 'C1', 78), -- AC Chair Car
(11, 'EC1', 52); -- Executive Chair Car

-- Insert sample seats (we'll create seats for the first few compartments as examples)
-- AC 1st Class compartments (18 seats each)
INSERT INTO seats (compartment_id, berth_type, seat_number, is_available) VALUES
-- H1 compartment
(1, 'Lower', 'H1-1', TRUE), (1, 'Upper', 'H1-2', TRUE),
(1, 'Lower', 'H1-3', TRUE), (1, 'Upper', 'H1-4', TRUE),
(1, 'Lower', 'H1-5', TRUE), (1, 'Upper', 'H1-6', TRUE),
(1, 'Lower', 'H1-7', TRUE), (1, 'Upper', 'H1-8', TRUE),
(1, 'Lower', 'H1-9', TRUE), (1, 'Upper', 'H1-10', TRUE),
(1, 'Lower', 'H1-11', TRUE), (1, 'Upper', 'H1-12', TRUE),
(1, 'Lower', 'H1-13', TRUE), (1, 'Upper', 'H1-14', TRUE),
(1, 'Lower', 'H1-15', TRUE), (1, 'Upper', 'H1-16', TRUE),
(1, 'Lower', 'H1-17', TRUE), (1, 'Upper', 'H1-18', TRUE);

-- AC 2 Tier compartments (46 seats each)
INSERT INTO seats (compartment_id, berth_type, seat_number, is_available) VALUES
-- A1 compartment - creating a typical AC 2 Tier layout
(3, 'Lower', 'A1-1', TRUE), (3, 'Upper', 'A1-2', TRUE),
(3, 'Lower', 'A1-3', TRUE), (3, 'Upper', 'A1-4', TRUE),
(3, 'Lower', 'A1-5', TRUE), (3, 'Upper', 'A1-6', TRUE),
(3, 'Lower', 'A1-7', TRUE), (3, 'Upper', 'A1-8', TRUE),
(3, 'Side Lower', 'A1-9', TRUE), (3, 'Side Upper', 'A1-10', TRUE),
(3, 'Lower', 'A1-11', TRUE), (3, 'Upper', 'A1-12', TRUE),
(3, 'Lower', 'A1-13', TRUE), (3, 'Upper', 'A1-14', TRUE),
(3, 'Lower', 'A1-15', TRUE), (3, 'Upper', 'A1-16', TRUE),
(3, 'Lower', 'A1-17', TRUE), (3, 'Upper', 'A1-18', TRUE),
(3, 'Side Lower', 'A1-19', TRUE), (3, 'Side Upper', 'A1-20', TRUE),
(3, 'Lower', 'A1-21', TRUE), (3, 'Upper', 'A1-22', TRUE),
(3, 'Lower', 'A1-23', TRUE), (3, 'Upper', 'A1-24', TRUE),
(3, 'Lower', 'A1-25', TRUE), (3, 'Upper', 'A1-26', TRUE),
(3, 'Lower', 'A1-27', TRUE), (3, 'Upper', 'A1-28', TRUE),
(3, 'Side Lower', 'A1-29', TRUE), (3, 'Side Upper', 'A1-30', TRUE),
(3, 'Lower', 'A1-31', TRUE), (3, 'Upper', 'A1-32', TRUE),
(3, 'Lower', 'A1-33', TRUE), (3, 'Upper', 'A1-34', TRUE),
(3, 'Lower', 'A1-35', TRUE), (3, 'Upper', 'A1-36', TRUE),
(3, 'Lower', 'A1-37', TRUE), (3, 'Upper', 'A1-38', TRUE),
(3, 'Side Lower', 'A1-39', TRUE), (3, 'Side Upper', 'A1-40', TRUE),
(3, 'Lower', 'A1-41', TRUE), (3, 'Upper', 'A1-42', TRUE),
(3, 'Lower', 'A1-43', TRUE), (3, 'Upper', 'A1-44', TRUE),
(3, 'Lower', 'A1-45', TRUE), (3, 'Upper', 'A1-46', TRUE);

-- AC 3 Tier compartments (64 seats each)
INSERT INTO seats (compartment_id, berth_type, seat_number, is_available) VALUES
-- B1 compartment - creating a typical AC 3 Tier layout
(6, 'Lower', 'B1-1', TRUE), (6, 'Middle', 'B1-2', TRUE), (6, 'Upper', 'B1-3', TRUE),
(6, 'Lower', 'B1-4', TRUE), (6, 'Middle', 'B1-5', TRUE), (6, 'Upper', 'B1-6', TRUE),
(6, 'Side Lower', 'B1-7', TRUE), (6, 'Side Upper', 'B1-8', TRUE),
(6, 'Lower', 'B1-9', TRUE), (6, 'Middle', 'B1-10', TRUE), (6, 'Upper', 'B1-11', TRUE),
(6, 'Lower', 'B1-12', TRUE), (6, 'Middle', 'B1-13', TRUE), (6, 'Upper', 'B1-14', TRUE),
(6, 'Side Lower', 'B1-15', TRUE), (6, 'Side Upper', 'B1-16', TRUE),
(6, 'Lower', 'B1-17', TRUE), (6, 'Middle', 'B1-18', TRUE), (6, 'Upper', 'B1-19', TRUE),
(6, 'Lower', 'B1-20', TRUE), (6, 'Middle', 'B1-21', TRUE), (6, 'Upper', 'B1-22', TRUE),
(6, 'Side Lower', 'B1-23', TRUE), (6, 'Side Upper', 'B1-24', TRUE),
(6, 'Lower', 'B1-25', TRUE), (6, 'Middle', 'B1-26', TRUE), (6, 'Upper', 'B1-27', TRUE),
(6, 'Lower', 'B1-28', TRUE), (6, 'Middle', 'B1-29', TRUE), (6, 'Upper', 'B1-30', TRUE),
(6, 'Side Lower', 'B1-31', TRUE), (6, 'Side Upper', 'B1-32', TRUE),
(6, 'Lower', 'B1-33', TRUE), (6, 'Middle', 'B1-34', TRUE), (6, 'Upper', 'B1-35', TRUE),
(6, 'Lower', 'B1-36', TRUE), (6, 'Middle', 'B1-37', TRUE), (6, 'Upper', 'B1-38', TRUE),
(6, 'Side Lower', 'B1-39', TRUE), (6, 'Side Upper', 'B1-40', TRUE),
(6, 'Lower', 'B1-41', TRUE), (6, 'Middle', 'B1-42', TRUE), (6, 'Upper', 'B1-43', TRUE),
(6, 'Lower', 'B1-44', TRUE), (6, 'Middle', 'B1-45', TRUE), (6, 'Upper', 'B1-46', TRUE),
(6, 'Side Lower', 'B1-47', TRUE), (6, 'Side Upper', 'B1-48', TRUE),
(6, 'Lower', 'B1-49', TRUE), (6, 'Middle', 'B1-50', TRUE), (6, 'Upper', 'B1-51', TRUE),
(6, 'Lower', 'B1-52', TRUE), (6, 'Middle', 'B1-53', TRUE), (6, 'Upper', 'B1-54', TRUE),
(6, 'Side Lower', 'B1-55', TRUE), (6, 'Side Upper', 'B1-56', TRUE),
(6, 'Lower', 'B1-57', TRUE), (6, 'Middle', 'B1-58', TRUE), (6, 'Upper', 'B1-59', TRUE),
(6, 'Lower', 'B1-60', TRUE), (6, 'Middle', 'B1-61', TRUE), (6, 'Upper', 'B1-62', TRUE),
(6, 'Side Lower', 'B1-63', TRUE), (6, 'Side Upper', 'B1-64', TRUE);

-- Insert sample bookings (some confirmed bookings to show seat occupancy)
INSERT INTO bookings (user_id, seat_id, train_id, route_id, passenger_name, passenger_age, status, pnr_number) VALUES
(2, 1, 1, 1, 'John Doe', 30, 'Confirmed', 'PNR001234567'),
(3, 5, 1, 1, 'Senior Citizen', 65, 'Confirmed', 'PNR001234568'),
(4, 9, 1, 1, 'Special Passenger', 40, 'Confirmed', 'PNR001234569');

-- Update seat availability for booked seats
UPDATE seats SET is_available = FALSE WHERE seat_id IN (1, 5, 9);

-- Insert sample payments
INSERT INTO payments (booking_id, amount, status, payment_method, transaction_id) VALUES
(1, 1500.00, 'Success', 'Credit Card', 'TXN001234567'),
(2, 1500.00, 'Success', 'Debit Card', 'TXN001234568'),
(3, 1500.00, 'Success', 'UPI', 'TXN001234569');

-- Insert sample waitlist entries
INSERT INTO waitlist (user_id, train_id, route_id, passenger_name, passenger_age, position) VALUES
(5, 1, 1, 'Waitlisted Passenger 1', 28, 1),
(2, 1, 1, 'Waitlisted Passenger 2', 35, 2);

-- Insert sample RAC entries
INSERT INTO rac (user_id, train_id, route_id, passenger_name, passenger_age, position) VALUES
(5, 1, 1, 'RAC Passenger 1', 25, 1),
(2, 1, 1, 'RAC Passenger 2', 32, 2);

-- Create a view for easy booking details retrieval
CREATE VIEW booking_details AS
SELECT 
    b.booking_id,
    b.pnr_number,
    u.username,
    u.role as user_role,
    t.train_name,
    t.train_number,
    r.source_station,
    r.destination_station,
    r.departure_time,
    r.arrival_time,
    r.price,
    b.passenger_name,
    b.passenger_age,
    s.seat_number,
    s.berth_type,
    comp.compartment_name,
    cl.class_type,
    b.booking_time,
    b.status as booking_status,
    p.amount as payment_amount,
    p.status as payment_status
FROM bookings b
JOIN users u ON b.user_id = u.user_id
JOIN trains t ON b.train_id = t.train_id
JOIN routes r ON b.route_id = r.route_id
LEFT JOIN seats s ON b.seat_id = s.seat_id
LEFT JOIN compartments comp ON s.compartment_id = comp.compartment_id
LEFT JOIN classes cl ON comp.class_id = cl.class_id
LEFT JOIN payments p ON b.booking_id = p.booking_id;

-- Create a view for seat availability
CREATE VIEW seat_availability AS
SELECT 
    s.seat_id,
    s.seat_number,
    s.berth_type,
    s.is_available,
    comp.compartment_name,
    cl.class_type,
    t.train_name,
    t.train_number,
    t.train_id
FROM seats s
JOIN compartments comp ON s.compartment_id = comp.compartment_id
JOIN classes cl ON comp.class_id = cl.class_id
JOIN trains t ON cl.train_id = t.train_id
ORDER BY t.train_name, cl.class_type, comp.compartment_name, s.seat_number;

-- Show database structure
SHOW TABLES;

-- Display sample data
SELECT 'Users Table' as Table_Name;
SELECT * FROM users;

SELECT 'Trains Table' as Table_Name;
SELECT * FROM trains;

SELECT 'Routes Table' as Table_Name;
SELECT * FROM routes;

SELECT 'Available Seats Summary' as Summary;
SELECT 
    t.train_name,
    cl.class_type,
    COUNT(*) as total_seats,
    SUM(CASE WHEN s.is_available = TRUE THEN 1 ELSE 0 END) as available_seats,
    SUM(CASE WHEN s.is_available = FALSE THEN 1 ELSE 0 END) as booked_seats
FROM trains t
JOIN classes cl ON t.train_id = cl.train_id
JOIN compartments comp ON cl.class_id = comp.class_id
JOIN seats s ON comp.compartment_id = s.compartment_id
GROUP BY t.train_name, cl.class_type
ORDER BY t.train_name, cl.class_type;
