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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProcessCheckApiIntegrationTests {

    private static final long MISSING_ID = 999_999L;
    private static final int MAX_TEXT_LENGTH = 120;

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
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath(
                        "$.content[*].status",
                        containsInAnyOrder("OK", "WARNING")
                ))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$.page.size", is(20)))
                .andExpect(jsonPath("$.page.totalElements", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(1)));
    }

    @Test
    void findAllUsesRequestedPageSizeAndSortOrder() throws Exception {
        mockMvc.perform(get("/api/process-checks")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "processName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath(
                        "$.content[0].processName",
                        is("Daily sales import")
                ))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$.page.size", is(1)))
                .andExpect(jsonPath("$.page.totalElements", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(2)));
    }

    @Test
    void findAllWithOkStatusReturnsOnlyOkRecords() throws Exception {
        mockMvc.perform(get("/api/process-checks")
                        .param("status", "OK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath(
                        "$.content[*].status",
                        everyItem(is("OK"))
                ))
                .andExpect(jsonPath(
                        "$.content[0].processName",
                        is("Daily sales import")
                ));
    }

    @Test
    void findAllWithCriticalStatusReturnsEmptyPageWhenNoRecordsMatch()
            throws Exception {
        mockMvc.perform(get("/api/process-checks")
                        .param("status", "CRITICAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.page.totalElements", is(0)))
                .andExpect(jsonPath("$.page.totalPages", is(0)));
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

    @Test
    void createReturnsCreatedRecordAndLocationHeader() throws Exception {
        mockMvc.perform(post("/api/process-checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson(
                                "Customer data quality check",
                                "Data Quality",
                                "CRITICAL",
                                "2026-07-11T08:30:00",
                                30
                        )))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        containsString("/api/process-checks/")
                ))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath(
                        "$.processName",
                        is("Customer data quality check")
                ))
                .andExpect(jsonPath("$.status", is("CRITICAL")));

        org.assertj.core.api.Assertions.assertThat(repository.findAll())
                .hasSize(3)
                .anySatisfy(processCheck -> {
                    org.assertj.core.api.Assertions.assertThat(
                            processCheck.getProcessName()
                    ).isEqualTo("Customer data quality check");
                    org.assertj.core.api.Assertions.assertThat(
                            processCheck.getStatus()
                    ).isEqualTo(ProcessStatus.CRITICAL);
                });
    }

    @Test
    void createWithInvalidRequestReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/process-checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson(
                                " ",
                                " ",
                                "OK",
                                "2026-07-11T08:30:00",
                                0
                        )))
                .andExpect(status().isBadRequest());

        org.assertj.core.api.Assertions.assertThat(repository.findAll())
                .hasSize(2);
    }

    @Test
    void createWithOversizedProcessNameReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/process-checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson(
                                "x".repeat(MAX_TEXT_LENGTH + 1),
                                "Data Quality",
                                "OK",
                                "2026-07-11T08:30:00",
                                30
                        )))
                .andExpect(status().isBadRequest());

        org.assertj.core.api.Assertions.assertThat(repository.findAll())
                .hasSize(2);
    }

    @Test
    void createWithOversizedOwnerReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/process-checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson(
                                "Customer data quality check",
                                "x".repeat(MAX_TEXT_LENGTH + 1),
                                "OK",
                                "2026-07-11T08:30:00",
                                30
                        )))
                .andExpect(status().isBadRequest());

        org.assertj.core.api.Assertions.assertThat(repository.findAll())
                .hasSize(2);
    }

    @Test
    void updateChangesAndPersistsExistingRecord() throws Exception {
        mockMvc.perform(put(
                        "/api/process-checks/{id}",
                        okProcessCheckId
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson(
                                "Daily sales import - corrected",
                                "BI Operations",
                                "WARNING",
                                "2026-07-11T09:15:00",
                                45
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.id",
                        is(okProcessCheckId.intValue())
                ))
                .andExpect(jsonPath(
                        "$.processName",
                        is("Daily sales import - corrected")
                ))
                .andExpect(jsonPath("$.owner", is("BI Operations")))
                .andExpect(jsonPath("$.status", is("WARNING")))
                .andExpect(jsonPath("$.slaMinutes", is(45)));

        ProcessCheck updated = repository.findById(okProcessCheckId)
                .orElseThrow();

        org.assertj.core.api.Assertions.assertThat(updated.getProcessName())
                .isEqualTo("Daily sales import - corrected");
        org.assertj.core.api.Assertions.assertThat(updated.getStatus())
                .isEqualTo(ProcessStatus.WARNING);
        org.assertj.core.api.Assertions.assertThat(updated.getSlaMinutes())
                .isEqualTo(45);
    }

    @Test
    void updateReturnsProblemDetailWhenRecordDoesNotExist()
            throws Exception {
        mockMvc.perform(put(
                        "/api/process-checks/{id}",
                        MISSING_ID
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson(
                                "Missing process",
                                "Data Operations",
                                "OK",
                                "2026-07-11T09:15:00",
                                60
                        )))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_PROBLEM_JSON
                ))
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void deleteRemovesExistingRecordAndReturnsNoContent() throws Exception {
        mockMvc.perform(delete(
                        "/api/process-checks/{id}",
                        okProcessCheckId
                ))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        org.assertj.core.api.Assertions.assertThat(
                repository.existsById(okProcessCheckId)
        ).isFalse();
    }

    @Test
    void deleteReturnsProblemDetailWhenRecordDoesNotExist()
            throws Exception {
        mockMvc.perform(delete(
                        "/api/process-checks/{id}",
                        MISSING_ID
                ))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_PROBLEM_JSON
                ))
                .andExpect(jsonPath("$.status", is(404)));
    }

    private String requestJson(
            String processName,
            String owner,
            String status,
            String lastCheckedAt,
            int slaMinutes
    ) {
        return """
                {
                  "processName": "%s",
                  "owner": "%s",
                  "status": "%s",
                  "lastCheckedAt": "%s",
                  "slaMinutes": %d
                }
                """.formatted(
                processName,
                owner,
                status,
                lastCheckedAt,
                slaMinutes
        );
    }
}
