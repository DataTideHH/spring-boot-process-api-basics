package de.datatidehh.processapi.processcheck;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessCheckRepository extends JpaRepository<ProcessCheck, Long> {

    List<ProcessCheck> findAllByStatus(ProcessStatus status);
}
