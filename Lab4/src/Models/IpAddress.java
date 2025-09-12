package Models;

import java.util.*;
import java.sql.Timestamp;

public class IpAddress {
    private final String ip;
    private final Timestamp lastAccess;

    public IpAddress(String ip) {
        this.ip = ip;
        this.lastAccess = new Timestamp(System.currentTimeMillis());
    }

    public String getIp() {
        return ip;
    }

    public Timestamp getLastAccess() {
        return lastAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IpAddress)) return false;
        IpAddress that = (IpAddress) o;
        return Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }

    @Override
    public String toString() {
        return ip + " (last: " + lastAccess + ")";
    }
}
