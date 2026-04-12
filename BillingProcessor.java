// File: BillingProcessor.java
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
        // Members get 15% discount
        return isMember ? (basePrice * 0.85) : basePrice;
    }

    @Override
    public void processPayment(double totalCost, String paymentType, double amountPaid) 
            throws StayTerminatedException, InvalidPaymentException {
        
        // 1. Requirement: Policy Check (Stay Limit)
        if (nights > 30) {
            throw new StayTerminatedException("Stay exceeds maximum 30 days.");
        }

        // 2. Calculate Final Price after Discount
        double finalAmount = applyMemberDiscount(totalCost);

        // 3. Requirement: Safety Check (Negative Input)
        if (amountPaid < 0) {
            throw new InvalidPaymentException("Payment Error: Amount cannot be negative.");
        }

        // 4. Payment Logic based on Type
        if (paymentType.equalsIgnoreCase("Cash")) {
            // Check if cash is enough
            if (amountPaid < finalAmount) {
                double missing = finalAmount - amountPaid;
                throw new InvalidPaymentException("Insufficient Cash. RM " + String.format("%.2f", missing) + " more required.");
            }

            double change = amountPaid - finalAmount;
            
            displaySuccessDialog("Cash", finalAmount, amountPaid, change);

        } else if (paymentType.equalsIgnoreCase("Card")) {
            // For card, we assume the machine processes the exact final amount
            displaySuccessDialog("Card", finalAmount, finalAmount, 0.0);
        }
        
        // Requirement: finally block is usually handled in the try-catch 
        // that calls this method, but we can log completion here.
        System.out.println("Payment transaction finalized for Type: " + paymentType);
    }

    private void displaySuccessDialog(String type, double total, double paid, double change) {
        String message = String.format(
            "--- PAYMENT SUCCESSFUL (%s) ---\n\n" +
            "Final Total   : RM %.2f\n" +
            "Amount Paid   : RM %.2f\n" +
            "Change/Balance: RM %.2f\n\n" +
            "Thank you for staying with Lumina Hotel!", 
            type.toUpperCase(), total, paid, change
        );
        JOptionPane.showMessageDialog(null, message, "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }
}
