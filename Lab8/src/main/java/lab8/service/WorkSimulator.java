package lab8.service;

import org.springframework.stereotype.Service;
import lab8.model.OrderEvent;
import java.util.Random;

@Service
public class WorkSimulator {
    private final Random rnd = new Random();

    public void doWork(OrderEvent e) throws InterruptedException {
        int n = 28 + rnd.nextInt(6); // трохи важче CPU
        long fib = fibIter(n);
        if (rnd.nextDouble() < 0.2) {
            Thread.sleep(20 + rnd.nextInt(30)); // 20–50мс «IO»
        }
    }

    private long fibIter(int n) {
        if (n <= 1) return n;
        long a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            long tmp = a + b;
            a = b;
            b = tmp;
        }
        return b;
    }
}
