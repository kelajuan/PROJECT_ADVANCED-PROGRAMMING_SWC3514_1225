/**
 * StayTerminatedException is triggered if a guest attempts to book 
 * for a duration exceeding the maximum allowed limit (30 days).
 * This fulfills the requirement for a relevant user-defined exception.
 */
class StayTerminatedException extends Exception {
    public StayTerminatedException(String message) {
        // Pass the error message to the parent Exception class
        super(message);
    }
}

/**
 * InvalidPaymentException is triggered if the billing logic results 
 * in a total price that is logically impossible or below the minimum rate.
 * This fulfills the requirement for a second user-defined exception.
 */
class InvalidPaymentException extends Exception {
    public InvalidPaymentException(String message) {
        // Pass the error message to the parent Exception class
        super(message);
    }
}
