import javax.swing.JOptionPane;

public class BillingProcessor implements Billing {
    private boolean isMember;
    private long nights;

    public BillingProcessor(boolean isMember, long nights) {
        this.isMember = isMember;
        this.nights = nights;
    }

    @Override
    public double applyMemberDiscount(double basePrice) {
        // Members get a 15% discount
        return isMember ? (basePrice * 0.85) : basePrice;
    }

    @Override
    public void processPayment(double amount) throws StayTerminatedException, InvalidPaymentException {
        // Check stay limit (30 days)
        if (nights > 30) {
            throw new StayTerminatedException("Stay exceeds maximum 30 days.");
        }

        double finalAmount = applyMemberDiscount(amount);

        if (finalAmount < 0) {
            throw new InvalidPaymentException("Payment cannot be negative.");
        }

        try {
            JOptionPane.showMessageDialog(null, "Final Total: RM " + finalAmount);
        } finally {
            // Requirement for finally block
            System.out.println("Payment processing completed.");
        }
    }
}