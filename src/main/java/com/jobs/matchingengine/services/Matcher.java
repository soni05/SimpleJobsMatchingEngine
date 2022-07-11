package com.jobs.matchingengine.services;

import com.jobs.matchingengine.domains.Availability;
import com.jobs.matchingengine.domains.Job;
import com.jobs.matchingengine.domains.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for containing all the matching logic
 * for a job for a specific worker.
 */


@Component
public class Matcher {

    private static final Logger log = LoggerFactory.getLogger(Matcher.class);

    /**
     * This method will return all the matching jobs per worker.
     *
     * @param jobs   - List of all jobs available
     * @param worker A worker based on the worker Id
     * @return All the matching jobs a worker can apply
     */
    public List<Job> fetchAllMatchingJobs(List<Job> jobs, Worker worker) {
        log.info("[Matcher]: Inside the fetchAllMatchingJobs");
        List<Job> matchJobList = new ArrayList<>();

        try {

            /** This can be the first check if worker is active then only search for the jobs
             *  if(!worker.isActive()){
             throw new IllegalArgumentException("This worker is not active to search for jobs");
             }**/

            /**
             * Below code added to match the job by driving license
             */
            matchJobList = jobs.stream().filter(job -> job.isDriverLicenseRequired() == worker.isHasDriversLicense()).collect(Collectors.toList());
            log.info("[Macther ] : Matching jobs after the drivinglicencse match:  " + matchJobList.size());

            /**
             * Below code added to match the job by certificates a worker has
             */
            matchJobList = matchJobList.stream().filter(job -> isCertificateMatches(job.getRequiredCertificates(), worker.getCertificates())).collect(Collectors.toList());
            log.info("[Macther ] : Matching jobs after certificate match: " + matchJobList.size());

            /**
             * Below code needs to be improved based on the availability and startdate
             */
            // matchJobList = matchJobList.stream().filter(job -> checkAvailability(job.getStartDate(), worker.getAvailability())).collect(Collectors.toList());
            //System.out.println("after availablity " + matchJobList.size());

            /**
             *  Below code will match the job for a worker
             *  based on the max distance a worker can travel and the distance of the job
             */
            matchJobList = matchJobList.stream().filter(job ->
                    calculateDistanceInKm(job.getLocation().getLatitude(),
                            worker.getJobSearchAddress().getLatitude(),
                            job.getLocation().getLongitude(),
                            worker.getJobSearchAddress().getLongitude(),
                            worker.getJobSearchAddress().getUnit(),
                            worker.getJobSearchAddress().getMaxJobDistance())).collect(Collectors.toList());
            log.info("[Macther ] : Matching jobs after the distance match : {} ", matchJobList.size());

            /**
             * Below code has been added to match the skill of the worker by the job title.
             */
            matchJobList = matchJobList.stream().filter(job -> worker.getSkills().contains(job.getJobTitle())).collect(Collectors.toList());
            log.info("[Macther ] : Matching jobs after the skills match : {}", matchJobList.size());

        } catch (Exception e) {
            log.error("[Matcher] : Exception occured to fetch the matching jobs for the worker Id : " + worker.getUserId());
        }

        log.info("[Matcher] : Total {} Matching jobs for the worker ID {} ", matchJobList.size(), worker.getUserId());
        return matchJobList;
    }

    /**
     * @param requiredCertificates Certificate requireed for jobs
     * @param certificates         certificate a worker has
     * @return true or false based on the if a worker has even a one certificate present
     */
    private boolean isCertificateMatches(List<String> requiredCertificates, List<String> certificates) {
        int matchedCeritificate = 0;
        for (String certificate : certificates) {
            if (requiredCertificates.contains(certificate)) {
                matchedCeritificate++;
            }
        }
        //This can be removed by adding a check like minimum matched should be a specific count.
        return matchedCeritificate > 0;
    }

    private boolean checkAvailability(String startDate, List<Availability> availability) {
        return false;
    }

    /**
     * @param latitude1   latitude of the job location
     * @param latitude2   latitude fof worker job search location
     * @param longitude1  longitude of job
     * @param longitude2  longitude of the worker job search location
     * @param unit        unit of the distance km/m
     * @param maxDistance max distance a worker can travel
     * @return
     */
    private boolean calculateDistanceInKm(String latitude1, String latitude2, String longitude1, String longitude2, String unit, int maxDistance) {
        final int R = 6371; // Radius of the earth

        double lon1 = Double.parseDouble(longitude1);
        double lon2 = Double.parseDouble(longitude2);
        double lat1 = Double.parseDouble(latitude1);
        double lat2 = Double.parseDouble(latitude2);

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "km") {
            dist = dist * 1.609344;
        }
        return Math.floor(dist) <= maxDistance;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
