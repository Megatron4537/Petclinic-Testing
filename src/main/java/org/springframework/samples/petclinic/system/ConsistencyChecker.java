package org.springframework.samples.petclinic.system;

import org.springframework.samples.petclinic.config.Sqlite;
import org.springframework.samples.petclinic.owner.Owner;

public class ConsistencyChecker {
    
    Sqlite sqlite;

    public ConsistencyChecker(Sqlite sqlite) {
       this.sqlite = sqlite;
    }

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
    
    private String calculateConsistency() {

            String items = "";
            //String[] array = sqlite.getCopySqliteDb();

            //check the consistency of the items for sale


            return items;

    }
}
