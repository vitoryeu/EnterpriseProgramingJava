package Lab10.service.impl;

import Lab10.service.InsufficientFundsException;
import Lab10.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SimplePaymentService implements PaymentService {

    @Value("${app.payment.fail-amount:-1}")
    private double failAmount;

    @Override
    public void processPayment(String reference, double amount) throws InsufficientFundsException {
        if (failAmount >= 0 && amount >= failAmount) {
            throw new InsufficientFundsException();
        }
    }
}
