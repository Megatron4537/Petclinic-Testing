package org.springframework.samples.petclinic.system;

import org.springframework.samples.petclinic.owner.Owner;

public class ConsistencyChecker {

    public ConsistencyChecker() {}

    public boolean compareOwners(Owner a, Owner b) {
        System.out.println("------ CONSISTENCY CHECKER ------");
        if (a.sameOwner(b)) {
            System.out.println("Owners are the same.");
            return a.sameOwner(b);
        } else {
            System.out.println("Owners are NOT the same.");
            return false;
        }
    }
}
