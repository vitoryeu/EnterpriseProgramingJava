import java.util.*;

import Models.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        NetworkTrafficAnalyzer analyzer = new NetworkTrafficAnalyzer();
        analyzer.addSuspiciousPattern("DROP TABLE");
        analyzer.addSuspiciousPattern("..");

        analyzer.processRequest("192.168.0.1", "GET /index.html");
        analyzer.processRequest("192.168.0.2", "POST /login password=123");
        analyzer.processRequest("192.168.0.1", "GET /admin?query=DROP TABLE users");

        Thread.sleep(10); // для різниці в lastAccess
        analyzer.processRequest("10.0.0.5", "GET /etc/passwd ..");

        analyzer.printStats();

        System.out.println("High load IPs (>=2): " + analyzer.getHighLoadIps(2));
    }
}