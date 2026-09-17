package BookMyTrainTicket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * PaymentDialog provides comprehensive payment UI with method selection,
 * payment processing, and receipt generation
 */
public class PaymentDialog extends JDialog {
    
    private PaymentManager paymentManager;
    private int bookingId;
    private BigDecimal amount;
    private JFrame parent;
    private boolean paymentSuccessful = false;
    
    // UI Components
    private JComboBox<PaymentManager.PaymentMethod> methodCombo;
    private JPanel paymentDetailsPanel;
    private CardLayout paymentCardLayout;
    
    // Card payment fields
    private JTextField cardNumberField;
    private JTextField cardHolderField;
    private JTextField expiryDateField;
    private JTextField cvvField;
    
    // UPI payment fields
    private JTextField upiIdField;
    
    // Other fields
    private JLabel amountLabel;
    private JButton payButton;
    private JButton cancelButton;
    
    public PaymentDialog(JFrame parent, int bookingId, BigDecimal bookingAmount) {
        super(parent, "Payment Gateway - BookMyTicket", true);
        this.parent = parent;
        this.bookingId = bookingId;
        this.amount = bookingAmount;
        
        try {
            this.paymentManager = new PaymentManager();
            initializeUI();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Payment system error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
    
    private void initializeUI() {
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Payment form
        JPanel formPanel = createPaymentForm();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Set initial payment method
        updatePaymentForm();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(25, 25, 112));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel titleLabel = new JLabel("Secure Payment Gateway");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        
        amountLabel = new JLabel("Amount: ₹" + String.format("%.2f", amount));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountLabel.setForeground(Color.YELLOW);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(amountLabel, gbc);
        
        return panel;
    }
    
    private JPanel createPaymentForm() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Payment Details"));
        
        // Payment method selection
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        methodPanel.add(new JLabel("Payment Method:"));
        
        methodCombo = new JComboBox<>(PaymentManager.PaymentMethod.values());
        methodCombo.addActionListener(e -> updatePaymentForm());
        methodPanel.add(methodCombo);
        
        panel.add(methodPanel, BorderLayout.NORTH);
        
        // Payment details panel with CardLayout
        paymentCardLayout = new CardLayout();
        paymentDetailsPanel = new JPanel(paymentCardLayout);
        
        // Create different payment forms
        paymentDetailsPanel.add(createCardPaymentPanel(), "CARD");
        paymentDetailsPanel.add(createUPIPaymentPanel(), "UPI");
        paymentDetailsPanel.add(createNetBankingPanel(), "NET_BANKING");
        paymentDetailsPanel.add(createWalletPanel(), "WALLET");
        
        panel.add(paymentDetailsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCardPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Card Number
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Card Number:"), gbc);
        cardNumberField = new JTextField(20);
        cardNumberField.setToolTipText("Enter 16-digit card number");
        gbc.gridx = 1;
        panel.add(cardNumberField, gbc);
        
        // Card Holder Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Card Holder Name:"), gbc);
        cardHolderField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(cardHolderField, gbc);
        
        // Expiry Date
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Expiry Date (MM/YY):"), gbc);
        expiryDateField = new JTextField(10);
        expiryDateField.setToolTipText("Format: MM/YY");
        gbc.gridx = 1;
        panel.add(expiryDateField, gbc);
        
        // CVV
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("CVV:"), gbc);
        cvvField = new JTextField(5);
        cvvField.setToolTipText("3-digit security code");
        gbc.gridx = 1;
        panel.add(cvvField, gbc);
        
        // Security note
        JLabel securityLabel = new JLabel("<html><i>🔒 Your card details are encrypted and secure</i></html>");
        securityLabel.setForeground(new Color(0, 128, 0));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(securityLabel, gbc);
        
        return panel;
    }
    
    private JPanel createUPIPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // UPI ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("UPI ID:"), gbc);
        upiIdField = new JTextField(20);
        upiIdField.setToolTipText("Enter your UPI ID (e.g., user@paytm)");
        gbc.gridx = 1;
        panel.add(upiIdField, gbc);
        
        // UPI info
        JLabel upiInfo = new JLabel("<html><i>Enter your UPI ID to proceed with payment</i></html>");
        upiInfo.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(upiInfo, gbc);
        
        return panel;
    }
    
    private JPanel createNetBankingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel infoLabel = new JLabel("<html><center>You will be redirected to your bank's<br>secure login page to complete the payment</center></html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(infoLabel, gbc);
        
        return panel;
    }
    
    private JPanel createWalletPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel infoLabel = new JLabel("<html><center>Payment will be processed through<br>your selected wallet application</center></html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(infoLabel, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        payButton = new JButton("Pay ₹" + String.format("%.2f", amount));
        payButton.setFont(new Font("Arial", Font.BOLD, 14));
        payButton.setBackground(new Color(34, 139, 34));
        payButton.setForeground(Color.WHITE);
        payButton.setPreferredSize(new Dimension(150, 40));
        payButton.addActionListener(this::processPayment);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> dispose());
        
        panel.add(payButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    private void updatePaymentForm() {
        PaymentManager.PaymentMethod method = (PaymentManager.PaymentMethod) methodCombo.getSelectedItem();
        
        switch (method) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                paymentCardLayout.show(paymentDetailsPanel, "CARD");
                break;
            case UPI:
                paymentCardLayout.show(paymentDetailsPanel, "UPI");
                break;
            case NET_BANKING:
                paymentCardLayout.show(paymentDetailsPanel, "NET_BANKING");
                break;
            case WALLET:
                paymentCardLayout.show(paymentDetailsPanel, "WALLET");
                break;
        }
    }
    
    private void processPayment(ActionEvent e) {
        PaymentManager.PaymentMethod method = (PaymentManager.PaymentMethod) methodCombo.getSelectedItem();
        
        // Validate input based on payment method
        if (!validatePaymentInput(method)) {
            return;
        }
        
        // Create payment request
        PaymentManager.PaymentRequest request = new PaymentManager.PaymentRequest(bookingId, amount, method);
        
        // Set method-specific details
        if (method == PaymentManager.PaymentMethod.CREDIT_CARD || 
            method == PaymentManager.PaymentMethod.DEBIT_CARD) {
            request.setCardDetails(
                cardNumberField.getText().trim(),
                cardHolderField.getText().trim(),
                expiryDateField.getText().trim(),
                cvvField.getText().trim()
            );
        } else if (method == PaymentManager.PaymentMethod.UPI) {
            request.setUpiId(upiIdField.getText().trim());
        }
        
        // Show processing dialog
        showProcessingDialog(request);
    }
    
    private boolean validatePaymentInput(PaymentManager.PaymentMethod method) {
        switch (method) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                if (cardNumberField.getText().trim().length() < 16) {
                    showError("Please enter a valid 16-digit card number");
                    return false;
                }
                if (cardHolderField.getText().trim().isEmpty()) {
                    showError("Please enter card holder name");
                    return false;
                }
                if (expiryDateField.getText().trim().length() != 5) {
                    showError("Please enter expiry date in MM/YY format");
                    return false;
                }
                if (cvvField.getText().trim().length() != 3) {
                    showError("Please enter 3-digit CVV");
                    return false;
                }
                break;
                
            case UPI:
                if (!upiIdField.getText().trim().contains("@")) {
                    showError("Please enter a valid UPI ID");
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }
    
    private void showProcessingDialog(PaymentManager.PaymentRequest request) {
        // Create processing dialog
        JDialog processingDialog = new JDialog(this, "Processing Payment", true);
        processingDialog.setSize(350, 200);
        processingDialog.setLocationRelativeTo(this);
        processingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel messageLabel = new JLabel("Processing your payment...", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        
        processingDialog.add(panel);
        
        // Process payment in background thread
        SwingWorker<PaymentManager.PaymentResult, Void> worker = new SwingWorker<PaymentManager.PaymentResult, Void>() {
            @Override
            protected PaymentManager.PaymentResult doInBackground() throws Exception {
                return paymentManager.processPayment(request);
            }
            
            @Override
            protected void done() {
                processingDialog.dispose();
                try {
                    PaymentManager.PaymentResult result = get();
                    handlePaymentResult(result);
                } catch (Exception ex) {
                    showError("Payment processing error: " + ex.getMessage());
                }
            }
        };
        
        worker.execute();
        processingDialog.setVisible(true);
    }
    
    private void handlePaymentResult(PaymentManager.PaymentResult result) {
        if (result.isSuccess()) {
            paymentSuccessful = true;
            showSuccessDialog(result);
        } else {
            showFailureDialog(result);
        }
    }
    
    private void showSuccessDialog(PaymentManager.PaymentResult result) {
        try {
            // Generate receipt
            PaymentManager.PaymentReceipt receipt = paymentManager.generateReceipt(result.getPaymentId());
            
            // Create success dialog
            JDialog successDialog = new JDialog(this, "Payment Successful", true);
            successDialog.setSize(600, 500);
            successDialog.setLocationRelativeTo(this);
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            
            // Success header
            JPanel headerPanel = new JPanel();
            headerPanel.setBackground(new Color(34, 139, 34));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JLabel successLabel = new JLabel("✓ Payment Successful!");
            successLabel.setFont(new Font("Arial", Font.BOLD, 20));
            successLabel.setForeground(Color.WHITE);
            headerPanel.add(successLabel);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Receipt details
            JTextArea receiptArea = new JTextArea(paymentManager.generatePrintableReceipt(receipt));
            receiptArea.setEditable(false);
            receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(receiptArea);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            
            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout());
            
            JButton printButton = new JButton("Print Receipt");
            printButton.addActionListener(e -> printReceipt(receiptArea.getText()));
            
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> {
                successDialog.dispose();
                dispose();
            });
            
            buttonPanel.add(printButton);
            buttonPanel.add(closeButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            successDialog.add(mainPanel);
            successDialog.setVisible(true);
            
        } catch (SQLException e) {
            showError("Error generating receipt: " + e.getMessage());
        }
    }
    
    private void showFailureDialog(PaymentManager.PaymentResult result) {
        JDialog failureDialog = new JDialog(this, "Payment Failed", true);
        failureDialog.setSize(400, 300);
        failureDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Failure header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(220, 20, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel failureLabel = new JLabel("✗ Payment Failed");
        failureLabel.setFont(new Font("Arial", Font.BOLD, 20));
        failureLabel.setForeground(Color.WHITE);
        headerPanel.add(failureLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Failure message
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("<html><center>" + result.getMessage() + 
            "<br><br>Your booking has been cancelled and the seat is now available for others.</center></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton retryButton = new JButton("Retry Payment");
        retryButton.addActionListener(e -> {
            failureDialog.dispose();
            // Reset form for retry
            clearForm();
        });
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            failureDialog.dispose();
            dispose();
        });
        
        buttonPanel.add(retryButton);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        failureDialog.add(mainPanel);
        failureDialog.setVisible(true);
    }
    
    private void printReceipt(String receiptText) {
        // Simple print simulation - in real application, use Java Print API
        JDialog printDialog = new JDialog(this, "Print Receipt", true);
        printDialog.setSize(400, 200);
        printDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("<html><center>Receipt has been sent to printer.<br><br>You can also save this information for your records.</center></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> printDialog.dispose());
        
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);
        
        printDialog.add(panel);
        printDialog.setVisible(true);
    }
    
    private void clearForm() {
        cardNumberField.setText("");
        cardHolderField.setText("");
        expiryDateField.setText("");
        cvvField.setText("");
        upiIdField.setText("");
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }
}