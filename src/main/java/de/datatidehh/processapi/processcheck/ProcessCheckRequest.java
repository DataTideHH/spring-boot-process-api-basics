package de.datatidehh.processapi.processcheck;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ProcessCheckRequest(
        @NotBlank @Size(max = 120) String processName,
        @NotBlank @Size(max = 120) String owner,
        @NotNull ProcessStatus status,
        @NotNull LocalDateTime lastCheckedAt,
        @NotNull @Min(1) Integer slaMinutes
) {
}
