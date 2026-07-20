package de.datatidehh.processapi.processcheck;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProcessCheckApiIntegrationTests {

    private static final long MISSING_ID = 999_999L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProcessCheckRepository repository;

    private Long okProcessCheckId;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        okProcessCheckId = repository.save(new ProcessCheck(
                "Daily sales import",
                "Data Operations",
                ProcessStatus.OK,
                LocalDateTime.of(2026, 7, 10, 0, 25),
                60
        )).getId();

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
                .andExpect(jsonPath(
                        "$[*].status",
                        containsInAnyOrder("OK", "WARNING")
                ));
    }

    @Test
    void findAllWithOkStatusReturnsOnlyOkRecords() throws Exception {
        mockMvc.perform(get("/api/process-checks")
                        .param("status", "OK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath(
                        "$[*].status",
                        everyItem(is("OK"))
                ))
                .andExpect(jsonPath(
                        "$[0].processName",
                        is("Daily sales import")
                ));
    }

    @Test
    void findAllWithCriticalStatusReturnsEmptyArrayWhenNoRecordsMatch()
            throws Exception {
        mockMvc.perform(get("/api/process-checks")
                        .param("status", "CRITICAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findAllWithInvalidStatusReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/process-checks")
                        .param("status", "UNKNOWN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByIdReturnsExistingRecord() throws Exception {
        mockMvc.perform(get(
                        "/api/process-checks/{id}",
                        okProcessCheckId
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.id",
                        is(okProcessCheckId.intValue())
                ))
                .andExpect(jsonPath(
                        "$.processName",
                        is("Daily sales import")
                ))
                .andExpect(jsonPath("$.status", is("OK")));
    }

    @Test
    void findByIdReturnsProblemDetailWhenRecordDoesNotExist()
            throws Exception {
        mockMvc.perform(get(
                        "/api/process-checks/{id}",
                        MISSING_ID
                ))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_PROBLEM_JSON
                ))
                .andExpect(jsonPath(
                        "$.title",
                        is("Process check not found")
                ))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath(
                        "$.detail",
                        is("Process check not found: " + MISSING_ID)
                ));
    }
}