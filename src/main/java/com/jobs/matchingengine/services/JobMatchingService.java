package com.jobs.matchingengine.services;

import com.jobs.matchingengine.domains.Job;
import com.jobs.matchingengine.domains.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class return the matching Job list as per workerId
 * This is responsible for fetching all the worker, jobs by making a request to the urls present
 * and sending it to the Matcher Service where all the matching logic is written.
 */

@Service
public class JobMatchingService {

    private static final Logger log = LoggerFactory.getLogger(JobMatchingService.class);

    RestTemplate restTemplate = new RestTemplate();

    /**
     * This will fetch the jobs api url present in the application.properties file
     */
    @Value("${fetchJobsUrl}")
    private String fetchJobsUrl;

    /**
     * This will fetch the worker api url present in the application.properties file
     */
    @Value("${fetchWorkerUrl}")
    private String fetchWorkerUrl;

    /**
     * This will fetch the jobs totolJobFetchCount present in the application.properties file
     */
    @Value("${totalJobsToFetch}")
    private int totalJobsToFetch;

    @Autowired
    private Matcher matcher;

    public List<Job> getMatchedJobsByWorkerId(String workerId) {
        List<Job> matchingJobs = new ArrayList<>();
        try {
            Worker worker = getWorkerByWorkerId(workerId);
            List<Job> jobs = fetchAllJobs();
            matchingJobs = matcher.fetchAllMatchingJobs(jobs, worker);
            if (matchingJobs.size() > totalJobsToFetch) { // if matchingjobs count are greator the totaljobs then returning first part till totalJobs to fetch count
                matchingJobs = matchingJobs.subList(0, totalJobsToFetch);
            }
        } catch (Exception e) {
            log.error("[JobMatchingService]: Exception occured while fetching the matching jobs for worker Id {} {},", e, workerId);
        }

        log.info("[JobMatchingService]: Total Matching jobs found : {} ", matchingJobs.size());
        return matchingJobs;
    }

    /**
     * This method will fetch the work based on the worker id provided
     * This will match the workerID with the userId
     *
     * @param workerId Fetch the specific worker by worker Id
     * @return Worker
     */
    private Worker getWorkerByWorkerId(String workerId) {
        List<Worker> workers = fetchAllWorkers();
        Worker worker = workers.stream().filter(w -> w.getUserId().equals(workerId)).findAny().get();
        return worker;

    }

    /**
     * This method will fetch all the jobs by making a api request to swipe jobs api
     *
     * @return Job list
     */
    public List<Job> fetchAllJobs() {
        List<Job> jobList = new ArrayList<>();
        try {
            ResponseEntity<Job[]> jobs = restTemplate.getForEntity(fetchJobsUrl, Job[].class);
            jobList = Arrays.stream(jobs.getBody()).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[JobMatchingService]: Exception occured while fetching the jobs {}", e);
        }

        return jobList;
    }

    /**
     * This method will fetch all the workers present by making a request to swipeJobs api
     * the API url is present in the application.properties
     *
     * @return workers List
     */
    public List<Worker> fetchAllWorkers() {
        List<Worker> workerList = new ArrayList<>();
        try {
            ResponseEntity<Worker[]> workers = restTemplate.getForEntity(fetchWorkerUrl, Worker[].class);
            workerList = Arrays.stream(workers.getBody()).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("[JobMatchingService]: Exception occured while fetching the jobs {}", e);
        }
        return workerList;
    }

}
