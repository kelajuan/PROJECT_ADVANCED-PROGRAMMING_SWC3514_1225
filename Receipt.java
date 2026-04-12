import java.io.*;

public class Receipt {

    private RoomManager booking;
    private boolean isMember;
    private double finalPrice;
    private String paymentType;
    private double amountPaid;

    // CONSTRUCTOR
    public Receipt(RoomManager booking, boolean isMember, double finalPrice,
                   String paymentType, double amountPaid) {
        this.booking = booking;
        this.isMember = isMember;
        this.finalPrice = finalPrice;
        this.paymentType = paymentType;
        this.amountPaid = amountPaid;
    }

    // SAVE METHOD
    public void save() {
        writeReceipt();
        serializeBooking();
    }

    private String getFileName() {
        return "receipt_" + booking.getBookingID() + ".txt";
    }

    // WRITE RECEIPT (CENTERED)
    private void writeReceipt() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName()))) {

            int width = 50; // adjust width if needed

            writer.write(centerText("===== LUMINA HOTEL RECEIPT =====", width) + "\n\n");

            writer.write(centerText("Booking ID: " + booking.getBookingID(), width) + "\n");
            writer.write(centerText("Guest Name: " + booking.getGuestName(), width) + "\n");
            writer.write(centerText("Phone Number: " + booking.getPhoneNumber(), width) + "\n");
            writer.write(centerText("Room Type: " + booking.getRoomType(), width) + "\n");

            writer.write("\n");

            writer.write(centerText("Check-in Date: " + booking.getCheckIn(), width) + "\n");
            writer.write(centerText("Check-out Date: " + booking.getCheckOut(), width) + "\n");

            writer.write("\n");

            writer.write(centerText(
                "Membership: " + (isMember ? "YES (15% Discount)" : "NO"), width) + "\n");

            writer.write(centerText("----------------------------------------", width) + "\n");

            writer.write(centerText("PAYMENT METHOD: " + paymentType.toUpperCase(), width) + "\n");
            writer.write(centerText("FINAL TOTAL: RM " + String.format("%.2f", finalPrice), width) + "\n");
            writer.write(centerText("AMOUNT PAID: RM " + String.format("%.2f", amountPaid), width) + "\n");

            double change = (paymentType.equalsIgnoreCase("Cash")) ? (amountPaid - finalPrice) : 0.0;
            writer.write(centerText("CHANGE DUE: RM " + String.format("%.2f", change), width) + "\n");

            writer.write(centerText("========================================", width) + "\n");
            writer.write(centerText("Thank you for choosing Lumina!", width) + "\n");

        } catch (IOException e) {
            System.out.println("Error saving receipt: " + e.getMessage());
        }
    }

    // SERIALIZATION
    private void serializeBooking() {
        String fileName = "booking_" + booking.getBookingID() + ".ser";

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(fileName))) {

            oos.writeObject(booking);
            System.out.println("Booking saved as .ser file!");

        } catch (IOException e) {
            System.out.println("Serialization error: " + e.getMessage());
        }
    }

    // READ RECEIPT
    public String readReceipt() {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(getFileName()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

        } catch (IOException e) {
            content.append("Error reading receipt: ").append(e.getMessage());
        }

        return content.toString();
    }

    // CENTER TEXT METHOD (IMPORTANT)
    private String centerText(String text, int width) {
        if (text.length() >= width) return text;

        int padding = (width - text.length()) / 2;
        String spaces = " ".repeat(padding);

        return spaces + text;
    }
}
