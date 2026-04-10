import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.io.Serializable;

public class RoomManager implements Serializable {

    //Static field for total records
    public static int totalBookings = 0;

    private static Map<String, Double> rates = new HashMap<>();
    private static double taxRate;

    //Static Initializer
    static {
        rates.put("Double", 280.0);
        rates.put("Twin", 250.0);
        taxRate = 0.06; // 6% Service Tax
    }

    //Static Method to generate unique IDs
    public static String generateBookingID() {
        return "LAN-" + (2026000 + totalBookings);
    }

    private String bookingID;
    private String guestName;
    private String phoneNumber;
    private String roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalPrice;

    public RoomManager(String name, String phone, String type,
    LocalDate in, LocalDate out) {
        // Increment static counter for each time booked in the system
        this.guestName = name;
        this.phoneNumber = phone;
        this.roomType = type;
        this.checkIn = in;
        this.checkOut = out;

        this.bookingID = generateBookingID();
        this.totalPrice = calculateFinalBill(type, in, out);

        totalBookings++;
    }

    // Helper method to calculate stay duration and cost
    private double calculateFinalBill(String type, LocalDate in,
    LocalDate out) {
        long nights = ChronoUnit.DAYS.between(in, out);
        double basePrice = rates.getOrDefault(type, 0.0) * nights;
        return basePrice + (basePrice * taxRate);
    }

    // Static getter for rates to be used in UI
    public static double getRate(String type) {
        return rates.getOrDefault(type, 0.0);
    }

    // Getters for Module 2 and 3
    public String getBookingID() { return bookingID; }
    public double getTotalPrice() { return totalPrice; }
    public String getGuestName() { return guestName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRoomType() { return roomType; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
}