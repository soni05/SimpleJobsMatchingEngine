package com.jobs.matchingengine.domains;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Worker {

    private int ratings;
    private boolean isActive;
    private List<String> certificates;
    private List<String> skills;
    private JobSearchAddress jobSearchAddress;
    private String transportation;
    private boolean hasDriversLicense;
    private List<Availability> availability;
    private String phone;
    private String email;
    private Name name;
    private String age;
    private String guid;
    private String userId;


}
