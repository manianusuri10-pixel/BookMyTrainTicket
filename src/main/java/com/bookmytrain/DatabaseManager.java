package com.bookmytrain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class DatabaseManager {

    // =========================================================
    // AIVEN MYSQL CONNECTION
    // =========================================================

    private static final String DB_HOST =
            getEnv("AIVEN_DB_HOST",
                    "bookmytrain-mysql-manianusuri10-9c3e.i.aivencloud.com");

    private static final String DB_PORT =
            getEnv("AIVEN_DB_PORT", "13572");

    private static final String DB_NAME =
            getEnv("AIVEN_DB_NAME", "defaultdb");

    private static final String DB_USER =
            getEnv("AIVEN_DB_USER", "avnadmin");

    private static final String DB_PASSWORD =
            getRequiredEnv("AIVEN_DB_PASSWORD");

    /*
     * Aiven requires SSL.
     */
    private static final String DB_URL =
            "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
                    + "?sslMode=REQUIRED"
                    + "&serverTimezone=UTC"
                    + "&allowPublicKeyRetrieval=true";

    private static DatabaseManager instance;

    private final Connection connection;


    // =========================================================
    // ENVIRONMENT VARIABLE HELPERS
    // =========================================================

    private static String getEnv(String name, String defaultValue) {

        String value = System.getenv(name);

        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        return value;
    }


    private static String getRequiredEnv(String name) {

        String value = System.getenv(name);

        if (value == null || value.trim().isEmpty()) {

            throw new IllegalStateException(
                    "Missing environment variable: " + name
            );
        }

        return value;
    }


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    private DatabaseManager() throws SQLException {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                    "MySQL JDBC driver not found. "
                    + "Add mysql-connector-j dependency.",
                    e
            );
        }


        System.out.println("----------------------------------------");
        System.out.println("Connecting to Aiven MySQL");
        System.out.println("----------------------------------------");

        System.out.println("Host     : " + DB_HOST);
        System.out.println("Port     : " + DB_PORT);
        System.out.println("Database : " + DB_NAME);
        System.out.println("User     : " + DB_USER);
        System.out.println("SSL      : REQUIRED");


        Properties props = new Properties();

        props.setProperty("user", DB_USER);
        props.setProperty("password", DB_PASSWORD);


        connection = DriverManager.getConnection(
                DB_URL,
                props
        );


        System.out.println(
                "Aiven MySQL connection successful!"
        );

        System.out.println("----------------------------------------");


        initializeDatabase();
    }


    // =========================================================
    // SINGLETON
    // =========================================================

    public static synchronized DatabaseManager getInstance()
            throws SQLException {

        if (instance == null
                || instance.connection.isClosed()) {

            instance = new DatabaseManager();
        }

        return instance;
    }


    // =========================================================
    // GET CONNECTION
    // =========================================================

    public static Connection getConnection()
            throws SQLException {

        return getInstance().connection;
    }


    // =========================================================
    // INITIALIZE DATABASE
    // =========================================================

    private void initializeDatabase()
            throws SQLException {

        createTables();

        insertSampleData();
    }


    // =========================================================
    // CREATE TABLES
    // =========================================================

    private void createTables()
            throws SQLException {

        String[] q = {

                // USERS
                "CREATE TABLE IF NOT EXISTS users (" +
                        "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "username VARCHAR(50) NOT NULL UNIQUE, " +
                        "password VARCHAR(100) NOT NULL, " +
                        "email VARCHAR(100), " +
                        "role ENUM(" +
                        "'Admin'," +
                        "'Regular'," +
                        "'Senior'," +
                        "'DifferentlyAbled'" +
                        ") NOT NULL" +
                        ")",


                // TRAINS
                "CREATE TABLE IF NOT EXISTS trains (" +
                        "train_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "train_name VARCHAR(100) NOT NULL, " +
                        "train_number VARCHAR(20) NOT NULL UNIQUE" +
                        ")",


                // ROUTES
                "CREATE TABLE IF NOT EXISTS routes (" +
                        "route_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "train_id INT NOT NULL, " +
                        "source_station VARCHAR(100) NOT NULL, " +
                        "destination_station VARCHAR(100) NOT NULL, " +
                        "departure_time TIME NOT NULL, " +
                        "arrival_time TIME NOT NULL, " +
                        "price DECIMAL(10,2) NOT NULL, " +
                        "FOREIGN KEY (train_id) " +
                        "REFERENCES trains(train_id) " +
                        "ON DELETE CASCADE" +
                        ")",


                // CLASSES
                "CREATE TABLE IF NOT EXISTS classes (" +
                        "class_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "train_id INT NOT NULL, " +
                        "class_type VARCHAR(50) NOT NULL, " +
                        "FOREIGN KEY (train_id) " +
                        "REFERENCES trains(train_id) " +
                        "ON DELETE CASCADE" +
                        ")",


                // COMPARTMENTS
                "CREATE TABLE IF NOT EXISTS compartments (" +
                        "compartment_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "class_id INT NOT NULL, " +
                        "compartment_name VARCHAR(50) NOT NULL, " +
                        "FOREIGN KEY (class_id) " +
                        "REFERENCES classes(class_id) " +
                        "ON DELETE CASCADE" +
                        ")",


                // SEATS
                "CREATE TABLE IF NOT EXISTS seats (" +
                        "seat_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "compartment_id INT NOT NULL, " +
                        "berth_type ENUM(" +
                        "'Lower'," +
                        "'Middle'," +
                        "'Upper'," +
                        "'Side Lower'," +
                        "'Side Upper'" +
                        ") NOT NULL, " +
                        "seat_number VARCHAR(20) NOT NULL, " +
                        "is_available BOOLEAN NOT NULL DEFAULT TRUE, " +
                        "FOREIGN KEY (compartment_id) " +
                        "REFERENCES compartments(compartment_id) " +
                        "ON DELETE CASCADE, " +
                        "UNIQUE KEY uq_seat " +
                        "(compartment_id, seat_number)" +
                        ")",


                // BOOKINGS
                "CREATE TABLE IF NOT EXISTS bookings (" +
                        "booking_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id INT NOT NULL, " +
                        "seat_id INT NULL, " +
                        "train_id INT NOT NULL, " +
                        "route_id INT NOT NULL, " +
                        "passenger_name VARCHAR(100) NOT NULL, " +
                        "passenger_age INT NOT NULL, " +
                        "pnr_number VARCHAR(20) UNIQUE, " +
                        "booking_time DATETIME " +
                        "DEFAULT CURRENT_TIMESTAMP, " +
                        "status ENUM(" +
                        "'Confirmed'," +
                        "'Cancelled'," +
                        "'RAC'," +
                        "'Waiting'" +
                        ") DEFAULT 'Confirmed', " +
                        "FOREIGN KEY (user_id) " +
                        "REFERENCES users(user_id), " +
                        "FOREIGN KEY (seat_id) " +
                        "REFERENCES seats(seat_id), " +
                        "FOREIGN KEY (train_id) " +
                        "REFERENCES trains(train_id), " +
                        "FOREIGN KEY (route_id) " +
                        "REFERENCES routes(route_id)" +
                        ")",


                // PAYMENTS
                "CREATE TABLE IF NOT EXISTS payments (" +
                        "payment_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "booking_id INT NOT NULL, " +
                        "amount DECIMAL(10,2) NOT NULL, " +
                        "payment_method VARCHAR(30), " +
                        "transaction_id VARCHAR(100), " +
                        "status ENUM(" +
                        "'Success'," +
                        "'Failed'," +
                        "'Pending'" +
                        ") NOT NULL, " +
                        "payment_time DATETIME " +
                        "DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (booking_id) " +
                        "REFERENCES bookings(booking_id)" +
                        ")",


                // WAITLIST
                "CREATE TABLE IF NOT EXISTS waitlist (" +
                        "waitlist_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id INT NOT NULL, " +
                        "train_id INT NOT NULL, " +
                        "route_id INT NOT NULL, " +
                        "request_time DATETIME " +
                        "DEFAULT CURRENT_TIMESTAMP, " +
                        "status ENUM(" +
                        "'Waiting'," +
                        "'Promoted'" +
                        ") DEFAULT 'Waiting', " +
                        "position INT NOT NULL, " +
                        "FOREIGN KEY (user_id) " +
                        "REFERENCES users(user_id), " +
                        "FOREIGN KEY (train_id) " +
                        "REFERENCES trains(train_id), " +
                        "FOREIGN KEY (route_id) " +
                        "REFERENCES routes(route_id)" +
                        ")",


                // RAC
                "CREATE TABLE IF NOT EXISTS rac (" +
                        "rac_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "user_id INT NOT NULL, " +
                        "train_id INT NOT NULL, " +
                        "route_id INT NOT NULL, " +
                        "request_time DATETIME " +
                        "DEFAULT CURRENT_TIMESTAMP, " +
                        "status ENUM(" +
                        "'RAC'," +
                        "'Promoted'" +
                        ") DEFAULT 'RAC', " +
                        "position INT NOT NULL, " +
                        "FOREIGN KEY (user_id) " +
                        "REFERENCES users(user_id), " +
                        "FOREIGN KEY (train_id) " +
                        "REFERENCES trains(train_id), " +
                        "FOREIGN KEY (route_id) " +
                        "REFERENCES routes(route_id)" +
                        ")"
        };


        try (Statement st =
                     connection.createStatement()) {

            for (String sql : q) {

                st.executeUpdate(sql);
            }
        }
    }


    // =========================================================
    // SAMPLE DATA
    // =========================================================

    private void insertSampleData()
            throws SQLException {

        insertUser(
                "admin",
                "admin123",
                "admin@train.com",
                "Admin"
        );


        insertUser(
                "john_doe",
                "password123",
                "john@email.com",
                "Regular"
        );


        insertUser(
                "senior_user",
                "senior123",
                "senior@email.com",
                "Senior"
        );


        int t1 = insertTrain(
                "Rajdhani Express",
                "12301"
        );


        int t2 = insertTrain(
                "Shatabdi Express",
                "12002"
        );


        int t3 = insertTrain(
                "Duronto Express",
                "12259"
        );


        insertRoute(
                t1,
                "New Delhi",
                "Mumbai Central",
                "16:55:00",
                "08:35:00",
                1500
        );


        insertRoute(
                t2,
                "New Delhi",
                "Chandigarh",
                "17:20:00",
                "21:00:00",
                800
        );


        insertRoute(
                t3,
                "Mumbai Central",
                "Pune",
                "06:00:00",
                "09:30:00",
                600
        );


        int c1 = insertClass(
                t1,
                "AC 2 Tier"
        );


        int c2 = insertClass(
                t1,
                "AC 3 Tier"
        );


        int c3 = insertClass(
                t2,
                "AC Chair Car"
        );


        int a1 = insertCompartment(
                c1,
                "A1"
        );


        int a2 = insertCompartment(
                c1,
                "A2"
        );


        int b1 = insertCompartment(
                c2,
                "B1"
        );


        int cc1 = insertCompartment(
                c3,
                "CC1"
        );


        generateSeats(a1);
        generateSeats(a2);
        generateSeats(b1);
        generateSeats(cc1);
    }


    // =========================================================
    // INSERT USER
    // =========================================================

    private void insertUser(
            String u,
            String p,
            String e,
            String r
    ) throws SQLException {

        String sql =
                "INSERT IGNORE INTO users(" +
                        "username,password,email,role" +
                        ") VALUES(?,?,?,?)";


        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, u);
            ps.setString(2, p);
            ps.setString(3, e);
            ps.setString(4, r);

            ps.executeUpdate();
        }
    }


    // =========================================================
    // INSERT TRAIN
    // =========================================================

    private int insertTrain(
            String n,
            String no
    ) throws SQLException {

        String sql =
                "INSERT INTO trains(" +
                        "train_name,train_number" +
                        ") VALUES(?,?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "train_id=LAST_INSERT_ID(train_id)";


        try (PreparedStatement ps =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setString(1, n);
            ps.setString(2, no);

            ps.executeUpdate();


            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    return rs.getInt(1);
                }
            }
        }


        return 0;
    }


    // =========================================================
    // INSERT ROUTE
    // =========================================================

    private void insertRoute(
            int t,
            String s,
            String d,
            String dep,
            String arr,
            double price
    ) throws SQLException {

        String sql =
                "INSERT IGNORE INTO routes(" +
                        "train_id," +
                        "source_station," +
                        "destination_station," +
                        "departure_time," +
                        "arrival_time," +
                        "price" +
                        ") VALUES(?,?,?,?,?,?)";


        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, t);
            ps.setString(2, s);
            ps.setString(3, d);
            ps.setString(4, dep);
            ps.setString(5, arr);
            ps.setDouble(6, price);

            ps.executeUpdate();
        }
    }


    // =========================================================
    // INSERT CLASS
    // =========================================================

    private int insertClass(
            int t,
            String type
    ) throws SQLException {

        String select =
                "SELECT class_id FROM classes " +
                        "WHERE train_id=? AND class_type=?";


        try (PreparedStatement ps =
                     connection.prepareStatement(select)) {

            ps.setInt(1, t);
            ps.setString(2, type);


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1);
                }
            }
        }


        String insert =
                "INSERT INTO classes(" +
                        "train_id,class_type" +
                        ") VALUES(?,?)";


        try (PreparedStatement ps =
                     connection.prepareStatement(
                             insert,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setInt(1, t);
            ps.setString(2, type);

            ps.executeUpdate();


            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    return rs.getInt(1);
                }
            }
        }


        throw new SQLException(
                "Could not create class"
        );
    }


    // =========================================================
    // INSERT COMPARTMENT
    // =========================================================

    private int insertCompartment(
            int c,
            String name
    ) throws SQLException {

        String select =
                "SELECT compartment_id " +
                        "FROM compartments " +
                        "WHERE class_id=? " +
                        "AND compartment_name=?";


        try (PreparedStatement ps =
                     connection.prepareStatement(select)) {

            ps.setInt(1, c);
            ps.setString(2, name);


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1);
                }
            }
        }


        String insert =
                "INSERT INTO compartments(" +
                        "class_id,compartment_name" +
                        ") VALUES(?,?)";


        try (PreparedStatement ps =
                     connection.prepareStatement(
                             insert,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setInt(1, c);
            ps.setString(2, name);

            ps.executeUpdate();


            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    return rs.getInt(1);
                }
            }
        }


        throw new SQLException(
                "Could not create compartment"
        );
    }


    // =========================================================
    // GENERATE SEATS
    // =========================================================

    private void generateSeats(
            int compartmentId
    ) throws SQLException {

        String[] types = {

                "Lower",
                "Middle",
                "Upper",
                "Side Lower",
                "Side Upper"
        };


        String checkSql =
                "SELECT COUNT(*) " +
                        "FROM seats " +
                        "WHERE compartment_id=?";


        try (PreparedStatement check =
                     connection.prepareStatement(checkSql)) {

            check.setInt(1, compartmentId);


            try (ResultSet rs =
                         check.executeQuery()) {

                if (rs.next()
                        && rs.getInt(1) > 0) {

                    return;
                }
            }
        }


        String sql =
                "INSERT INTO seats(" +
                        "compartment_id," +
                        "berth_type," +
                        "seat_number," +
                        "is_available" +
                        ") VALUES(?,?,?,TRUE)";


        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            int n = 1;


            for (String type : types) {

                for (int i = 0; i < 4; i++) {

                    ps.setInt(
                            1,
                            compartmentId
                    );

                    ps.setString(
                            2,
                            type
                    );

                    ps.setString(
                            3,
                            String.valueOf(n++)
                    );

                    ps.addBatch();
                }
            }


            ps.executeBatch();
        }
    }


    // =========================================================
    // CLOSE
    // =========================================================

    public void close()
            throws SQLException {

        if (!connection.isClosed()) {

            connection.close();
        }
    }
}