package Lab10.service;

public interface PaymentService {
    void processPayment(String reference, double amount) throws InsufficientFundsException;
}
