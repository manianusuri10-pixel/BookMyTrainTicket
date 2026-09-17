package BookMyTrainTicket;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application class for BookMyTicket train booking system
 * Java Swing GUI implementation
 */
public class BookMyTicketApp {
    private JFrame mainFrame;
    private User currentUser;
    private LoginOperations loginOps;
    private TrainManager trainManager;
    private BookingManager bookingManager;
    private SeatAvailabilityManager seatManager;
    
    // GUI Components
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private CardLayout pageLayout;
    private JPanel pagePanel;
    private List<TrainManager.TrainSearchResult> searchResults;
    private TrainManager.TrainSearchResult selectedTrain;
    private SeatAvailabilityManager.CompartmentSeats selectedCompartment;
    private SeatAvailabilityManager.SeatWithDetails selectedSeat;
    private JPanel trainSelectionPanel;
    private JPanel compartmentSelectionPanel;
    private JPanel seatSelectionPanel;
    private JTextArea bookingsArea;
    
    public BookMyTicketApp() {
        try {
            // Initialize managers
            loginOps = new LoginOperations();
            trainManager = new TrainManager();
            bookingManager = new BookingManager();
            seatManager = new SeatAvailabilityManager();
            
            // Initialize GUI
            initializeGUI();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Database connection failed: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private void initializeGUI() {
        mainFrame = new JFrame("BookMyTicket - Train Booking System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);
        
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create card layout for different screens
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create different panels
        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createRegisterPanel(), "REGISTER");
        
        mainFrame.add(mainPanel);
        
        // Show login panel initially
        cardLayout.show(mainPanel, "LOGIN");
        
        mainFrame.setVisible(true);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("BookMyTicket");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 30, 0);
        panel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Train Booking System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(subtitleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.gridy = 2;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 10, 10, 0);
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(new JLabel("Password:"), gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 10, 10, 0);
        panel.add(passwordField, gbc);
        
        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.BLACK);
        loginButton.setPreferredSize(new Dimension(100, 35));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(loginButton, gbc);
        
        // Register button
        JButton registerButton = new JButton("New User? Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerButton.setForeground(new Color(25, 25, 112));
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 20, 0);
        panel.add(registerButton, gbc);
        
        // Login button action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter both username and password", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                User user = loginOps.authenticateUser(username, password);
                if (user != null) {
                    currentUser = user;
                    showMainDashboard();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid username or password", 
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Database error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Register button action
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));
        
        return panel;
    }
    
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("Register New User");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 30, 0);
        panel.add(titleLabel, gbc);
        
        // Form fields
        String[] labels = {"Username:", "Password:", "Email:", "User Type:"};
        JComponent[] fields = new JComponent[4];
        
        fields[0] = new JTextField(20);
        fields[1] = new JPasswordField(20);
        fields[2] = new JTextField(20);
        
        JComboBox<User.UserRole> roleCombo = new JComboBox<>(User.UserRole.values());
        roleCombo.setSelectedItem(User.UserRole.Regular);
        fields[3] = roleCombo;
        
        gbc.gridwidth = 1;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.insets = new Insets(10, 0, 10, 10);
            panel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            gbc.insets = new Insets(10, 10, 10, 0);
            panel.add(fields[i], gbc);
        }
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(34, 139, 34));
        registerButton.setForeground(Color.BLACK);
        
        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        gbc.gridx = 0;
        gbc.gridy = labels.length + 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        panel.add(buttonPanel, gbc);
        
        // Register button action
        registerButton.addActionListener(e -> {
            String username = ((JTextField) fields[0]).getText().trim();
            String password = new String(((JPasswordField) fields[1]).getPassword());
            String email = ((JTextField) fields[2]).getText().trim();
            User.UserRole role = (User.UserRole) roleCombo.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Username and password are required", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                boolean success = loginOps.registerUser(username, password, email, role);
                if (success) {
                    JOptionPane.showMessageDialog(mainFrame, "Registration successful! Please login.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "LOGIN");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Username already exists", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Database error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Back button action
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        
        return panel;
    }
    
    private void showMainDashboard() {
        // Remove existing components
        mainPanel.removeAll();
        
        // Create new dashboard
        JPanel dashboardPanel = createDashboardPanel();
        mainPanel.add(dashboardPanel, "DASHBOARD");
        
        cardLayout.show(mainPanel, "DASHBOARD");
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createHeaderPanel(), BorderLayout.NORTH);
        pageLayout = new CardLayout();
        pagePanel = new JPanel(pageLayout);
        pagePanel.add(createProfilePanel(), "PROFILE");
        pagePanel.add(createSearchTrainsPanel(), "SEARCH");
        trainSelectionPanel = createTrainSelectionPanel();
        compartmentSelectionPanel = createCompartmentSelectionPanel();
        seatSelectionPanel = createSeatSelectionPanel();
        pagePanel.add(trainSelectionPanel, "TRAINS");
        pagePanel.add(compartmentSelectionPanel, "COMPARTMENTS");
        pagePanel.add(seatSelectionPanel, "SEATS");
        pagePanel.add(createPassengerDetailsPanel(), "PASSENGER");
        pagePanel.add(createMyBookingsPanel(), "BOOKINGS");
        if (currentUser.isAdmin()) {
            pagePanel.add(createAdminPanel(), "ADMIN");
        }
        panel.add(createNavigationPanel(), BorderLayout.WEST);
        panel.add(pagePanel, BorderLayout.CENTER);
        pageLayout.show(pagePanel, "PROFILE");
        return panel;
    }

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        panel.setPreferredSize(new Dimension(170, 0));
        addPageButton(panel, "Profile", "PROFILE");
        addPageButton(panel, "Search Trains", "SEARCH");
        addPageButton(panel, "My Bookings", "BOOKINGS");
        if (currentUser.isAdmin()) {
            addPageButton(panel, "Admin Panel", "ADMIN");
        }
        return panel;
    }

    private void addPageButton(JPanel navigationPanel, String label, String pageName) {
        JButton button = new JButton(label);
        button.addActionListener(e -> pageLayout.show(pagePanel, pageName));
        navigationPanel.add(button);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);
        gbc.gridwidth = 1;
        addProfileRow(panel, gbc, 1, "Username:", currentUser.getUsername());
        addProfileRow(panel, gbc, 2, "Email:", currentUser.getEmail());
        addProfileRow(panel, gbc, 3, "Account type:", String.valueOf(currentUser.getRole()));
        return panel;
    }

    private void addProfileRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value == null || value.isBlank() ? "Not provided" : value), gbc);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 112));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.addActionListener(e -> logout());
        
        panel.add(welcomeLabel, BorderLayout.WEST);
        panel.add(logoutButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createSearchTrainsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search form
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JTextField sourceField = new JTextField(15);
        JTextField destField = new JTextField(15);
        JButton searchButton = new JButton("Search Trains");
        searchButton.setBackground(new Color(30, 144, 255));
        searchButton.setForeground(Color.BLACK);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("From:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(sourceField, gbc);
        gbc.gridx = 2;
        searchPanel.add(new JLabel("To:"), gbc);
        gbc.gridx = 3;
        searchPanel.add(destField, gbc);
        gbc.gridx = 4;
        searchPanel.add(searchButton, gbc);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Search button action
        searchButton.addActionListener(e -> {
            String source = sourceField.getText().trim();
            String destination = destField.getText().trim();
            
            if (source.isEmpty() || destination.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter both source and destination", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                searchResults = trainManager.searchTrains(source, destination);
                refreshTrainSelectionPanel();
                pageLayout.show(pagePanel, "TRAINS");
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error searching trains: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }

    private JPanel createTrainSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        refreshTrainSelectionPanel(panel);
        return panel;
    }

    private void refreshTrainSelectionPanel() {
        refreshTrainSelectionPanel(trainSelectionPanel);
    }

    private void refreshTrainSelectionPanel(JPanel panel) {
        if (panel == null) {
            return;
        }
        panel.removeAll();
        JLabel title = new JLabel("2. Select a Train");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        JPanel resultsPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        if (searchResults == null || searchResults.isEmpty()) {
            resultsPanel.add(new JLabel("No trains found for this route."));
        } else {
            for (TrainManager.TrainSearchResult result : searchResults) {
                JPanel resultPanel = new JPanel(new BorderLayout(10, 5));
                resultPanel.setBorder(BorderFactory.createTitledBorder(result.getTrain().getTrainName()));
                resultPanel.add(new JLabel(result.getTrain().getTrainNumber() + " | "
                    + result.getRoute().getSourceStation() + " to "
                    + result.getRoute().getDestinationStation() + " | Departure: "
                    + result.getRoute().getDepartureTime() + " | Seats: "
                    + result.getAvailableSeats()), BorderLayout.CENTER);
                JButton selectButton = new JButton("Select Train");
                selectButton.addActionListener(e -> {
                    selectedTrain = result;
                    refreshCompartmentSelectionPanel();
                    pageLayout.show(pagePanel, "COMPARTMENTS");
                });
                resultPanel.add(selectButton, BorderLayout.EAST);
                resultsPanel.add(resultPanel);
            }
        }
        panel.add(new JScrollPane(resultsPanel), BorderLayout.CENTER);
        JButton backButton = new JButton("Back to Search");
        backButton.addActionListener(e -> pageLayout.show(pagePanel, "SEARCH"));
        panel.add(backButton, BorderLayout.SOUTH);
        panel.revalidate();
        panel.repaint();
    }

    private JPanel createCompartmentSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private void refreshCompartmentSelectionPanel() {
        compartmentSelectionPanel.removeAll();
        JLabel title = new JLabel("3. Select a Compartment - " + selectedTrain.getTrain().getTrainName());
        title.setFont(new Font("Arial", Font.BOLD, 22));
        compartmentSelectionPanel.add(title, BorderLayout.NORTH);
        JPanel listPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        try {
            List<SeatAvailabilityManager.CompartmentSeats> compartments =
                seatManager.getSeatsGroupedByCompartment(selectedTrain.getTrain().getTrainId());
            for (SeatAvailabilityManager.CompartmentSeats compartment : compartments) {
                JButton compartmentButton = new JButton(compartment.toString());
                compartmentButton.setEnabled(compartment.getAvailableSeatsCount() > 0);
                compartmentButton.addActionListener(e -> {
                    selectedCompartment = compartment;
                    refreshSeatSelectionPanel();
                    pageLayout.show(pagePanel, "SEATS");
                });
                listPanel.add(compartmentButton);
            }
            if (compartments.isEmpty()) {
                listPanel.add(new JLabel("No compartments found for this train."));
            }
        } catch (SQLException ex) {
            listPanel.add(new JLabel("Unable to load compartments: " + ex.getMessage()));
        }
        compartmentSelectionPanel.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        JButton backButton = new JButton("Back to Trains");
        backButton.addActionListener(e -> pageLayout.show(pagePanel, "TRAINS"));
        compartmentSelectionPanel.add(backButton, BorderLayout.SOUTH);
        compartmentSelectionPanel.revalidate();
        compartmentSelectionPanel.repaint();
    }

    private JPanel createSeatSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private void refreshSeatSelectionPanel() {
        seatSelectionPanel.removeAll();
        JLabel title = new JLabel("4. Select a Seat - " + selectedCompartment.getCompartmentName());
        title.setFont(new Font("Arial", Font.BOLD, 22));
        seatSelectionPanel.add(title, BorderLayout.NORTH);
        JPanel seatsPanel = new JPanel(new GridLayout(0, 4, 8, 8));
        ButtonGroup seatGroup = new ButtonGroup();
        for (SeatAvailabilityManager.SeatWithDetails seat : selectedCompartment.getSeats()) {
            JRadioButton seatButton = new JRadioButton(seat.getSeatNumber() + " - " + seat.getBerthType());
            seatButton.setEnabled(seat.isAvailable());
            seatButton.putClientProperty("seat", seat);
            seatGroup.add(seatButton);
            seatsPanel.add(seatButton);
        }
        seatSelectionPanel.add(new JScrollPane(seatsPanel), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back to Compartments");
        backButton.addActionListener(e -> pageLayout.show(pagePanel, "COMPARTMENTS"));
        JButton continueButton = new JButton("Continue to Passenger Details");
        continueButton.addActionListener(e -> {
            selectedSeat = null;
            for (AbstractButton button : java.util.Collections.list(seatGroup.getElements())) {
                if (button.isSelected()) {
                    selectedSeat = (SeatAvailabilityManager.SeatWithDetails) button.getClientProperty("seat");
                    break;
                }
            }
            if (selectedSeat == null) {
                JOptionPane.showMessageDialog(mainFrame, "Please select an available seat", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            pageLayout.show(pagePanel, "PASSENGER");
        });
        buttons.add(backButton);
        buttons.add(continueButton);
        seatSelectionPanel.add(buttons, BorderLayout.SOUTH);
        seatSelectionPanel.revalidate();
        seatSelectionPanel.repaint();
    }

    private JPanel createPassengerDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("5. Passenger Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; panel.add(ageField, gbc);
        JButton backButton = new JButton("Back to Seats");
        backButton.addActionListener(e -> pageLayout.show(pagePanel, "SEATS"));
        JButton confirmButton = new JButton("Confirm and Pay");
        confirmButton.addActionListener(e -> createBooking(nameField, ageField));
        gbc.gridx = 0; gbc.gridy = 2; panel.add(backButton, gbc);
        gbc.gridx = 1; panel.add(confirmButton, gbc);
        return panel;
    }

    private void createBooking(JTextField nameField, JTextField ageField) {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        if (name.isEmpty() || ageText.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0 || age > 120) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a valid age", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BookingManager.BookingResult result = bookingManager.createBooking(
                currentUser.getUserId(), selectedSeat.getSeatId(), selectedTrain.getTrain().getTrainId(),
                selectedTrain.getRoute().getRouteId(), name, age);
            if (!result.isSuccess()) {
                JOptionPane.showMessageDialog(mainFrame, "Booking failed: " + result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            PaymentDialog paymentDialog = new PaymentDialog(mainFrame, result.getId(), selectedTrain.getRoute().getPrice());
            paymentDialog.setVisible(true);
            refreshMyBookings();
            if (paymentDialog.isPaymentSuccessful()) {
                JOptionPane.showMessageDialog(mainFrame, "Booking and payment successful! Booking ID: " + result.getId());
                pageLayout.show(pagePanel, "BOOKINGS");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Payment was not completed. Booking has been cancelled.", "Payment Cancelled", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Please enter a valid age", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showSeatSelectionDialog(List<TrainManager.TrainSearchResult> searchResults) {
        if (searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No trains available", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Let user select a train first
        TrainManager.TrainSearchResult[] options = searchResults.toArray(new TrainManager.TrainSearchResult[0]);
        TrainManager.TrainSearchResult selectedTrain = (TrainManager.TrainSearchResult) JOptionPane.showInputDialog(
            mainFrame,
            "Select a train:",
            "Train Selection",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (selectedTrain == null) return;
        
        // Show seat selection dialog
        showSeatMapDialog(selectedTrain);
    }
    
    private void showSeatMapDialog(TrainManager.TrainSearchResult trainResult) {
        JDialog seatDialog = new JDialog(mainFrame, "Select Seat - " + trainResult.getTrain().getTrainName(), true);
        seatDialog.setSize(800, 600);
        seatDialog.setLocationRelativeTo(mainFrame);
        
        try {
            List<SeatAvailabilityManager.SeatWithDetails> seats = seatManager.getAvailableSeats(
                trainResult.getTrain().getTrainId(), trainResult.getRoute().getRouteId());
            
            // Get recommended seats for user type
            List<SeatAvailabilityManager.SeatWithDetails> recommendedSeats = 
                seatManager.getRecommendedSeats(trainResult.getTrain().getTrainId(), currentUser.getRole());
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            
            // Info panel
            JPanel infoPanel = new JPanel();
            infoPanel.add(new JLabel("Recommended seats for " + currentUser.getRole() + " users are highlighted"));
            mainPanel.add(infoPanel, BorderLayout.NORTH);
            
            // Seat selection area
            JPanel seatPanel = new JPanel(new GridLayout(0, 6, 5, 5));
            seatPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            ButtonGroup seatGroup = new ButtonGroup();
            
            for (SeatAvailabilityManager.SeatWithDetails seat : seats) {
                JRadioButton seatButton = new JRadioButton(seat.getSeatNumber() + " (" + seat.getBerthType() + ")");
                seatButton.putClientProperty("seat", seat);
                
                // Highlight recommended seats
                if (recommendedSeats.contains(seat)) {
                    seatButton.setBackground(new Color(144, 238, 144));
                    seatButton.setOpaque(true);
                }
                
                seatGroup.add(seatButton);
                seatPanel.add(seatButton);
            }
            
            JScrollPane seatScrollPane = new JScrollPane(seatPanel);
            mainPanel.add(seatScrollPane, BorderLayout.CENTER);
            
            // Book button
            JPanel buttonPanel = new JPanel();
            JButton bookButton = new JButton("Book Selected Seat");
            bookButton.setBackground(new Color(34, 139, 34));
            bookButton.setForeground(Color.BLACK);
            
            bookButton.addActionListener(e -> {
                // Find selected seat
                SeatAvailabilityManager.SeatWithDetails selectedSeat = null;
                for (AbstractButton button : java.util.Collections.list(seatGroup.getElements())) {
                    if (button.isSelected()) {
                        selectedSeat = (SeatAvailabilityManager.SeatWithDetails) button.getClientProperty("seat");
                        break;
                    }
                }
                
                if (selectedSeat == null) {
                    JOptionPane.showMessageDialog(seatDialog, "Please select a seat", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Show passenger details dialog
                showPassengerDetailsDialog(trainResult, selectedSeat, seatDialog);
            });
            
            buttonPanel.add(bookButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            seatDialog.add(mainPanel);
            seatDialog.setVisible(true);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error loading seats: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showPassengerDetailsDialog(TrainManager.TrainSearchResult trainResult, 
                                          SeatAvailabilityManager.SeatWithDetails seat, JDialog parentDialog) {
        JDialog passengerDialog = new JDialog(mainFrame, "Passenger Details", true);
        passengerDialog.setSize(400, 300);
        passengerDialog.setLocationRelativeTo(parentDialog);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Passenger Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        panel.add(ageField, gbc);
        
        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.setBackground(new Color(34, 139, 34));
        confirmButton.setForeground(Color.BLACK);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(confirmButton, gbc);
        
        confirmButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            
            if (name.isEmpty() || ageText.isEmpty()) {
                JOptionPane.showMessageDialog(passengerDialog, "Please fill all fields", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int age = Integer.parseInt(ageText);
                if (age <= 0 || age > 120) {
                    JOptionPane.showMessageDialog(passengerDialog, "Please enter a valid age", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create booking
                BookingManager.BookingResult result = bookingManager.createBooking(
                    currentUser.getUserId(),
                    seat.getSeatId(),
                    trainResult.getTrain().getTrainId(),
                    trainResult.getRoute().getRouteId(),
                    name,
                    age
                );

                if (result.isSuccess()) {
                    // Show payment dialog
                    BigDecimal bookingAmount = trainResult.getRoute().getPrice();
                    PaymentDialog paymentDialog = new PaymentDialog(mainFrame, result.getId(), bookingAmount);
                    paymentDialog.setVisible(true);
                    
                    passengerDialog.dispose();
                    parentDialog.dispose();
                    
                    // Refresh my bookings tab
                    refreshMyBookings();
                     if (paymentDialog.isPaymentSuccessful()) {
                        JOptionPane.showMessageDialog(passengerDialog, 
                            "Booking and payment successful!\nBooking ID: " + result.getId(), 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                        passengerDialog.dispose();
                        // parentDialog.dispose();
                        
                        // // Refresh my bookings tab
                        // refreshMyBookings();
                    } else {
                        JOptionPane.showMessageDialog(passengerDialog, 
                            "Payment was not completed. Booking has been cancelled.", 
                            "Payment Cancelled", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(passengerDialog, 
                        "Booking failed: " + result.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(passengerDialog, "Please enter a valid age", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(passengerDialog, "Database error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        passengerDialog.add(panel);
        passengerDialog.setVisible(true);
    }
    
    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshMyBookings());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(refreshButton);
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Bookings display area
        bookingsArea = new JTextArea(25, 70);
        bookingsArea.setEditable(false);
        bookingsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(bookingsArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Load initial bookings
        refreshMyBookings();
        
        return panel;
    }
    
    private void refreshMyBookings() {
        try {
            List<BookingManager.BookingDetails> bookings = bookingManager.getBookingsForUser(currentUser.getUserId());
            
            StringBuilder sb = new StringBuilder();
            sb.append("My Bookings\n");
            sb.append("=".repeat(100)).append("\n\n");
            
            if (bookings.isEmpty()) {
                sb.append("No bookings found.\n");
            } else {
                for (BookingManager.BookingDetails booking : bookings) {
                    sb.append("Booking ID: ").append(booking.getBookingId()).append("\n");
                    sb.append("Train: ").append(booking.getTrainName()).append(" (").append(booking.getTrainNumber()).append(")\n");
                    sb.append("Route: ").append(booking.getSourceStation()).append(" → ").append(booking.getDestinationStation()).append("\n");
                    sb.append("Passenger: ").append(booking.getPassengerName()).append(" (Age: ").append(booking.getPassengerAge()).append(")\n");
                    sb.append("Seat: ").append(booking.getSeatNumber() != null ? booking.getSeatNumber() : "N/A");
                    if (booking.getBerthType() != null) {
                        sb.append(" (").append(booking.getBerthType()).append(")");
                    }
                    sb.append("\n");
                    sb.append("Status: ").append(booking.getStatus()).append("\n");
                    sb.append("Booking Time: ").append(booking.getBookingTime()).append("\n");
                    sb.append("Price: ₹").append(booking.getPrice()).append("\n");
                    sb.append("-".repeat(80)).append("\n\n");
                }
            }
            
            if (bookingsArea != null) {
                bookingsArea.setText(sb.toString());
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error loading bookings: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Admin Panel - Train and User Management"));
        // Add admin functionality here
        return panel;
    }
    
    private void logout() {
        currentUser = null;
        mainPanel.removeAll();
        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createRegisterPanel(), "REGISTER");
        cardLayout.show(mainPanel, "LOGIN");
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookMyTicketApp();
        });
    }
}
