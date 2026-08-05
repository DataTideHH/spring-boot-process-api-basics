package de.datatidehh.processapi.processcheck;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessCheckRepository extends JpaRepository<ProcessCheck, Long> {

    Page<ProcessCheck> findAllByStatus(ProcessStatus status, Pageable pageable);
}
