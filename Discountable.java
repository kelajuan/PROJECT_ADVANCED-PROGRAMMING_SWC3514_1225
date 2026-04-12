// File: Discountable.java
interface Discountable {
    double applyMemberDiscount(double price);
}

// File: Billing.java
interface Billing extends Discountable {
    // Updated to include payment details
    void processPayment(double totalCost, String paymentType, double amountPaid) 
        throws StayTerminatedException, InvalidPaymentException;
}
