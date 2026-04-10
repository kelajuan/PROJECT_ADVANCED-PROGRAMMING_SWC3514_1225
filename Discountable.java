interface Discountable {
    double applyMemberDiscount(double price);
}

// Interface extension requirement
interface Billing extends Discountable {
    void processPayment(double amount) throws StayTerminatedException, InvalidPaymentException;
}