package de.datatidehh.processapi.processcheck;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/process-checks")
public class ProcessCheckController {

    private final ProcessCheckService service;

    public ProcessCheckController(ProcessCheckService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProcessCheckResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProcessCheckResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ProcessCheckResponse create(@Valid @RequestBody ProcessCheckRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ProcessCheckResponse update(@PathVariable Long id, @Valid @RequestBody ProcessCheckRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
