package de.datatidehh.processapi.processcheck;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ProcessCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String processName;

    @Column(nullable = false, length = 120)
    private String owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProcessStatus status;

    @Column(nullable = false)
    private LocalDateTime lastCheckedAt;

    @Column(nullable = false)
    private Integer slaMinutes;

    protected ProcessCheck() {
    }

    public ProcessCheck(String processName, String owner, ProcessStatus status, LocalDateTime lastCheckedAt, Integer slaMinutes) {
        this.processName = processName;
        this.owner = owner;
        this.status = status;
        this.lastCheckedAt = lastCheckedAt;
        this.slaMinutes = slaMinutes;
    }

    public Long getId() { return id; }
    public String getProcessName() { return processName; }
    public String getOwner() { return owner; }
    public ProcessStatus getStatus() { return status; }
    public LocalDateTime getLastCheckedAt() { return lastCheckedAt; }
    public Integer getSlaMinutes() { return slaMinutes; }

    public void update(String processName, String owner, ProcessStatus status, LocalDateTime lastCheckedAt, Integer slaMinutes) {
        this.processName = processName;
        this.owner = owner;
        this.status = status;
        this.lastCheckedAt = lastCheckedAt;
        this.slaMinutes = slaMinutes;
    }
}
