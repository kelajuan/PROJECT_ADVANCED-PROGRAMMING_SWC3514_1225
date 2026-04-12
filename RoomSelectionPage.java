import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDate;

public class RoomSelectionPage extends JFrame {

    public RoomSelectionPage() {
        super("Choose Your Relaxation");
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        mainPanel.setBackground(new Color(80, 12, 35));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        // Room Cards
        mainPanel.add(createRoomCard("\"Lan\" Comfy Sleep", "Double Bed", "double.png", "Double", "<html>35–45㎡<br>Floor: 10-30<br>Free Wi-Fi<br>Air conditioning<br>Private Bathroom</html>"));
        mainPanel.add(createRoomCard("\"Lan\" River-View", "Double Bed", "river.png", "Double", "<html>35–45㎡<br>Floor: 17-33<br>Free Wi-Fi<br>Air conditioning<br>Private Bathroom</html>"));
        mainPanel.add(createRoomCard("\"Lan\" Business", "Twin Bed", "twin.png", "Twin", "<html>50–60㎡<br>Floor: 12-32<br>Free Wi-Fi<br>Air conditioning<br>Private Bathroom</html>"));

        add(mainPanel);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createRoomCard(String name, String bedType, String imgPath, String logicType, String description) {
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridx = 0;

        JLabel lblTitle = new JLabel(name);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 0;
        card.add(lblTitle, gbc);

        ImageIcon icon = new ImageIcon(imgPath);
        Image img = icon.getImage().getScaledInstance(280, 180, Image.SCALE_SMOOTH);
        JLabel lblImg = new JLabel(new ImageIcon(img));
        gbc.gridy = 1;
        card.add(lblImg, gbc);

        JLabel lblInfo = new JLabel(bedType);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 2;
        card.add(lblInfo, gbc);

        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        card.add(lblDesc, gbc);

        double rate = RoomManager.getRate(logicType);
        JLabel lblPrice = new JLabel("RM " + rate + " / night");
        lblPrice.setFont(new Font("Arial", Font.BOLD, 16));
        lblPrice.setForeground(new Color(80, 12, 35));
        gbc.gridy = 4;
        card.add(lblPrice, gbc);

        JButton btnSelect = new JButton("Select This Room");
        gbc.gridy = 5;
        card.add(btnSelect, gbc);

        btnSelect.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField dateInField = new JTextField("2026-04-08");
            JTextField dateOutField = new JTextField("2026-04-10");

            Object[] inputFields = {
                "Guest Name:", nameField,
                "Phone Number:", phoneField,
                "Check-in (YYYY-MM-DD):", dateInField,
                "Check-out (YYYY-MM-DD):", dateOutField
            };

            int option = JOptionPane.showConfirmDialog(this, inputFields, "Booking Details", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    LocalDate in = LocalDate.parse(dateInField.getText());
                    LocalDate out = LocalDate.parse(dateOutField.getText());
                    long nights = java.time.temporal.ChronoUnit.DAYS.between(in, out);

                    // 1. Initialize RoomManager for Validation
                    RoomManager booking = new RoomManager(nameField.getText(), phoneField.getText(), logicType, in, out);

                    if (booking.validateData()) {
                        
                        // 2. Determine Membership Status
                        int memberOpt = JOptionPane.showConfirmDialog(this, "Is this guest a Lumina Member?", "Membership Check", JOptionPane.YES_NO_OPTION);
                        boolean isMember = (memberOpt == JOptionPane.YES_OPTION);

                        // 3. Calculate Final Total and Show to User BEFORE asking payment
                        BillingProcessor billing = new BillingProcessor(isMember, nights);
                        double finalTotal = billing.applyMemberDiscount(booking.getTotalPrice());

                        String summaryMsg = String.format(
                            "Booking Summary for %s:\n" +
                            "Room: %s (%d nights)\n" +
                            "----------------------------\n" +
                            "Total Amount Due: RM %.2f\n\n" +
                            "Proceed to Payment?",
                            booking.getGuestName(), name, nights, finalTotal
                        );

                        int proceed = JOptionPane.showConfirmDialog(this, summaryMsg, "Confirm Total", JOptionPane.OK_CANCEL_OPTION);
                        
                        if (proceed == JOptionPane.OK_OPTION) {
                            
                            // 4. Select Payment Method
                            String[] payOptions = {"Cash", "Card"};
                            int payTypeOpt = JOptionPane.showOptionDialog(this, 
                                "Final Total: RM " + String.format("%.2f", finalTotal) + "\nSelect Method:", 
                                "Lumina Payment System", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, payOptions, payOptions[0]);

                            if (payTypeOpt == -1) return; 
                            String paymentType = payOptions[payTypeOpt];
                            double amountPaid = 0;

                            // 5. Handle Cash Input specifically
                            if (paymentType.equals("Cash")) {
                                String cashInput = JOptionPane.showInputDialog(this, "Total Due: RM " + String.format("%.2f", finalTotal) + "\nEnter Cash Received:");
                                if (cashInput == null || cashInput.isEmpty()) return;
                                amountPaid = Double.parseDouble(cashInput);
                            } else {
                                // For Card, we assume exact payment
                                amountPaid = finalTotal;
                            }

                            // 6. Process Payment through the Logic Layer (BillingProcessor)
                            try {
                                billing.processPayment(booking.getTotalPrice(), paymentType, amountPaid);
                                
                                // 7. Success Sequence: Save Data and Show Receipt
                                Receipt receipt = new Receipt(booking, isMember, finalTotal, paymentType, amountPaid);
                                receipt.save();

                                JTextArea textArea = new JTextArea(receipt.readReceipt());
                                textArea.setEditable(false);
                                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                                JScrollPane scrollPane = new JScrollPane(textArea);
                                scrollPane.setPreferredSize(new Dimension(400, 350));
                                
                                JOptionPane.showMessageDialog(this, scrollPane, "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
                                dispose();

                            } catch (StayTerminatedException ex) {
                                JOptionPane.showMessageDialog(this, "POLICY ERROR: " + ex.getMessage(), "Stay Terminated", JOptionPane.ERROR_MESSAGE);
                            } catch (InvalidPaymentException ex) {
                                JOptionPane.showMessageDialog(this, "PAYMENT ERROR: " + ex.getMessage(), "Transaction Failed", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Validation Failed: " + booking.getErrorMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: Invalid format or system error. Please try again.");
                }
            }
        });
        return card;
    }
}
