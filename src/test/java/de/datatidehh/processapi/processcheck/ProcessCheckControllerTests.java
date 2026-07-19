package de.datatidehh.processapi.processcheck;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProcessCheckControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProcessCheckRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(new ProcessCheck(
                "Daily sales import",
                "Data Operations",
                ProcessStatus.OK,
                LocalDateTime.of(2026, 7, 10, 0, 25),
                60
        ));
        repository.save(new ProcessCheck(
                "Monthly finance export",
                "Finance Operations",
                ProcessStatus.WARNING,
                LocalDateTime.of(2026, 7, 10, 1, 30),
                120
        ));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void findAllWithoutStatusReturnsRecordsOfAllStatuses() throws Exception {
        mockMvc.perform(get("/api/process-checks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder("OK", "WARNING")));
    }

    @Test
    void findAllWithOkStatusReturnsOnlyOkRecords() throws Exception {
        mockMvc.perform(get("/api/process-checks").param("status", "OK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].status", everyItem(is("OK"))))
                .andExpect(jsonPath("$[0].processName", is("Daily sales import")));
    }

    @Test
    void findAllWithStatusWithoutMatchesReturnsEmptyArray() throws Exception {
        mockMvc.perform(get("/api/process-checks").param("status", "CRITICAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findAllWithInvalidStatusReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/process-checks").param("status", "UNKNOWN"))
                .andExpect(status().isBadRequest());
    }
}
