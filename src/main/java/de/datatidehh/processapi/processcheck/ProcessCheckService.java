package de.datatidehh.processapi.processcheck;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessCheckService {

    private final ProcessCheckRepository repository;

    public ProcessCheckService(ProcessCheckRepository repository) {
        this.repository = repository;
    }

    public List<ProcessCheckResponse> findAll(ProcessStatus status) {
        List<ProcessCheck> processChecks = status == null
                ? repository.findAll()
                : repository.findAllByStatus(status);

        return processChecks
                .stream()
                .map(ProcessCheckResponse::fromEntity)
                .toList();
    }

    public ProcessCheckResponse findById(Long id) {
        return repository.findById(id)
                .map(ProcessCheckResponse::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Process check not found: " + id));
    }

    public ProcessCheckResponse create(ProcessCheckRequest request) {
        ProcessCheck processCheck = new ProcessCheck(
                request.processName(),
                request.owner(),
                request.status(),
                request.lastCheckedAt(),
                request.slaMinutes()
        );

        return ProcessCheckResponse.fromEntity(repository.save(processCheck));
    }

    public ProcessCheckResponse update(Long id, ProcessCheckRequest request) {
        ProcessCheck processCheck = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Process check not found: " + id));

        processCheck.update(
                request.processName(),
                request.owner(),
                request.status(),
                request.lastCheckedAt(),
                request.slaMinutes()
        );

        return ProcessCheckResponse.fromEntity(repository.save(processCheck));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
