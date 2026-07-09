package de.datatidehh.processapi.processcheck;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ProcessCheckRequest(
        @NotBlank String processName,
        @NotBlank String owner,
        @NotNull ProcessStatus status,
        @NotNull LocalDateTime lastCheckedAt,
        @NotNull @Min(1) Integer slaMinutes
) {
}
