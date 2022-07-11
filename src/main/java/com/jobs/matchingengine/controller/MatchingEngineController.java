package com.jobs.matchingengine.controller;

import com.jobs.matchingengine.domains.Job;
import com.jobs.matchingengine.services.JobMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class MatchingEngineController {

    @Autowired
    private JobMatchingService jobMatchingService;

    @GetMapping("/{userId}")
    public List<Job> getMatchingJobsByWorkerId(@PathVariable String userId) {
        List<Job> jobs = new ArrayList<>();
        try {
            jobs = jobMatchingService.getMatchedJobsByWorkerId(userId);
        } catch (Exception e) {

        }

        return jobs;
    }
}
