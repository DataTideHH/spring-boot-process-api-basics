package de.datatidehh.processapi.processcheck;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProcessCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String processName;
    private String owner;

    @Enumerated(EnumType.STRING)
    private ProcessStatus status;

    private LocalDateTime lastCheckedAt;
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
