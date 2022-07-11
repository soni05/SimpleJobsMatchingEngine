package com.jobs.matchingengine.domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSearchAddress {

    private String unit;
    private int maxJobDistance;
    private String longitude;
    private String latitude;

}
