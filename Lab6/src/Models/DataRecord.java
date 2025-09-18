package Models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class DataRecord {
    private long id;
    private String category;
    private double value;
    private LocalDateTime timestamp;
    private int priority;
    private String status;
    private List<String> tags;

    public DataRecord(long id, String category, double value, LocalDateTime timestamp,
                      int priority, String status, List<String> tags) {
        this.id = id;
        this.category = Objects.requireNonNull(category);
        this.value = value;
        this.timestamp = Objects.requireNonNull(timestamp);
        this.priority = priority;
        this.status = Objects.requireNonNull(status);
        this.tags = Objects.requireNonNull(tags);
    }

    public long getId() { return id; }
    public String getCategory() { return category; }
    public double getValue() { return value; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getPriority() { return priority; }
    public String getStatus() { return status; }
    public List<String> getTags() { return tags; }

    public void setCategory(String category) { this.category = category; }
    public void setValue(double value) { this.value = value; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setStatus(String status) { this.status = status; }
    public void setTags(List<String> tags) { this.tags = tags; }

    @Override public String toString() {
        return "DataRecord{" + "id=" + id + ", cat='" + category + '\'' +
                ", value=" + value + ", ts=" + timestamp +
                ", pr=" + priority + ", status='" + status + '\'' +
                ", tags=" + tags + '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataRecord that)) return false;
        return id == that.id;
    }
    @Override public int hashCode() { return Long.hashCode(id); }
}