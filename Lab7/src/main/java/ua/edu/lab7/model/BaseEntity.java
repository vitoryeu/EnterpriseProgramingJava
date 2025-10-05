package ua.edu.lab7.model;

import jakarta.persistence.*;

import java.time.Instant;

@MappedSuperclass
public abstract class BaseEntity {
    @PrePersist
    protected void onCreate() {
        if (this instanceof Auditable a) {
            a.setCreatedAt(Instant.now());
            a.setUpdatedAt(Instant.now());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (this instanceof Auditable a) {
            a.setUpdatedAt(Instant.now());
        }
    }

    public interface Auditable {
        void setCreatedAt(Instant t);

        void setUpdatedAt(Instant t);
    }
}
