package com.smartwatch.leaderboard.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job rankingJob;
    private final Job gamificationJob;

    public BatchController(
            JobLauncher jobLauncher,
            @Qualifier("rankingJob") Job rankingJob,
            @Qualifier("gamificationJob") Job gamificationJob
    ) {
        this.jobLauncher = jobLauncher;
        this.rankingJob = rankingJob;
        this.gamificationJob = gamificationJob;
    }

    @GetMapping("/rank")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> runRankingJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(rankingJob, params);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("jobId", execution.getId());
            response.put("exitCode", execution.getExitStatus().getExitCode());
            response.put("message", "Spring Batch ranking engine completed successfully! Ranks assigned to all closed task and challenge participants.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "FAILED");
            response.put("message", "Failed to launch Spring Batch ranking job: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/game")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> runGamificationJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(gamificationJob, params);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("jobId", execution.getId());
            response.put("exitCode", execution.getExitStatus().getExitCode());
            response.put("message", "Spring Batch gamification and reward engine completed successfully! Badges awarded to qualifying users.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "FAILED");
            response.put("message", "Failed to launch Spring Batch gamification job: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
