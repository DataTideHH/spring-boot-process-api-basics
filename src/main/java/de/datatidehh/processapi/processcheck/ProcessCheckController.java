package de.datatidehh.processapi.processcheck;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/process-checks")
public class ProcessCheckController {

    private final ProcessCheckService service;

    public ProcessCheckController(ProcessCheckService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProcessCheckResponse> findAll(
            @RequestParam(required = false) ProcessStatus status
    ) {
        return service.findAll(status);
    }

    @GetMapping("/{id}")
    public ProcessCheckResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProcessCheckResponse> create(
            @Valid @RequestBody ProcessCheckRequest request
    ) {
        ProcessCheckResponse response = service.create(request);
        Long id = response.id();

        URI location = buildLocation(id);

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ProcessCheckResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ProcessCheckRequest request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private URI buildLocation(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}