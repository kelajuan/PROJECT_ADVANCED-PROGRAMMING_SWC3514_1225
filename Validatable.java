// File: Validatable.java
public interface Validatable {
    // Regex to check if phone number is only digits and 10-11 chars long
    String PHONE_REGEX = "\\d{10,11}";

    // Method to be implemented to check if data is valid
    boolean validateData();

    // Method to return the specific error message to the user
    String getErrorMessage();
}