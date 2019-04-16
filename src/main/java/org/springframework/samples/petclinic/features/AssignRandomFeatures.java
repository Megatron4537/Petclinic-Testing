package org.springframework.samples.petclinic.features;

import java.util.concurrent.ThreadLocalRandom;

public class AssignRandomFeatures {

    public AssignRandomFeatures() {

    }

    public boolean getFeatureA(int haveFeature) {
        int random = ThreadLocalRandom.current().nextInt(1, 250);
        if(haveFeature >= random) {
            return true;
        }
        return false;
    }

    public boolean getFeatureB(int haveFeature) {
        int random = ThreadLocalRandom.current().nextInt(1, 250);
        if(haveFeature >= random) {
            return true;
        }
        return false;
    }
}
