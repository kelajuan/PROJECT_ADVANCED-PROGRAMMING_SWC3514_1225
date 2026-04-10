import java.io.*;

public class Receipt {

    private RoomManager booking;
    private boolean isMember;
    private double finalPrice;

    public Receipt(RoomManager booking, boolean isMember, double finalPrice) {
        this.booking = booking;
        this.isMember = isMember;
        this.finalPrice = finalPrice;
    }

    //SAVE METHOD (UPDATED)
    public void save() {
        writeReceipt();
        serializeBooking();
    }

    private String getFileName() {
        return "receipt_" + booking.getBookingID() + ".txt";
    }

    //WRITE RECEIPT
    private void writeReceipt() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName()))) {

            writer.write("===== LUMINA HOTEL RECEIPT =====\n");
            writer.write("Booking ID: " + booking.getBookingID() + "\n");
            writer.write("Guest Name: " + booking.getGuestName() + "\n");
            writer.write("Phone Number: " + booking.getPhoneNumber() + "\n");
            writer.write("Room Type: " + booking.getRoomType() + "\n");

            writer.write("Check-in Date: " + booking.getCheckIn() + "\n");
            writer.write("Check-out Date: " + booking.getCheckOut() + "\n");

            writer.write("Membership: " + (isMember ? "YES (15% Discount)" : "NO") + "\n");

            writer.write("--------------------------------\n");
            writer.write("FINAL TOTAL: RM " + String.format("%.2f", finalPrice) + "\n");
            writer.write("================================\n");

        } catch (IOException e) {
            System.out.println("Error saving receipt: " + e.getMessage());
        }
    }

    //SERIALIZATION
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

    //READ RECEIPT
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
}