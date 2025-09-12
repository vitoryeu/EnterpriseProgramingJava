package Models;

import java.util.*;

public class NetworkTrafficAnalyzer {
    private Set<IpAddress> uniqueIps = new HashSet<>();
    private Set<String> suspiciousPatterns = new HashSet<>();
    private Map<IpAddress, Integer> requestCount = new HashMap<>();

    public void processRequest(String ipAddress, String requestData) {
        IpAddress ip = new IpAddress(ipAddress);
        uniqueIps.add(ip);
        requestCount.put(ip, requestCount.getOrDefault(ip, 0) + 1);

        for (String pattern : suspiciousPatterns) {
            if (requestData.contains(pattern)) {
                System.out.println("⚠ Suspicious activity from " + ipAddress);
            }
        }
    }

    public void addSuspiciousPattern(String pattern) {
        suspiciousPatterns.add(pattern);
    }

    public boolean isBlacklisted(String ipAddress) {
        return suspiciousPatterns.contains(ipAddress);
    }

    public Set<IpAddress> getHighLoadIps(int threshold) {
        Set<IpAddress> result = new HashSet<>();
        for (Map.Entry<IpAddress, Integer> entry : requestCount.entrySet()) {
            if (entry.getValue() >= threshold) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public void printStats() {
        System.out.println("=== Traffic Stats ===");
        System.out.println("Unique IPs: " + uniqueIps.size());
        for (Map.Entry<IpAddress, Integer> entry : requestCount.entrySet()) {
            System.out.println(entry.getKey().getIp() + " → " + entry.getValue() + " requests");
        }
    }
}