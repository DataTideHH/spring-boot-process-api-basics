package de.datatidehh.processapi.processcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProcessCheckService {

    private static final Logger log = LoggerFactory.getLogger(ProcessCheckService.class);

    private final ProcessCheckRepository repository;

    public ProcessCheckService(ProcessCheckRepository repository) {
        this.repository = repository;
    }

    public Page<ProcessCheckResponse> findAll(
            ProcessStatus status,
            Pageable pageable
    ) {
        Page<ProcessCheck> processChecks = status == null
                ? repository.findAll(pageable)
                : repository.findAllByStatus(status, pageable);

        return processChecks.map(ProcessCheckResponse::fromEntity);
    }

    public ProcessCheckResponse findById(Long id) {
        return ProcessCheckResponse.fromEntity(findEntityById(id));
    }

    @Transactional
    public ProcessCheckResponse create(ProcessCheckRequest request) {
        ProcessCheck processCheck = new ProcessCheck(
                request.processName(),
                request.owner(),
                request.status(),
                request.lastCheckedAt(),
                request.slaMinutes()
        );

        ProcessCheck savedProcessCheck = repository.save(processCheck);

        log.info(
                "Created process check id={} status={}",
                savedProcessCheck.getId(),
                savedProcessCheck.getStatus()
        );

        return ProcessCheckResponse.fromEntity(savedProcessCheck);
    }

    @Transactional
    public ProcessCheckResponse update(Long id, ProcessCheckRequest request) {
        ProcessCheck processCheck = findEntityById(id);

        processCheck.update(
                request.processName(),
                request.owner(),
                request.status(),
                request.lastCheckedAt(),
                request.slaMinutes()
        );

        log.info(
                "Updated process check id={} status={}",
                processCheck.getId(),
                processCheck.getStatus()
        );

        return ProcessCheckResponse.fromEntity(processCheck);
    }

    @Transactional
    public void delete(Long id) {
        ProcessCheck processCheck = findEntityById(id);
        repository.delete(processCheck);

        log.info("Deleted process check id={}", id);
    }

    private ProcessCheck findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProcessCheckNotFoundException(id));
    }
}
