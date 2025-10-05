package lab8.model;

public enum OrderPriority {
    URGENT(0), VIP(1), STANDARD(2);
    private final int level;
    OrderPriority(int level) { this.level = level; }
    public int getLevel() { return level; }
}
