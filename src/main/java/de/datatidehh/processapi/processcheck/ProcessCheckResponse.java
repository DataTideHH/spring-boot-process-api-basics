package de.datatidehh.processapi.processcheck;

import java.time.LocalDateTime;

public record ProcessCheckResponse(
        Long id,
        String processName,
        String owner,
        ProcessStatus status,
        LocalDateTime lastCheckedAt,
        Integer slaMinutes
) {
    public static ProcessCheckResponse fromEntity(ProcessCheck processCheck) {
        return new ProcessCheckResponse(
                processCheck.getId(),
                processCheck.getProcessName(),
                processCheck.getOwner(),
                processCheck.getStatus(),
                processCheck.getLastCheckedAt(),
                processCheck.getSlaMinutes()
        );
    }
}
