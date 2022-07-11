package com.jobs.matchingengine.domains;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Job {

    private boolean driverLicenseRequired;
    private List<String> requiredCertificates;
    private Location location;
    private String billRate;
    private String workersRequired;
    private String startDate;
    private String jobTitle;
    private String company;
    private String guid;
    private String jobId;

}
